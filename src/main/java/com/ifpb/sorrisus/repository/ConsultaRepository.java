package com.ifpb.sorrisus.repository;

import com.ifpb.sorrisus.model.Consulta;
import com.ifpb.sorrisus.model.Dentista;
import com.ifpb.sorrisus.model.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface ConsultaRepository extends JpaRepository<Consulta, Long> {

    List<Consulta> findByDentistaAndDataHoraBetween(Dentista dentista, LocalDateTime start, LocalDateTime end);

    boolean existsByDentistaAndDataHora(Dentista dentista, LocalDateTime dataHora);

    boolean existsByDentistaAndDataHoraAndIdNot(Dentista dentista, LocalDateTime dataHora, Long id);

    List<Consulta> findByPaciente(Paciente paciente);
}
