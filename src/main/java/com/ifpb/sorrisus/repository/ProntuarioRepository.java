package com.ifpb.sorrisus.repository;

import com.ifpb.sorrisus.model.Prontuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProntuarioRepository extends JpaRepository<Prontuario, Long> {
}