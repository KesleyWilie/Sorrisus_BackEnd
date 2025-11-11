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

import static org.assertj.core.api.Assertions.assertThat;
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
    }
}
