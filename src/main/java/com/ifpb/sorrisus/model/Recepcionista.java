package com.ifpb.sorrisus.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "recepcionistas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Recepcionista extends Usuario {

    @Column(nullable = false)
    private String turno;

    public Recepcionista(String nome, String email, String senha, Role role, String turno) {
        super(null, nome, email, senha, role);
        this.turno = turno;
    }
}
