package com.Academia.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.List;

public final class Requests {
    private Requests() {}
    public record AlunoRequest(@NotBlank String nome,@NotBlank @Email String email,@NotBlank String cpf,
            LocalDate dataNascimento,@NotNull LocalDate inicioMatricula,@NotNull LocalDate fimMatricula) {}
    public record ProfessorRequest(@NotBlank String nome,@NotBlank @Email String email,String especialidade) {}
    public record ExercicioRequest(@NotBlank String nome,@NotBlank String grupoMuscular,String descricao) {}
    public record ItemRequest(@NotNull Long exercicioId,@NotNull @Min(1) Integer series,@NotNull @Min(1) Integer repeticoes,
            @PositiveOrZero Double cargaKg,@PositiveOrZero Integer descansoSegundos) {}
    public record TreinoRequest(@NotBlank String nome,String observacoes,@NotNull Long alunoId,@NotNull Long professorId,
            LocalDate dataCriacao,@NotEmpty List<@Valid ItemRequest> exercicios) {}
}
