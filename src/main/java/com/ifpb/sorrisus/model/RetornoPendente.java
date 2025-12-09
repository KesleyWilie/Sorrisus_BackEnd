package com.ifpb.sorrisus.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RetornoPendente {

    private Paciente paciente;
    private Consulta ultimaConsulta;
    private LocalDateTime dataUltimaConsulta;
    private int diasDesdeUltimaConsulta;
    private String prioridade;
    private String observacao;

    public RetornoPendente(Paciente paciente, Consulta ultimaConsulta) {
        this.paciente = paciente;
        this.ultimaConsulta = ultimaConsulta;
        this.dataUltimaConsulta = ultimaConsulta.getDataHora();
    }
}