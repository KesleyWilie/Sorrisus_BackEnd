package com.ifpb.sorrisus.service;

import com.ifpb.sorrisus.dto.ProntuarioDTO;
import com.ifpb.sorrisus.model.Anamnese;
import com.ifpb.sorrisus.model.Paciente;
import com.ifpb.sorrisus.repository.AnamneseRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AnamneseServiceTest {

    @InjectMocks private AnamneseService service;
    @Mock private AnamneseRepository repository;

    @Test
    @DisplayName("Deve retornar nova anamnese se não encontrar no banco")
    void deveRetornarNovaSeVazio() {
        Paciente p = new Paciente();
        when(repository.findByPaciente(p)).thenReturn(Optional.empty());

        Anamnese a = service.buscarPorPaciente(p);
        assertNotNull(a);
        assertNull(a.getId()); 
    }

    @Test
    @DisplayName("Deve salvar dados corretamente")
    void deveSalvarDados() {
        Paciente p = new Paciente();
        ProntuarioDTO dto = new ProntuarioDTO();
        dto.setAlergiaResposta("Sim");

        when(repository.findByPaciente(p)).thenReturn(Optional.empty());
        when(repository.save(any())).thenAnswer(i -> i.getArgument(0));

        Anamnese salva = service.salvarOuAtualizar(p, dto);
        
        assertEquals("Sim", salva.getAlergiaResposta());
        assertEquals(p, salva.getPaciente());
    }
}