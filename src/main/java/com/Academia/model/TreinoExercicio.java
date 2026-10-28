package com.Academia.model;

import jakarta.persistence.*;

@Entity
@Table(name = "treino_exercicios")
public class TreinoExercicio {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne(optional = false) private Treino treino;
    @ManyToOne(optional = false) private Exercicio exercicio;
    @Column(nullable = false) private Integer series;
    @Column(nullable = false) private Integer repeticoes;
    private Double cargaKg;
    private Integer descansoSegundos;
    protected TreinoExercicio() {}
    public TreinoExercicio(Exercicio exercicio,Integer series,Integer repeticoes,Double cargaKg,Integer descansoSegundos){this.exercicio=exercicio;this.series=series;this.repeticoes=repeticoes;this.cargaKg=cargaKg;this.descansoSegundos=descansoSegundos;}
    void associar(Treino treino){this.treino=treino;}
    public Long getId(){return id;} public Exercicio getExercicio(){return exercicio;} public Integer getSeries(){return series;}
    public Integer getRepeticoes(){return repeticoes;} public Double getCargaKg(){return cargaKg;} public Integer getDescansoSegundos(){return descansoSegundos;}
}
