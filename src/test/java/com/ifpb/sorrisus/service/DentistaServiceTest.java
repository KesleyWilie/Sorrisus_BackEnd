package com.ifpb.sorrisus.service;

import com.ifpb.sorrisus.exception.BusinessException;
import com.ifpb.sorrisus.exception.InvalidCROException;
import com.ifpb.sorrisus.exception.InvalidFieldException;
import com.ifpb.sorrisus.exception.ResourceNotFoundException;
import com.ifpb.sorrisus.model.Dentista;
import com.ifpb.sorrisus.repository.DentistaRepository;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("Teste Unitário - DentistaService")
class DentistaServiceTest {

    @Mock
    private DentistaRepository dentistaRepository;

    @InjectMocks
    private DentistaService dentistaService;

    private Dentista dentista;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        dentista = new Dentista();
        dentista.setId(1L);
        dentista.setNome("Dra. Ana Costa");
        dentista.setEmail("ana@sorrisus.com");
        dentista.setCro("12345-PB");
        dentista.setEspecialidade("Ortodontia");
    }

    @Test
    @DisplayName("Deve salvar dentista com sucesso")
    void deveSalvarDentista() {
        when(dentistaRepository.existsByCro("12345-PB")).thenReturn(false);
        when(dentistaRepository.save(any(Dentista.class))).thenReturn(dentista);

        Dentista salvo = dentistaService.salvar(dentista);

        assertThat(salvo).isNotNull();
        assertThat(salvo.getCro()).isEqualTo("12345-PB");
        assertThat(salvo.getEspecialidade()).isEqualTo("Ortodontia");
        verify(dentistaRepository, times(1)).save(dentista);
    }

    @Test
    @DisplayName("Deve lançar exceção ao salvar dentista com CRO nulo")
    void deveLancarExcecaoAoSalvarComCroNulo() {
        dentista.setCro(null);

        assertThatThrownBy(() -> dentistaService.salvar(dentista))
                .isInstanceOf(InvalidFieldException.class)
                .hasMessageContaining("O CRO é obrigatório");

        verify(dentistaRepository, never()).save(any(Dentista.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao salvar dentista com CRO em formato inválido")
    void deveLancarExcecaoAoSalvarComCroInvalido() {
        dentista.setCro("INVALIDO");

        assertThatThrownBy(() -> dentistaService.salvar(dentista))
                .isInstanceOf(InvalidCROException.class)
                .hasMessageContaining("Formato inválido de CRO")
                .hasMessageContaining("12345-PB");

        verify(dentistaRepository, never()).save(any(Dentista.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao salvar dentista com CRO já existente")
    void deveLancarExcecaoAoSalvarComCroJaExistente() {
        when(dentistaRepository.existsByCro("12345-PB")).thenReturn(true);

        assertThatThrownBy(() -> dentistaService.salvar(dentista))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("12345-PB")
                .hasMessageContaining("já está cadastrado");

        verify(dentistaRepository, never()).save(any(Dentista.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao salvar dentista com especialidade nula")
    void deveLancarExcecaoAoSalvarComEspecialidadeNula() {
        dentista.setEspecialidade(null);
        when(dentistaRepository.existsByCro("12345-PB")).thenReturn(false);

        assertThatThrownBy(() -> dentistaService.salvar(dentista))
                .isInstanceOf(InvalidFieldException.class)
                .hasMessageContaining("A especialidade é obrigatória");

        verify(dentistaRepository, never()).save(any(Dentista.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao salvar dentista com especialidade em branco")
    void deveLancarExcecaoAoSalvarComEspecialidadeEmBranco() {
        dentista.setEspecialidade("   ");
        when(dentistaRepository.existsByCro("12345-PB")).thenReturn(false);

        assertThatThrownBy(() -> dentistaService.salvar(dentista))
                .isInstanceOf(InvalidFieldException.class)
                .hasMessageContaining("A especialidade é obrigatória");

        verify(dentistaRepository, never()).save(any(Dentista.class));
    }

    @Test
    @DisplayName("Deve buscar dentista por ID com sucesso")
    void deveBuscarDentistaPorId() {
        when(dentistaRepository.findById(1L)).thenReturn(Optional.of(dentista));

        Dentista resultado = dentistaService.buscarPorId(1L);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getNome()).isEqualTo("Dra. Ana Costa");
        verify(dentistaRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar dentista inexistente")
    void deveLancarExcecaoAoBuscarInexistente() {
        when(dentistaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> dentistaService.buscarPorId(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Dentista com id 99 não encontrado");

        verify(dentistaRepository, times(1)).findById(99L);
    }

    @Test
    @DisplayName("Deve listar todos os dentistas")
    void deveListarTodosDentistas() {
        when(dentistaRepository.findAll()).thenReturn(Arrays.asList(dentista));

        List<Dentista> lista = dentistaService.listarTodos();

        assertThat(lista).hasSize(1);
        assertThat(lista.get(0).getNome()).isEqualTo("Dra. Ana Costa");
        assertThat(lista.get(0).getCro()).isEqualTo("12345-PB");
        verify(dentistaRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve atualizar dentista com sucesso")
    void deveAtualizarDentistaComSucesso() {
        Dentista atualizado = new Dentista();
        atualizado.setNome("Dra. Ana Atualizada");
        atualizado.setEmail("ana.atualizada@sorrisus.com");
        atualizado.setCro("54321-PB");
        atualizado.setEspecialidade("Endodontia");

        when(dentistaRepository.findById(1L)).thenReturn(Optional.of(dentista));
        when(dentistaRepository.existsByCro("54321-PB")).thenReturn(false);
        when(dentistaRepository.save(any(Dentista.class))).thenAnswer(inv -> inv.getArgument(0));

        Dentista resultado = dentistaService.atualizar(1L, atualizado);

        assertThat(resultado.getNome()).isEqualTo("Dra. Ana Atualizada");
        assertThat(resultado.getCro()).isEqualTo("54321-PB");
        assertThat(resultado.getEspecialidade()).isEqualTo("Endodontia");
        verify(dentistaRepository, times(1)).findById(1L);
        verify(dentistaRepository, times(1)).save(any(Dentista.class));
    }

    @Test
    @DisplayName("Deve manter mesmo CRO ao atualizar dentista")
    void deveManterMesmoCroAoAtualizar() {
        Dentista atualizado = new Dentista();
        atualizado.setNome("Dra. Ana Atualizada");
        atualizado.setCro("12345-PB");
        atualizado.setEspecialidade("Periodontia");

        when(dentistaRepository.findById(1L)).thenReturn(Optional.of(dentista));
        when(dentistaRepository.save(any(Dentista.class))).thenAnswer(inv -> inv.getArgument(0));

        Dentista resultado = dentistaService.atualizar(1L, atualizado);

        assertThat(resultado.getCro()).isEqualTo("12345-PB");
        verify(dentistaRepository, times(1)).findById(1L);
        verify(dentistaRepository, times(1)).save(any(Dentista.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao atualizar dentista com CRO já existente")
    void deveLancarExcecaoAoAtualizarComCroJaExistente() {
        Dentista atualizado = new Dentista();
        atualizado.setNome("Dra. Ana Atualizada");
        atualizado.setCro("99999-SP");
        atualizado.setEspecialidade("Endodontia");

        when(dentistaRepository.findById(1L)).thenReturn(Optional.of(dentista));
        when(dentistaRepository.existsByCro("99999-SP")).thenReturn(true);

        assertThatThrownBy(() -> dentistaService.atualizar(1L, atualizado))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("99999-SP")
                .hasMessageContaining("já está cadastrado");

        verify(dentistaRepository, never()).save(any(Dentista.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao atualizar dentista com CRO em formato inválido")
    void deveLancarExcecaoAoAtualizarComCroInvalido() {
        Dentista atualizado = new Dentista();
        atualizado.setNome("Dra. Ana Atualizada");
        atualizado.setCro("INVALIDO");
        atualizado.setEspecialidade("Endodontia");

        when(dentistaRepository.findById(1L)).thenReturn(Optional.of(dentista));

        assertThatThrownBy(() -> dentistaService.atualizar(1L, atualizado))
                .isInstanceOf(InvalidCROException.class)
                .hasMessageContaining("Formato inválido de CRO");

        verify(dentistaRepository, never()).save(any(Dentista.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar atualizar dentista inexistente")
    void deveLancarExcecaoAoAtualizarInexistente() {
        Dentista atualizado = new Dentista();
        atualizado.setNome("Nome Qualquer");
        atualizado.setCro("12345-PB");
        atualizado.setEspecialidade("Ortodontia");

        when(dentistaRepository.findById(2L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> dentistaService.atualizar(2L, atualizado))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Dentista com id 2 não encontrado");

        verify(dentistaRepository, times(1)).findById(2L);
        verify(dentistaRepository, never()).save(any(Dentista.class));
    }

    @Test
    @DisplayName("Deve deletar dentista com sucesso")
    void deveDeletarDentista() {
        when(dentistaRepository.findById(1L)).thenReturn(Optional.of(dentista));
        doNothing().when(dentistaRepository).delete(dentista);

        dentistaService.deletar(1L);

        verify(dentistaRepository, times(1)).findById(1L);
        verify(dentistaRepository, times(1)).delete(dentista);
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar deletar dentista inexistente")
    void deveLancarExcecaoAoDeletarInexistente() {
        when(dentistaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> dentistaService.deletar(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Dentista com id 99 não encontrado");

        verify(dentistaRepository, never()).delete(any(Dentista.class));
    }
}