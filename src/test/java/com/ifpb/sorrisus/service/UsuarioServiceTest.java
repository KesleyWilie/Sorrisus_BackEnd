package com.ifpb.sorrisus.service;

import com.ifpb.sorrisus.model.Role;
import com.ifpb.sorrisus.model.Usuario;
import com.ifpb.sorrisus.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("Teste Unitário - UsuarioService")
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuarioBase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        usuarioBase = new Usuario();
        usuarioBase.setId(1L);
        usuarioBase.setNome("Maria Souza");
        usuarioBase.setEmail("maria@sorrisus.com");
        usuarioBase.setSenha("123456");
        usuarioBase.setRole(Role.ROLE_RECEPCIONISTA);
    }

    @Test
    @DisplayName("Deve salvar um usuário com sucesso")
    void deveSalvarUsuario() {
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioBase);

        Usuario salvo = usuarioService.salvar(usuarioBase);

        assertThat(salvo).isNotNull();
        assertThat(salvo.getNome()).isEqualTo("Maria Souza");
        verify(usuarioRepository, times(1)).save(usuarioBase);
    }

    @Test
    @DisplayName("Deve listar todos os usuários")
    void deveListarUsuarios() {
        when(usuarioRepository.findAll()).thenReturn(Arrays.asList(usuarioBase));

        List<Usuario> usuarios = usuarioService.listarTodos();

        assertThat(usuarios).hasSize(1);
        assertThat(usuarios.get(0).getEmail()).isEqualTo("maria@sorrisus.com");
    }

    @Test
    @DisplayName("Deve buscar usuário por ID e validar dados")
    void deveBuscarPorIdValidarDados() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioBase));

        Optional<Usuario> resultado = usuarioService.buscarPorId(1L);

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Deve deletar usuário com sucesso")
    void deveDeletarUsuario() {
        doNothing().when(usuarioRepository).deleteById(1L);

        usuarioService.deletar(1L);

        verify(usuarioRepository, times(1)).deleteById(1L);
    }
}
