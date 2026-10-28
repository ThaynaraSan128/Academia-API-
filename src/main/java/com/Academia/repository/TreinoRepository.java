package com.Academia.repository;
import com.Academia.model.Treino;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;
public interface TreinoRepository extends JpaRepository<Treino,Long> {
    List<Treino> findByAlunoIdOrderByDataCriacaoDesc(Long alunoId);
    List<Treino> findByAlunoIdAndDataCriacaoBetweenOrderByDataCriacaoDesc(Long alunoId,LocalDate inicio,LocalDate fim);
}
