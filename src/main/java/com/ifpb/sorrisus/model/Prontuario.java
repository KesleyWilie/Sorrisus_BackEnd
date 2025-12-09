package com.ifpb.sorrisus.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "prontuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Prontuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String alergiaResposta;
    @Column(columnDefinition = "TEXT")
    private String alergiaNotas;

    private String antibioticoResposta;
    @Column(columnDefinition = "TEXT")
    private String antibioticoNotas;

    private String anestesicoResposta;
    @Column(columnDefinition = "TEXT")
    private String anestesicoNotas;

    private String sensibilidadeResposta;
    @Column(columnDefinition = "TEXT")
    private String sensibilidadeNotas;

    private String pressaoResposta;
    @Column(columnDefinition = "TEXT")
    private String pressaoNotas;

    private String medicamentoResposta;
    @Column(columnDefinition = "TEXT")
    private String medicamentoNotas;

    private String problemaSaudeResposta;
    @Column(columnDefinition = "TEXT")
    private String problemaSaudeNotas;

    @Column(columnDefinition = "TEXT")
    private String observacoes;

    @Column(columnDefinition = "TEXT")
    private String planoTratamento;

    // odontograma salvo em JSON
    @Column(columnDefinition = "TEXT")
    private String odontogramaJson;

    @OneToOne(mappedBy = "prontuario")
    private Consulta consulta;
}
