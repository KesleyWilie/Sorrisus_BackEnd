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

    @Column(length = 4000)
    private String observacoes;

    @OneToOne(mappedBy = "prontuario")
    private Consulta consulta;
}
