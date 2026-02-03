package com.ifpb.sorrisus.security.service;

import com.ifpb.sorrisus.model.Role;
import com.ifpb.sorrisus.model.Usuario;
import com.ifpb.sorrisus.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@DisplayName("Teste Unitário - CustomUserDetailsService")
class CustomUserDetailsServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private CustomUserDetailsService userDetailsService;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("Maria Souza");
        usuario.setEmail("maria@sorrisus.com");
        usuario.setSenha("$2a$10$dXJ3U0pXRGpVVG1FVG1FUQ==");
        usuario.setRole(Role.ROLE_RECEPCIONISTA);
    }

    @Test
    @DisplayName("Deve carregar usuário por username com sucesso")
    void deveCarregarUsuarioPorUsernameComSucesso() {
        when(usuarioRepository.findByEmail("maria@sorrisus.com"))
                .thenReturn(Optional.of(usuario));

        UserDetails userDetails = userDetailsService.loadUserByUsername("maria@sorrisus.com");

        assertThat(userDetails).isNotNull();
        assertThat(userDetails.getUsername()).isEqualTo("maria@sorrisus.com");
        assertThat(userDetails.getAuthorities()).isNotEmpty();
        verify(usuarioRepository, times(1)).findByEmail("maria@sorrisus.com");
    }

    @Test
    @DisplayName("Deve lançar exceção ao carregar usuário não existente")
    void deveLancarExcecaoAoCarregarUsuarioInexistente() {
        when(usuarioRepository.findByEmail("inexistente@sorrisus.com"))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> userDetailsService.loadUserByUsername("inexistente@sorrisus.com"))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("Usuário não encontrado");

        verify(usuarioRepository, times(1)).findByEmail("inexistente@sorrisus.com");
    }

    @Test
    @DisplayName("Deve carregar usuário com role ROLE_DENTISTA")
    void deveCarregarUsuarioComRoleDentista() {
        usuario.setRole(Role.ROLE_DENTISTA);
        when(usuarioRepository.findByEmail("dentista@sorrisus.com"))
                .thenReturn(Optional.of(usuario));

        UserDetails userDetails = userDetailsService.loadUserByUsername("dentista@sorrisus.com");

        assertThat(userDetails.getAuthorities()).anyMatch(auth -> auth.getAuthority().equals("ROLE_DENTISTA"));
    }

    @Test
    @DisplayName("Deve carregar usuário com role ROLE_PACIENTE")
    void deveCarregarUsuarioComRolePaciente() {
        usuario.setRole(Role.ROLE_PACIENTE);
        when(usuarioRepository.findByEmail("paciente@sorrisus.com"))
                .thenReturn(Optional.of(usuario));

        UserDetails userDetails = userDetailsService.loadUserByUsername("paciente@sorrisus.com");

        assertThat(userDetails.getAuthorities()).anyMatch(auth -> auth.getAuthority().equals("ROLE_PACIENTE"));
    }

    @Test
    @DisplayName("Deve carregar múltiplos usuários com emails diferentes")
    void deveCarregarMultiplosUsuarios() {
        Usuario usuario2 = new Usuario();
        usuario2.setId(2L);
        usuario2.setNome("João Silva");
        usuario2.setEmail("joao@sorrisus.com");
        usuario2.setSenha("$2a$10$senhaHashada123456789");
        usuario2.setRole(Role.ROLE_PACIENTE);

        when(usuarioRepository.findByEmail("maria@sorrisus.com"))
                .thenReturn(Optional.of(usuario));
        when(usuarioRepository.findByEmail("joao@sorrisus.com"))
                .thenReturn(Optional.of(usuario2));

        UserDetails details1 = userDetailsService.loadUserByUsername("maria@sorrisus.com");
        UserDetails details2 = userDetailsService.loadUserByUsername("joao@sorrisus.com");

        assertThat(details1.getUsername()).isEqualTo("maria@sorrisus.com");
        assertThat(details2.getUsername()).isEqualTo("joao@sorrisus.com");
        assertThat(details1.getAuthorities()).anyMatch(auth -> auth.getAuthority().equals("ROLE_RECEPCIONISTA"));
        assertThat(details2.getAuthorities()).anyMatch(auth -> auth.getAuthority().equals("ROLE_PACIENTE"));
        verify(usuarioRepository, times(2)).findByEmail(anyString());
    }

    @Test
    @DisplayName("Deve lançar exceção com email nulo")
    void deveLancarExcecaoComEmailNulo() {
        when(usuarioRepository.findByEmail(null)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userDetailsService.loadUserByUsername(null))
                .isInstanceOf(UsernameNotFoundException.class);
    }

    @Test
    @DisplayName("Deve lançar exceção com email vazio")
    void deveLancarExcecaoComEmailVazio() {
        when(usuarioRepository.findByEmail(""))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> userDetailsService.loadUserByUsername(""))
                .isInstanceOf(UsernameNotFoundException.class);
    }

    @Test
    @DisplayName("Deve carregar usuário com senha criptografada")
    void deveCarregarUsuarioComSenhaCriptografada() {
        String senhaCriptografada = "$2a$10$N9qo8uLOickgx2ZMRZoM2eIjZAgcg7b3XeKeUxWdeS86E36gBS/O6";
        usuario.setSenha(senhaCriptografada);
        
        when(usuarioRepository.findByEmail("maria@sorrisus.com"))
                .thenReturn(Optional.of(usuario));

        UserDetails userDetails = userDetailsService.loadUserByUsername("maria@sorrisus.com");

        assertThat(userDetails.getPassword()).isEqualTo(senhaCriptografada);
    }

    @Test
    @DisplayName("Deve conter apenas uma autoridade por usuário")
    void deveConterApenasUmaAutoridadePorUsuario() {
        when(usuarioRepository.findByEmail("maria@sorrisus.com"))
                .thenReturn(Optional.of(usuario));

        UserDetails userDetails = userDetailsService.loadUserByUsername("maria@sorrisus.com");

        assertThat(userDetails.getAuthorities()).hasSize(1);
    }

    @Test
    @DisplayName("Deve carregar usuário com nome completo")
    void deveCarregarUsuarioComNomeCompleto() {
        usuario.setNome("Maria Silva Souza Santos");
        
        when(usuarioRepository.findByEmail("maria@sorrisus.com"))
                .thenReturn(Optional.of(usuario));

        UserDetails userDetails = userDetailsService.loadUserByUsername("maria@sorrisus.com");

        assertThat(userDetails.getUsername()).isEqualTo("maria@sorrisus.com");
    }

    @Test
    @DisplayName("Deve estar habilitado após carregar")
    void deveEstarHabilitadoAposCarregar() {
        when(usuarioRepository.findByEmail("maria@sorrisus.com"))
                .thenReturn(Optional.of(usuario));

        UserDetails userDetails = userDetailsService.loadUserByUsername("maria@sorrisus.com");

        assertThat(userDetails.isEnabled()).isTrue();
        assertThat(userDetails.isAccountNonExpired()).isTrue();
        assertThat(userDetails.isCredentialsNonExpired()).isTrue();
        assertThat(userDetails.isAccountNonLocked()).isTrue();
    }
}