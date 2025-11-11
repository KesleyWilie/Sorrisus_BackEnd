package com.ifpb.sorrisus.service;

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
        dentista.setCro("CRO-12345");
        dentista.setEspecialidade("Ortodontia");
    }

    @Test
    @DisplayName("Deve salvar dentista com sucesso")
    void deveSalvarDentista() {
        when(dentistaRepository.save(any(Dentista.class))).thenReturn(dentista);

        Dentista salvo = dentistaService.salvar(dentista);

        assertThat(salvo).isNotNull();
        assertThat(salvo.getCro()).isEqualTo("CRO-12345");
        verify(dentistaRepository, times(1)).save(dentista);
    }

    @Test
    @DisplayName("Deve buscar dentista por ID")
    void deveBuscarDentistaPorId() {
        when(dentistaRepository.findById(1L)).thenReturn(Optional.of(dentista));

        Optional<Dentista> resultado = dentistaService.buscarPorId(1L);

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Deve listar todos os dentistas")
    void deveListarTodosDentistas() {
        when(dentistaRepository.findAll()).thenReturn(Arrays.asList(dentista));

        List<Dentista> lista = dentistaService.listarTodos();

        assertThat(lista).hasSize(1);
        assertThat(lista.get(0).getNome()).isEqualTo("Dra. Ana Costa");
        verify(dentistaRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve retornar Optional vazio ao buscar dentista inexistente")
    void deveRetornarOptionalVazioAoBuscarInexistente() {
        when(dentistaRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Dentista> resultado = dentistaService.buscarPorId(99L);

        assertThat(resultado).isEmpty();
        verify(dentistaRepository, times(1)).findById(99L);
    }

    @Test
    @DisplayName("Deve atualizar dentista com sucesso")
    void deveAtualizarDentistaComSucesso() {
        Dentista atualizado = new Dentista();
        atualizado.setNome("Dra. Ana Atualizada");
        atualizado.setEmail("ana.atualizada@sorrisus.com");
        atualizado.setCro("CRO-54321");
        atualizado.setEspecialidade("Endodontia");

        when(dentistaRepository.findById(1L)).thenReturn(Optional.of(dentista));
        when(dentistaRepository.save(any(Dentista.class))).thenAnswer(inv -> inv.getArgument(0));

        Dentista resultado = dentistaService.atualizar(1L, atualizado);

        assertThat(resultado.getNome()).isEqualTo("Dra. Ana Atualizada");
        assertThat(resultado.getCro()).isEqualTo("CRO-54321");
        assertThat(resultado.getEspecialidade()).isEqualTo("Endodontia");
        verify(dentistaRepository, times(1)).findById(1L);
        verify(dentistaRepository, times(1)).save(any(Dentista.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar atualizar dentista inexistente")
    void deveLancarExcecaoAoAtualizarInexistente() {
        Dentista atualizado = new Dentista();
        atualizado.setNome("Nome Qualquer");

        when(dentistaRepository.findById(2L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> dentistaService.atualizar(2L, atualizado))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Dentista não encontrado");

        verify(dentistaRepository, times(1)).findById(2L);
        verify(dentistaRepository, never()).save(any(Dentista.class));
    }

    @Test
    @DisplayName("Deve deletar dentista com sucesso")
    void deveDeletarDentista() {
        doNothing().when(dentistaRepository).deleteById(1L);

        dentistaService.deletar(1L);

        verify(dentistaRepository, times(1)).deleteById(1L);
    }
}
