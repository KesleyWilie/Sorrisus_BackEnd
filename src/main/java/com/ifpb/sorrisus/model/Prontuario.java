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

    @Column(columnDefinition = "TEXT")
    private String observacoes;

    @Column(columnDefinition = "TEXT")
    private String planoTratamento;

    @Column(columnDefinition = "TEXT")
    private String odontogramaJson;

    @OneToOne
    @JoinColumn(name = "consulta_id")
    private Consulta consulta;
}