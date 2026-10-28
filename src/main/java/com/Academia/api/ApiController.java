package com.Academia.api;

import com.Academia.model.*;
import com.Academia.repository.*;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

import static java.time.temporal.ChronoUnit.DAYS;

@RestController
@RequestMapping("/api")
public class ApiController {
    private final AlunoRepository alunos; private final ProfessorRepository professores;
    private final ExercicioRepository exercicios; private final TreinoRepository treinos;
    public ApiController(AlunoRepository alunos,ProfessorRepository professores,ExercicioRepository exercicios,TreinoRepository treinos){
        this.alunos=alunos;this.professores=professores;this.exercicios=exercicios;this.treinos=treinos;
    }

    public record AlunoResponse(Long id,String nome,String email,String cpf,LocalDate dataNascimento,LocalDate inicioMatricula,LocalDate fimMatricula,boolean matriculaAtiva) {}
    public record ProfessorResponse(Long id,String nome,String email,String especialidade) {}
    public record ExercicioResponse(Long id,String nome,String grupoMuscular,String descricao) {}
    public record ItemResponse(Long id,ExercicioResponse exercicio,Integer series,Integer repeticoes,Double cargaKg,Integer descansoSegundos) {}
    public record TreinoResponse(Long id,String nome,String observacoes,LocalDate dataCriacao,Long alunoId,String alunoNome,Long professorId,String professorNome,List<ItemResponse> exercicios) {}
    public record MatriculaResponse(Long alunoId,LocalDate inicio,LocalDate fim,boolean ativa,Long diasRestantes) {}

    @PostMapping("/alunos") @ResponseStatus(HttpStatus.CREATED)
    public AlunoResponse criarAluno(@Valid @RequestBody Requests.AlunoRequest r){
        if(r.fimMatricula().isBefore(r.inicioMatricula())) throw bad("fimMatricula deve ser igual ou posterior a inicioMatricula");
        if(alunos.existsByEmailIgnoreCase(r.email())) throw conflict("Já existe aluno com este e-mail");
        if(alunos.existsByCpf(r.cpf())) throw conflict("Já existe aluno com este CPF");
        return aluno(alunos.save(new Aluno(r.nome(),r.email(),r.cpf(),r.dataNascimento(),r.inicioMatricula(),r.fimMatricula())));
    }
    @GetMapping("/alunos") public List<AlunoResponse> listarAlunos(){return alunos.findAll().stream().map(this::aluno).toList();}
    @GetMapping("/alunos/{id}") public AlunoResponse buscarAluno(@PathVariable Long id){return aluno(alunoOuFalha(id));}
    @GetMapping("/alunos/matriculas/ativas") public List<AlunoResponse> matriculasAtivas(){return alunos.findByInicioMatriculaLessThanEqualAndFimMatriculaGreaterThanEqual(LocalDate.now(),LocalDate.now()).stream().map(this::aluno).toList();}
    @GetMapping("/alunos/matriculas/vencidas") public List<AlunoResponse> matriculasVencidas(){return alunos.findByFimMatriculaBefore(LocalDate.now()).stream().map(this::aluno).toList();}

    @PostMapping("/professores") @ResponseStatus(HttpStatus.CREATED)
    public ProfessorResponse criarProfessor(@Valid @RequestBody Requests.ProfessorRequest r){
        if(professores.existsByEmailIgnoreCase(r.email())) throw conflict("Já existe professor com este e-mail");
        return professor(professores.save(new Professor(r.nome(),r.email(),r.especialidade())));
    }
    @GetMapping("/professores") public List<ProfessorResponse> listarProfessores(){return professores.findAll().stream().map(this::professor).toList();}
    @GetMapping("/professores/{id}") public ProfessorResponse buscarProfessor(@PathVariable Long id){return professor(professorOuFalha(id));}

    @PostMapping("/exercicios") @ResponseStatus(HttpStatus.CREATED)
    public ExercicioResponse criarExercicio(@Valid @RequestBody Requests.ExercicioRequest r){
        if(exercicios.existsByNomeIgnoreCase(r.nome())) throw conflict("Já existe exercício com este nome");
        return exercicio(exercicios.save(new Exercicio(r.nome(),r.grupoMuscular(),r.descricao())));
    }
    @GetMapping("/exercicios") public List<ExercicioResponse> listarExercicios(){return exercicios.findAll().stream().map(this::exercicio).toList();}
    @GetMapping("/exercicios/{id}") public ExercicioResponse buscarExercicio(@PathVariable Long id){return exercicio(exercicioOuFalha(id));}

