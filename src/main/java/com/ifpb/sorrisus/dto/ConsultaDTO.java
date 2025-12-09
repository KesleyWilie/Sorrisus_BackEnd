package com.ifpb.sorrisus.dto;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class ConsultaDTO {
    private Long id;

    @NotNull
    private LocalDateTime dataHora;

    @NotNull
    private Long pacienteId;
    private String nomePaciente;

    @NotNull
    private Long dentistaId;
    private String nomeDentista;

    private String status;
    private String observacao;
    private ProntuarioDTO prontuario;
}