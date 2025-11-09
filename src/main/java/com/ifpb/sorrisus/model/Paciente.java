package com.ifpb.sorrisus.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "pacientes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Paciente extends Usuario {

    @Column(nullable = false, unique = true)
    private String cpf;

    @Column(nullable = false)
    private String telefone;

    private LocalDate dataNascimento;

    public Paciente(String nome, String email, String senha, Role role, String cpf, String telefone, LocalDate dataNascimento) {
        super(null, nome, email, senha, role);
        this.cpf = cpf;
        this.telefone = telefone;
        this.dataNascimento = dataNascimento;
    }
}
