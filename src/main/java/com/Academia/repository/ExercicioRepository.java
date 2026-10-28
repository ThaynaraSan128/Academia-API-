package com.Academia.repository;
import com.Academia.model.Exercicio;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ExercicioRepository extends JpaRepository<Exercicio,Long> { boolean existsByNomeIgnoreCase(String nome); }
