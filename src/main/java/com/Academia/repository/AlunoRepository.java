package com.Academia.repository;
import com.Academia.model.Aluno;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;
public interface AlunoRepository extends JpaRepository<Aluno,Long> {
    boolean existsByEmailIgnoreCase(String email); boolean existsByCpf(String cpf);
    List<Aluno> findByFimMatriculaBefore(LocalDate data);
    List<Aluno> findByInicioMatriculaLessThanEqualAndFimMatriculaGreaterThanEqual(LocalDate inicio,LocalDate fim);
}
