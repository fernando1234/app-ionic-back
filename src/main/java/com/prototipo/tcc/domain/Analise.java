package com.prototipo.tcc.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

@Entity
public class Analise implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private BigDecimal ph;
    private BigDecimal turbidez;
    private BigDecimal condutividade;
    private BigDecimal temperatura;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private Date dataLeitura;

    private BigDecimal phP;
    private BigDecimal phN;
    private BigDecimal cloro;
    private BigDecimal decantador;
    private BigDecimal sanilidade;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private Date dataTratamento;

    @ManyToOne
    @JoinColumn(name = "i_usuarios")
    private Usuario usuario;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getPh() {
        return ph;
    }

    public void setPh(BigDecimal ph) {
        this.ph = ph;
    }

    public BigDecimal getTurbidez() {
        return turbidez;
    }

    public void setTurbidez(BigDecimal turbidez) {
        this.turbidez = turbidez;
    }

    public BigDecimal getCondutividade() {
        return condutividade;
    }

    public void setCondutividade(BigDecimal condutividade) {
        this.condutividade = condutividade;
    }

    public BigDecimal getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(BigDecimal temperatura) {
        this.temperatura = temperatura;
    }

    public Date getDataLeitura() {
        return dataLeitura;
    }

    public void setDataLeitura(Date dataLeitura) {
        this.dataLeitura = dataLeitura;
    }

    public BigDecimal getPhP() {
        return phP;
    }

    public void setPhP(BigDecimal phP) {
        this.phP = phP;
    }

    public BigDecimal getPhN() {
        return phN;
    }

    public void setPhN(BigDecimal phN) {
        this.phN = phN;
    }

    public BigDecimal getCloro() {
        return cloro;
    }

    public void setCloro(BigDecimal cloro) {
        this.cloro = cloro;
    }

    public BigDecimal getDecantador() {
        return decantador;
    }

    public void setDecantador(BigDecimal decantador) {
        this.decantador = decantador;
    }

    public BigDecimal getSanilidade() {
        return sanilidade;
    }

    public void setSanilidade(BigDecimal sanilidade) {
        this.sanilidade = sanilidade;
    }

    public Date getDataTratamento() {
        return dataTratamento;
    }

    public void setDataTratamento(Date dataTratamento) {
        this.dataTratamento = dataTratamento;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Analise analise = (Analise) o;
        return Objects.equals(id, analise.id) &&
                Objects.equals(ph, analise.ph) &&
                Objects.equals(turbidez, analise.turbidez) &&
                Objects.equals(condutividade, analise.condutividade) &&
                Objects.equals(temperatura, analise.temperatura) &&
                Objects.equals(dataLeitura, analise.dataLeitura) &&
                Objects.equals(phP, analise.phP) &&
                Objects.equals(phN, analise.phN) &&
                Objects.equals(cloro, analise.cloro) &&
                Objects.equals(decantador, analise.decantador) &&
                Objects.equals(sanilidade, analise.sanilidade) &&
                Objects.equals(dataTratamento, analise.dataTratamento) &&
                Objects.equals(usuario, analise.usuario);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ph, turbidez, condutividade, temperatura, dataLeitura, phP, phN, cloro, decantador, sanilidade, dataTratamento, usuario);
    }
}
