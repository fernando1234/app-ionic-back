package com.prototipo.tcc.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
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
    private LocalDateTime dataLeitura;

    private BigDecimal phP;
    private BigDecimal phN;
    private BigDecimal cloro;
    private BigDecimal decantador;
    private BigDecimal alcalinidade;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime dataTratamento;

    private BigDecimal phNovo;
    private BigDecimal turbidezNovo;
    private BigDecimal condutividadeNovo;
    private BigDecimal temperaturaNovo;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime dataLeituraNovo;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
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

    public LocalDateTime getDataLeitura() {
        return dataLeitura;
    }

    public void setDataLeitura(LocalDateTime dataLeitura) {
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

    public BigDecimal getAlcalinidade() {
        return alcalinidade;
    }

    public void setAlcalinidade(BigDecimal alcalinidade) {
        this.alcalinidade = alcalinidade;
    }

    public LocalDateTime getDataTratamento() {
        return dataTratamento;
    }

    public void setDataTratamento(LocalDateTime dataTratamento) {
        this.dataTratamento = dataTratamento;
    }

    public BigDecimal getPhNovo() {
        return phNovo;
    }

    public void setPhNovo(BigDecimal phNovo) {
        this.phNovo = phNovo;
    }

    public BigDecimal getTurbidezNovo() {
        return turbidezNovo;
    }

    public void setTurbidezNovo(BigDecimal turbidezNovo) {
        this.turbidezNovo = turbidezNovo;
    }

    public BigDecimal getCondutividadeNovo() {
        return condutividadeNovo;
    }

    public void setCondutividadeNovo(BigDecimal condutividadeNovo) {
        this.condutividadeNovo = condutividadeNovo;
    }

    public BigDecimal getTemperaturaNovo() {
        return temperaturaNovo;
    }

    public void setTemperaturaNovo(BigDecimal temperaturaNovo) {
        this.temperaturaNovo = temperaturaNovo;
    }

    public LocalDateTime getDataLeituraNovo() {
        return dataLeituraNovo;
    }

    public void setDataLeituraNovo(LocalDateTime dataLeituraNovo) {
        this.dataLeituraNovo = dataLeituraNovo;
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
        return Objects.equals(id, analise.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "id=" + id +
                ", ph=" + ph +
                ", turbidez=" + turbidez +
                ", condutividade=" + condutividade +
                ", temperatura=" + temperatura +
                ", dataLeitura=" + dataLeitura +
                ", phP=" + phP +
                ", phN=" + phN +
                ", cloro=" + cloro +
                ", decantador=" + decantador +
                ", alcalinidade=" + alcalinidade +
                ", dataTratamento=" + dataTratamento +
                ", phNovo=" + phNovo +
                ", turbidezNovo=" + turbidezNovo +
                ", condutividadeNovo=" + condutividadeNovo +
                ", temperaturaNovo=" + temperaturaNovo +
                ", dataLeituraNovo=" + dataLeituraNovo +
                ", usuario=" + usuario;
    }
}