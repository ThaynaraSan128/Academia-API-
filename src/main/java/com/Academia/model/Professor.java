package com.Academia.model;

import jakarta.persistence.*;

@Entity
@Table(name = "professores")
public class Professor {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable = false) private String nome;
    @Column(nullable = false, unique = true) private String email;
    private String especialidade;
    protected Professor() {}
    public Professor(String nome,String email,String especialidade){this.nome=nome;this.email=email;this.especialidade=especialidade;}
    public Long getId(){return id;} public String getNome(){return nome;} public String getEmail(){return email;} public String getEspecialidade(){return especialidade;}
}
