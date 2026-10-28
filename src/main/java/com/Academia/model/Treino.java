package com.Academia.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "treinos")
public class Treino {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable = false) private String nome;
    @Column(length = 1000) private String observacoes;
    @Column(nullable = false) private LocalDate dataCriacao;
    @ManyToOne(optional = false) private Aluno aluno;
    @ManyToOne(optional = false) private Professor professor;
    @OneToMany(mappedBy = "treino", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id ASC") private List<TreinoExercicio> exercicios = new ArrayList<>();
    protected Treino() {}
    public Treino(String nome,String observacoes,LocalDate dataCriacao,Aluno aluno,Professor professor){this.nome=nome;this.observacoes=observacoes;this.dataCriacao=dataCriacao;this.aluno=aluno;this.professor=professor;}
    public void adicionarExercicio(TreinoExercicio item){item.associar(this);exercicios.add(item);}
    public Long getId(){return id;} public String getNome(){return nome;} public String getObservacoes(){return observacoes;}
    public LocalDate getDataCriacao(){return dataCriacao;} public Aluno getAluno(){return aluno;} public Professor getProfessor(){return professor;}
    public List<TreinoExercicio> getExercicios(){return exercicios;}
}
