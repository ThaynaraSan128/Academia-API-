package com.Academia.repository;
import com.Academia.model.Professor;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ProfessorRepository extends JpaRepository<Professor,Long> { boolean existsByEmailIgnoreCase(String email); }
