package com.ifpb.sorrisus.service;

import com.ifpb.sorrisus.model.Paciente;
import com.ifpb.sorrisus.repository.PacienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("Teste Unitário - PacienteService")
class PacienteServiceTest {

    @Mock
    private PacienteRepository pacienteRepository;

    @InjectMocks
    private PacienteService pacienteService;

    private Paciente paciente;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        paciente = new Paciente();
        paciente.setId(1L);
        paciente.setNome("João Silva");
        paciente.setEmail("joao@sorrisus.com");
        paciente.setTelefone("83999999999");
        paciente.setCpf("12345678900");
        paciente.setDataNascimento(LocalDate.parse("1990-01-01"));
    }

    @Test
    @DisplayName("Deve salvar paciente com sucesso")
    void deveSalvarPaciente() {
        when(pacienteRepository.save(any(Paciente.class))).thenReturn(paciente);

        Paciente salvo = pacienteService.salvar(paciente);

        assertThat(salvo).isNotNull();
        assertThat(salvo.getNome()).isEqualTo("João Silva");
        verify(pacienteRepository, times(1)).save(paciente);
    }

    @Test
    @DisplayName("Deve listar todos os pacientes")
    void deveListarPacientes() {
        when(pacienteRepository.findAll()).thenReturn(Arrays.asList(paciente));

        List<Paciente> lista = pacienteService.listarTodos();

        assertThat(lista).hasSize(1);
        assertThat(lista.get(0).getEmail()).isEqualTo("joao@sorrisus.com");
    }

    @Test
    @DisplayName("Deve buscar paciente por ID com sucesso")
    void deveBuscarPacientePorId() {
        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));

        Optional<Paciente> resultado = pacienteService.buscarPorId(1L);

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getCpf()).isEqualTo("12345678900");
        verify(pacienteRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Deve retornar Optional vazio ao buscar paciente inexistente")
    void deveRetornarOptionalVazioAoBuscarInexistente() {
        when(pacienteRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Paciente> resultado = pacienteService.buscarPorId(99L);

        assertThat(resultado).isEmpty();
        verify(pacienteRepository, times(1)).findById(99L);
    }

    @Test
    @DisplayName("Deve atualizar paciente com sucesso")
    void deveAtualizarPacienteComSucesso() {
        Paciente atualizado = new Paciente();
        atualizado.setNome("João Atualizado");
        atualizado.setEmail("joao.atualizado@sorrisus.com");
        atualizado.setCpf("99988877766");
        atualizado.setTelefone("83888888888");
        atualizado.setDataNascimento(LocalDate.parse("1991-02-02"));

        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));
        when(pacienteRepository.save(any(Paciente.class))).thenAnswer(inv -> inv.getArgument(0));

        Paciente resultado = pacienteService.atualizar(1L, atualizado);

        assertThat(resultado.getNome()).isEqualTo("João Atualizado");
        assertThat(resultado.getCpf()).isEqualTo("99988877766");
        assertThat(resultado.getTelefone()).isEqualTo("83888888888");
        assertThat(resultado.getDataNascimento()).isEqualTo(LocalDate.parse("1991-02-02"));
        verify(pacienteRepository, times(1)).findById(1L);
        verify(pacienteRepository, times(1)).save(any(Paciente.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar atualizar paciente inexistente")
    void deveLancarExcecaoAoAtualizarInexistente() {
        Paciente atualizado = new Paciente();
        atualizado.setNome("Nome Qualquer");

        when(pacienteRepository.findById(2L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> pacienteService.atualizar(2L, atualizado))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Paciente não encontrado");

        verify(pacienteRepository, times(1)).findById(2L);
        verify(pacienteRepository, never()).save(any(Paciente.class));
    }

    @Test
    @DisplayName("Deve deletar paciente com sucesso")
    void deveDeletarPaciente() {
        doNothing().when(pacienteRepository).deleteById(1L);
        pacienteService.deletar(1L);
        verify(pacienteRepository, times(1)).deleteById(1L);
    }
}