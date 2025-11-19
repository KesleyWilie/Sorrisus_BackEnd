package com.ifpb.sorrisus.security.filter;

import com.ifpb.sorrisus.security.JWTUtil;
import com.ifpb.sorrisus.security.service.CustomUserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@DisplayName("Teste Unitário - JwtAuthenticationFilter")
class JwtAuthenticationFilterTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    private JWTUtil jwtUtil;

    @Mock
    private CustomUserDetailsService userDetailsService;

    private JwtAuthenticationFilter jwtFilter;
    private String validToken;
    private String tokenWithoutBearer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtUtil = new JWTUtil();
        jwtFilter = new JwtAuthenticationFilter(jwtUtil, userDetailsService);
        SecurityContextHolder.clearContext();

        tokenWithoutBearer = jwtUtil.generateToken("maria@sorrisus.com", "ROLE_RECEPCIONISTA");
        validToken = "Bearer " + tokenWithoutBearer;
    }

    @Test
    @DisplayName("Deve processar requisição com token válido")
    void deveProcessarRequisicaoComTokenValido() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn(validToken);

        UserDetails userDetails = new User("maria@sorrisus.com", "password", 
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_RECEPCIONISTA")));
        when(userDetailsService.loadUserByUsername("maria@sorrisus.com"))
                .thenReturn(userDetails);

        jwtFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
    }

    @Test
    @DisplayName("Deve processar requisição sem token")
    void deveProcessarRequisicaoSemToken() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn(null);

        jwtFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }

    @Test
    @DisplayName("Deve ignorar token com prefixo incorreto")
    void deveIgnorarTokenComPrefixoIncorreto() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("Basic token123");

        jwtFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }

    @Test
    @DisplayName("Deve ignorar token inválido")
    void deveIgnorarTokenInvalido() throws ServletException, IOException {
        String invalidToken = "Bearer invalidtoken";
        when(request.getHeader("Authorization")).thenReturn(invalidToken);

        jwtFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
    }

    @Test
    @DisplayName("Deve processar multiplas requisições com tokens válidos")
    void deveProcessarMultiplasRequisicoes() throws ServletException, IOException {
        for (int i = 0; i < 3; i++) {
            String email = "user" + i + "@sorrisus.com";
            String token = jwtUtil.generateToken(email, "ROLE_PACIENTE");
            String header = "Bearer " + token;

            when(request.getHeader("Authorization")).thenReturn(header);

            UserDetails userDetails = new User(email, "password",
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_PACIENTE")));
            when(userDetailsService.loadUserByUsername(email)).thenReturn(userDetails);

            jwtFilter.doFilterInternal(request, response, filterChain);
        }

        verify(filterChain, times(3)).doFilter(request, response);
    }

    @Test
    @DisplayName("Deve processar requisição com token Bearer sem espaço")
    void deveProcessarComBearerSemEspaco() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("Bearer" + tokenWithoutBearer);

        jwtFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
    }

    @Test
    @DisplayName("Deve processar requisição com header vazio")
    void deveProcessarComHeaderVazio() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("");

        jwtFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
    }

    @Test
    @DisplayName("Deve processar token com diferentes roles")
    void deveProcessarTokenComDiferentesRoles() throws ServletException, IOException {
        String[] roles = {"ROLE_DENTISTA", "ROLE_RECEPCIONISTA", "ROLE_PACIENTE"};

        for (String role : roles) {
            String token = jwtUtil.generateToken("user@sorrisus.com", role);
            String header = "Bearer " + token;

            when(request.getHeader("Authorization")).thenReturn(header);

            UserDetails userDetails = new User("user@sorrisus.com", "password",
                    Collections.singletonList(new SimpleGrantedAuthority(role)));
            when(userDetailsService.loadUserByUsername("user@sorrisus.com")).thenReturn(userDetails);

            jwtFilter.doFilterInternal(request, response, filterChain);
        }

        verify(filterChain, times(3)).doFilter(request, response);
    }

    @Test
    @DisplayName("Deve carregar user details quando token é válido")
    void deveCarregarUserDetailsComTokenValido() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn(validToken);

        UserDetails userDetails = new User("maria@sorrisus.com", "password",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_RECEPCIONISTA")));
        when(userDetailsService.loadUserByUsername("maria@sorrisus.com"))
                .thenReturn(userDetails);

        jwtFilter.doFilterInternal(request, response, filterChain);

        verify(userDetailsService).loadUserByUsername("maria@sorrisus.com");
        verify(filterChain).doFilter(request, response);
    }

    @Test
    @DisplayName("Deve não chamar userDetailsService quando header é nulo")
    void deveNaoCarregarUserDetailsQuandoHeaderNulo() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn(null);

        jwtFilter.doFilterInternal(request, response, filterChain);

        verify(userDetailsService, never()).loadUserByUsername(anyString());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    @DisplayName("Deve não chamar userDetailsService quando prefixo é inválido")
    void deveNaoCarregarUserDetailsComPrefixoInvalido() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("Basic token123");

        jwtFilter.doFilterInternal(request, response, filterChain);

        verify(userDetailsService, never()).loadUserByUsername(anyString());
        verify(filterChain).doFilter(request, response);
    }
}