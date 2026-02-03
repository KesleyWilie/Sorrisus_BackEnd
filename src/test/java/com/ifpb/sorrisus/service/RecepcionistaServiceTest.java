package com.ifpb.sorrisus.service;

import com.ifpb.sorrisus.exception.InvalidFieldException;
import com.ifpb.sorrisus.exception.ResourceNotFoundException;
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
        recepcionista.setTurno("MANHA");
    }

    @Test
    @DisplayName("Deve salvar recepcionista com sucesso")
    void deveSalvarRecepcionista() {
        when(recepcionistaRepository.save(any(Recepcionista.class))).thenReturn(recepcionista);

        Recepcionista salvo = recepcionistaService.salvar(recepcionista);

        assertThat(salvo).isNotNull();
        assertThat(salvo.getNome()).isEqualTo("Carla Lima");
        assertThat(salvo.getTurno()).isEqualTo("MANHA");
        verify(recepcionistaRepository, times(1)).save(recepcionista);
    }

    @Test
    @DisplayName("Deve lançar exceção ao salvar recepcionista com turno nulo")
    void deveLancarExcecaoAoSalvarComTurnoNulo() {
        recepcionista.setTurno(null);

        assertThatThrownBy(() -> recepcionistaService.salvar(recepcionista))
                .isInstanceOf(InvalidFieldException.class)
                .hasMessageContaining("O turno é obrigatório");

        verify(recepcionistaRepository, never()).save(any(Recepcionista.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao salvar recepcionista com turno inválido")
    void deveLancarExcecaoAoSalvarComTurnoInvalido() {
        recepcionista.setTurno("MADRUGADA");

        assertThatThrownBy(() -> recepcionistaService.salvar(recepcionista))
                .isInstanceOf(InvalidFieldException.class)
                .hasMessageContaining("Turno inválido")
                .hasMessageContaining("MANHA, TARDE, NOITE");

        verify(recepcionistaRepository, never()).save(any(Recepcionista.class));
    }

    @Test
    @DisplayName("Deve salvar recepcionista com turno TARDE")
    void deveSalvarRecepcionistaComTurnoTarde() {
        recepcionista.setTurno("TARDE");
        when(recepcionistaRepository.save(any(Recepcionista.class))).thenReturn(recepcionista);

        Recepcionista salvo = recepcionistaService.salvar(recepcionista);

        assertThat(salvo).isNotNull();
        assertThat(salvo.getTurno()).isEqualTo("TARDE");
        verify(recepcionistaRepository, times(1)).save(recepcionista);
    }

    @Test
    @DisplayName("Deve salvar recepcionista com turno NOITE")
    void deveSalvarRecepcionistaComTurnoNoite() {
        recepcionista.setTurno("NOITE");
        when(recepcionistaRepository.save(any(Recepcionista.class))).thenReturn(recepcionista);

        Recepcionista salvo = recepcionistaService.salvar(recepcionista);

        assertThat(salvo).isNotNull();
        assertThat(salvo.getTurno()).isEqualTo("NOITE");
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

        Recepcionista resultado = recepcionistaService.buscarPorId(1L);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getNome()).isEqualTo("Carla Lima");
        verify(recepcionistaRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar recepcionista inexistente")
    void deveLancarExcecaoAoBuscarInexistente() {
        when(recepcionistaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> recepcionistaService.buscarPorId(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Recepcionista com id 99 não encontrado");

        verify(recepcionistaRepository, times(1)).findById(99L);
    }

    @Test
    @DisplayName("Deve atualizar recepcionista com sucesso")
    void deveAtualizarRecepcionistaComSucesso() {
        Recepcionista atualizado = new Recepcionista();
        atualizado.setNome("Carla Atualizada");
        atualizado.setEmail("carla.atualizada@sorrisus.com");
        atualizado.setTurno("TARDE");

        when(recepcionistaRepository.findById(1L)).thenReturn(Optional.of(recepcionista));
        when(recepcionistaRepository.save(any(Recepcionista.class))).thenAnswer(inv -> inv.getArgument(0));

        Recepcionista resultado = recepcionistaService.atualizar(1L, atualizado);

        assertThat(resultado.getNome()).isEqualTo("Carla Atualizada");
        assertThat(resultado.getTurno()).isEqualTo("TARDE");
        verify(recepcionistaRepository, times(1)).findById(1L);
        verify(recepcionistaRepository, times(1)).save(any(Recepcionista.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao atualizar recepcionista com turno inválido")
    void deveLancarExcecaoAoAtualizarComTurnoInvalido() {
        Recepcionista atualizado = new Recepcionista();
        atualizado.setNome("Carla Atualizada");
        atualizado.setTurno("MEIO_TURNO");

        when(recepcionistaRepository.findById(1L)).thenReturn(Optional.of(recepcionista));

        assertThatThrownBy(() -> recepcionistaService.atualizar(1L, atualizado))
                .isInstanceOf(InvalidFieldException.class)
                .hasMessageContaining("Turno inválido")
                .hasMessageContaining("MANHA, TARDE, NOITE");

        verify(recepcionistaRepository, times(1)).findById(1L);
        verify(recepcionistaRepository, never()).save(any(Recepcionista.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar atualizar recepcionista inexistente")
    void deveLancarExcecaoAoAtualizarInexistente() {
        Recepcionista atualizado = new Recepcionista();
        atualizado.setNome("Nome Qualquer");
        atualizado.setTurno("MANHA");

        when(recepcionistaRepository.findById(2L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> recepcionistaService.atualizar(2L, atualizado))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Recepcionista com id 2 não encontrado");

        verify(recepcionistaRepository, times(1)).findById(2L);
        verify(recepcionistaRepository, never()).save(any(Recepcionista.class));
    }

    @Test
    @DisplayName("Deve deletar recepcionista com sucesso")
    void deveDeletarRecepcionista() {
        when(recepcionistaRepository.findById(1L)).thenReturn(Optional.of(recepcionista));
        doNothing().when(recepcionistaRepository).delete(recepcionista);

        recepcionistaService.deletar(1L);

        verify(recepcionistaRepository, times(1)).findById(1L);
        verify(recepcionistaRepository, times(1)).delete(recepcionista);
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar deletar recepcionista inexistente")
    void deveLancarExcecaoAoDeletarInexistente() {
        when(recepcionistaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> recepcionistaService.deletar(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Recepcionista com id 99 não encontrado");

        verify(recepcionistaRepository, never()).delete(any(Recepcionista.class));
    }
}