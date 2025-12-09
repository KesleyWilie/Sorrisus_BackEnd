package com.ifpb.sorrisus.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "anamneses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Anamnese {

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

    @OneToOne
    @JoinColumn(name = "paciente_id")
    private Paciente paciente;
}