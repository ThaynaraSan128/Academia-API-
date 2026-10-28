package com.Academia.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "alunos")
public class Aluno {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false) private String nome;
    @Column(nullable = false, unique = true) private String email;
    @Column(nullable = false, unique = true) private String cpf;
    private LocalDate dataNascimento;
    @Column(nullable = false) private LocalDate inicioMatricula;
    @Column(nullable = false) private LocalDate fimMatricula;

    protected Aluno() {}
    public Aluno(String nome, String email, String cpf, LocalDate dataNascimento, LocalDate inicioMatricula, LocalDate fimMatricula) {
        this.nome=nome; this.email=email; this.cpf=cpf; this.dataNascimento=dataNascimento;
        this.inicioMatricula=inicioMatricula; this.fimMatricula=fimMatricula;
    }
    public Long getId(){return id;} public String getNome(){return nome;} public String getEmail(){return email;}
    public String getCpf(){return cpf;} public LocalDate getDataNascimento(){return dataNascimento;}
    public LocalDate getInicioMatricula(){return inicioMatricula;} public LocalDate getFimMatricula(){return fimMatricula;}
    public boolean matriculaAtiva(LocalDate data){return !data.isBefore(inicioMatricula) && !data.isAfter(fimMatricula);}
}
