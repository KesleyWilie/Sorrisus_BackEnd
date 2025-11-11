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

import static org.assertj.core.api.Assertions.assertThat;
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
    @DisplayName("Deve deletar paciente com sucesso")
    void deveDeletarPaciente() {
        doNothing().when(pacienteRepository).deleteById(1L);
        pacienteService.deletar(1L);
        verify(pacienteRepository, times(1)).deleteById(1L);
    }
}
