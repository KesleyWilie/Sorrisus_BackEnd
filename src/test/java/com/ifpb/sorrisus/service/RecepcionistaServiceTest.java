package com.ifpb.sorrisus.service;

import com.ifpb.sorrisus.model.Recepcionista;
import com.ifpb.sorrisus.repository.RecepcionistaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("Teste Unitário - RecepcionistaService")
class RecepcionistaServiceTest {

    @Mock
    private RecepcionistaRepository recepcionistaRepository;

    @InjectMocks
    private RecepcionistaService recepcionistaService;

    private Recepcionista recepcionista;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        recepcionista = new Recepcionista();
        recepcionista.setId(1L);
        recepcionista.setNome("Carla Lima");
        recepcionista.setEmail("carla@sorrisus.com");
        recepcionista.setTurno("Manhã");
    }

    @Test
    @DisplayName("Deve salvar recepcionista com sucesso")
    void deveSalvarRecepcionista() {
        when(recepcionistaRepository.save(any(Recepcionista.class))).thenReturn(recepcionista);

        Recepcionista salvo = recepcionistaService.salvar(recepcionista);

        assertThat(salvo).isNotNull();
        assertThat(salvo.getNome()).isEqualTo("Carla Lima");
        verify(recepcionistaRepository, times(1)).save(recepcionista);
    }

    @Test
    @DisplayName("Deve listar recepcionistas")
    void deveListarRecepcionistas() {
        when(recepcionistaRepository.findAll()).thenReturn(List.of(recepcionista));

        List<Recepcionista> lista = recepcionistaService.listarTodos();

        assertThat(lista).hasSize(1);
        assertThat(lista.get(0).getEmail()).isEqualTo("carla@sorrisus.com");
        verify(recepcionistaRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve buscar recepcionista por ID com sucesso")
    void deveBuscarRecepcionistaPorId() {
        when(recepcionistaRepository.findById(1L)).thenReturn(Optional.of(recepcionista));

        Optional<Recepcionista> resultado = recepcionistaService.buscarPorId(1L);

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getId()).isEqualTo(1L);
        verify(recepcionistaRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Deve retornar Optional vazio ao buscar recepcionista inexistente")
    void deveRetornarOptionalVazioAoBuscarInexistente() {
        when(recepcionistaRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Recepcionista> resultado = recepcionistaService.buscarPorId(99L);

        assertThat(resultado).isEmpty();
        verify(recepcionistaRepository, times(1)).findById(99L);
    }

    @Test
    @DisplayName("Deve atualizar recepcionista com sucesso")
    void deveAtualizarRecepcionistaComSucesso() {
        Recepcionista atualizado = new Recepcionista();
        atualizado.setNome("Carla Atualizada");
        atualizado.setEmail("carla.atualizada@sorrisus.com");

        when(recepcionistaRepository.findById(1L)).thenReturn(Optional.of(recepcionista));
        when(recepcionistaRepository.save(any(Recepcionista.class))).thenAnswer(inv -> inv.getArgument(0));

        Recepcionista resultado = recepcionistaService.atualizar(1L, atualizado);

        assertThat(resultado.getNome()).isEqualTo("Carla Atualizada");
        assertThat(resultado.getEmail()).isEqualTo("carla.atualizada@sorrisus.com");
        verify(recepcionistaRepository, times(1)).findById(1L);
        verify(recepcionistaRepository, times(1)).save(any(Recepcionista.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar atualizar recepcionista inexistente")
    void deveLancarExcecaoAoAtualizarInexistente() {
        Recepcionista atualizado = new Recepcionista();
        atualizado.setNome("Nome Qualquer");

        when(recepcionistaRepository.findById(2L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> recepcionistaService.atualizar(2L, atualizado))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Recepcionista não encontrado");

        verify(recepcionistaRepository, times(1)).findById(2L);
        verify(recepcionistaRepository, never()).save(any(Recepcionista.class));
    }

    @Test
    @DisplayName("Deve deletar recepcionista com sucesso")
    void deveDeletarRecepcionista() {
        doNothing().when(recepcionistaRepository).deleteById(1L);

        recepcionistaService.deletar(1L);

        verify(recepcionistaRepository, times(1)).deleteById(1L);
    }
}