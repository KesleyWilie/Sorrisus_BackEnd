package com.ifpb.sorrisus.repository;

import com.ifpb.sorrisus.model.Anamnese;
import com.ifpb.sorrisus.model.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AnamneseRepository extends JpaRepository<Anamnese, Long> {
    Optional<Anamnese> findByPaciente(Paciente paciente);
}