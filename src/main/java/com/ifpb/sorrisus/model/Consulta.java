package com.ifpb.sorrisus.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "consultas", indexes = {
        @Index(name = "idx_consulta_dentista_datahora", columnList = "dentista_id, data_hora"),
        @Index(name = "idx_consulta_paciente_datahora", columnList = "paciente_id, data_hora")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Consulta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "data_hora", nullable = false)
    private LocalDateTime dataHora;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusConsulta status = StatusConsulta.PENDENTE;

    @ManyToOne
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;

    @ManyToOne
    @JoinColumn(name = "dentista_id", nullable = false)
    private Dentista dentista;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "prontuario_id")
    private Prontuario prontuario;

    @Column(length = 1000)
    private String observacao;
}
