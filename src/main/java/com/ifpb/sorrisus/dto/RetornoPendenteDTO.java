package com.ifpb.sorrisus.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RetornoPendenteDTO {
    private Long pacienteId;
    private String pacienteNome;
    private String pacienteEmail;
    private String pacienteTelefone;
    private LocalDateTime dataUltimaConsulta;
    private Long diasDesdeUltimaConsulta;
    private Long dentistaId;
    private String dentistaNome;
    private String prioridade;
}