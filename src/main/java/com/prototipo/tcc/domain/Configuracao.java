package com.prototipo.tcc.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.prototipo.tcc.domain.enums.PeriodoRepeticao;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalTime;
import java.util.Objects;

@Entity
public class Configuracao implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message="Preenchimento obrigatório")
//    @Size(max=80, message="O tamanho deve ter no máximo {max} caracteres")
    private Integer capacidadeLitros;

    @NotNull(message="Preenchimento obrigatório")
    private PeriodoRepeticao periodoRepeticao;

    @NotNull(message="Preenchimento obrigatório")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime horarioPrevisto;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCapacidadeLitros() {
        return capacidadeLitros;
    }

    public void setCapacidadeLitros(Integer capacidadeLitros) {
        this.capacidadeLitros = capacidadeLitros;
    }

    public PeriodoRepeticao getPeriodoRepeticao() {
        return periodoRepeticao;
    }

    public void setPeriodoRepeticao(PeriodoRepeticao periodoRepeticao) {
        this.periodoRepeticao = periodoRepeticao;
    }

    public LocalTime getHorarioPrevisto() {
        return horarioPrevisto;
    }

    public void setHorarioPrevisto(LocalTime horarioPrevisto) {
        this.horarioPrevisto = horarioPrevisto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Configuracao that = (Configuracao) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
