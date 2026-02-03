package com.ifpb.sorrisus.controller.auth;

import com.ifpb.sorrisus.dto.auth.LoginRequest;
import com.ifpb.sorrisus.dto.auth.LoginResponse;
import com.ifpb.sorrisus.security.JWTUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DisplayName("Teste Unitário - AuthController")
class AuthControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    private JWTUtil jwtUtil;

    private AuthController authController;

    @Mock
    private com.ifpb.sorrisus.service.UsuarioService usuarioService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtUtil = new JWTUtil(); 
        authController = new AuthController(authenticationManager, jwtUtil, usuarioService, 3600000L);
    }

    @Test
    @DisplayName("Deve retornar token ao fazer login com credenciais válidas")
    void deveRetornarTokenAoLogin() {
        UserDetails userDetails = new User("maria@sorrisus.com", "senha", 
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_RECEPCIONISTA")));
        Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(auth);

        LoginRequest request = new LoginRequest();
        request.setEmail("maria@sorrisus.com");
        request.setPassword("senha");

        com.ifpb.sorrisus.model.Usuario usuario = new com.ifpb.sorrisus.model.Usuario();
        usuario.setId(1L);
        usuario.setEmail("maria@sorrisus.com");

        when(usuarioService.buscarPorEmail("maria@sorrisus.com")).thenReturn(usuario);

        ResponseEntity<?> response = authController.login(request);

        assertThat(response).isNotNull();
        assertThat(response.getBody()).isInstanceOf(LoginResponse.class);

        LoginResponse body = (LoginResponse) response.getBody();
        assertThat(body.getAccessToken()).isNotBlank();
        assertThat(body.getTokenType()).isEqualTo("Bearer");
        assertThat(body.getEmail()).isEqualTo("maria@sorrisus.com");
        assertThat(body.getUserId()).isEqualTo(1);
        assertThat(body.getExpiresIn()).isGreaterThan(0);
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar login com credenciais inválidas")
    void deveLancarExcecaoComCredenciaisInvalidas() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Credenciais inválidas"));

        LoginRequest request = new LoginRequest();
        request.setEmail("maria@sorrisus.com");
        request.setPassword("senhaErrada");

        assertThatThrownBy(() -> authController.login(request))
                .isInstanceOf(BadCredentialsException.class)
                .hasMessageContaining("Credenciais inválidas");
    }

    @Test
    @DisplayName("Deve gerar token JWT válido via utilitário")
    void deveGerarTokenJwtValido() {
        String token = jwtUtil.generateToken("usuario@sorrisus.com", "ROLE_PACIENTE");
        assertThat(token).isNotNull();
        assertThat(jwtUtil.validateToken(token)).isTrue();
        assertThat(jwtUtil.getUsernameFromToken(token)).isEqualTo("usuario@sorrisus.com");
        assertThat(jwtUtil.getRoleFromToken(token)).isEqualTo("ROLE_PACIENTE");
    }
}