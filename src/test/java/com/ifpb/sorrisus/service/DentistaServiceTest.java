package com.ifpb.sorrisus.service;

import com.ifpb.sorrisus.model.Dentista;
import com.ifpb.sorrisus.repository.DentistaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
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
}