    @PostMapping("/treinos") @ResponseStatus(HttpStatus.CREATED)
    public TreinoResponse criarTreino(@Valid @RequestBody Requests.TreinoRequest r){
        Aluno aluno=alunoOuFalha(r.alunoId()); Professor professor=professorOuFalha(r.professorId());
        LocalDate data=r.dataCriacao()==null?LocalDate.now():r.dataCriacao();
        if(!aluno.matriculaAtiva(data)) throw bad("A matrícula do aluno não está ativa na data do treino");
        if(data.isAfter(LocalDate.now())) throw bad("dataCriacao não pode ser futura");
        Treino treino=new Treino(r.nome(),r.observacoes(),data,aluno,professor);
        for(Requests.ItemRequest item:r.exercicios()) treino.adicionarExercicio(new TreinoExercicio(exercicioOuFalha(item.exercicioId()),item.series(),item.repeticoes(),item.cargaKg(),item.descansoSegundos()));
        return treino(treinos.save(treino));
    }
    @GetMapping("/treinos/{id}") public TreinoResponse buscarTreino(@PathVariable Long id){return treino(treinoOuFalha(id));}
    @GetMapping("/alunos/{alunoId}/treinos") public List<TreinoResponse> historico(@PathVariable Long alunoId,
            @RequestParam(required=false) @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam(required=false) @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate fim){
        alunoOuFalha(alunoId);
        if((inicio==null)!=(fim==null)) throw bad("Informe inicio e fim juntos para filtrar o período");
        if(inicio!=null && fim.isBefore(inicio)) throw bad("fim deve ser igual ou posterior a inicio");
        var lista=inicio==null?treinos.findByAlunoIdOrderByDataCriacaoDesc(alunoId):treinos.findByAlunoIdAndDataCriacaoBetweenOrderByDataCriacaoDesc(alunoId,inicio,fim);
        return lista.stream().map(this::treino).toList();
    }
    @GetMapping("/alunos/{alunoId}/matricula") public MatriculaResponse validadeMatricula(@PathVariable Long alunoId){
        Aluno a=alunoOuFalha(alunoId); LocalDate hoje=LocalDate.now();
        boolean ativa=a.matriculaAtiva(hoje);
        return new MatriculaResponse(a.getId(),a.getInicioMatricula(),a.getFimMatricula(),ativa,ativa?DAYS.between(hoje,a.getFimMatricula()):0L);
    }

    private Aluno alunoOuFalha(Long id){return alunos.findById(id).orElseThrow(()->notFound("Aluno não encontrado: "+id));}
    private Professor professorOuFalha(Long id){return professores.findById(id).orElseThrow(()->notFound("Professor não encontrado: "+id));}
    private Exercicio exercicioOuFalha(Long id){return exercicios.findById(id).orElseThrow(()->notFound("Exercício não encontrado: "+id));}
    private Treino treinoOuFalha(Long id){return treinos.findById(id).orElseThrow(()->notFound("Treino não encontrado: "+id));}
    private AlunoResponse aluno(Aluno a){return new AlunoResponse(a.getId(),a.getNome(),a.getEmail(),a.getCpf(),a.getDataNascimento(),a.getInicioMatricula(),a.getFimMatricula(),a.matriculaAtiva(LocalDate.now()));}
    private ProfessorResponse professor(Professor p){return new ProfessorResponse(p.getId(),p.getNome(),p.getEmail(),p.getEspecialidade());}
    private ExercicioResponse exercicio(Exercicio e){return new ExercicioResponse(e.getId(),e.getNome(),e.getGrupoMuscular(),e.getDescricao());}
    private TreinoResponse treino(Treino t){return new TreinoResponse(t.getId(),t.getNome(),t.getObservacoes(),t.getDataCriacao(),t.getAluno().getId(),t.getAluno().getNome(),t.getProfessor().getId(),t.getProfessor().getNome(),t.getExercicios().stream().map(i->new ItemResponse(i.getId(),exercicio(i.getExercicio()),i.getSeries(),i.getRepeticoes(),i.getCargaKg(),i.getDescansoSegundos())).toList());}
    private ApiException notFound(String m){return new ApiException(HttpStatus.NOT_FOUND,m);} private ApiException bad(String m){return new ApiException(HttpStatus.BAD_REQUEST,m);} private ApiException conflict(String m){return new ApiException(HttpStatus.CONFLICT,m);}
}
