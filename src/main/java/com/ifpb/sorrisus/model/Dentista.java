package com.ifpb.sorrisus.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "dentistas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Dentista extends Usuario {

    @Column(nullable = false)
    private String cro;

    @Column(nullable = false)
    private String especialidade;

    public Dentista(String nome, String email, String senha, Role role, String cro, String especialidade) {
        super(null, nome, email, senha, role);
        this.cro = cro;
        this.especialidade = especialidade;
    }
}
