package com.Academia.model;

import jakarta.persistence.*;

@Entity
@Table(name = "exercicios")
public class Exercicio {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable = false, unique = true) private String nome;
    private String grupoMuscular;
    @Column(length = 1000) private String descricao;
    protected Exercicio() {}
    public Exercicio(String nome,String grupoMuscular,String descricao){this.nome=nome;this.grupoMuscular=grupoMuscular;this.descricao=descricao;}
    public Long getId(){return id;} public String getNome(){return nome;} public String getGrupoMuscular(){return grupoMuscular;} public String getDescricao(){return descricao;}
}
