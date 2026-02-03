package com.ifpb.sorrisus.repository;

import com.ifpb.sorrisus.model.Dentista;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DentistaRepository extends JpaRepository<Dentista, Long> {
    boolean existsByCro(String cro);
    boolean existsByEmail(String email);
 }
