package br.com.david.desafio.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * Entidade que representa a memória de cálculo detalhada de cada mês.
 * Armazena a evolução mês a mês do saldo e dos juros aplicados.
 */
@Entity
@Table(name = "memoria_calculo")
public class MemoriaCalculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "simulacao_id", nullable = false)
    private Simulacao simulacao;

    @Column(name = "mes", nullable = false)
    private Integer mes;

    @Column(name = "saldo_inicial", nullable = false, precision = 19, scale = 2)
    private BigDecimal saldoInicial;

    @Column(name = "juro", nullable = false, precision = 19, scale = 2)
    private BigDecimal juro;

    @Column(name = "saldo_final", nullable = false, precision = 19, scale = 2)
    private BigDecimal saldoFinal;

    public MemoriaCalculo() {
    }

    public MemoriaCalculo(Integer mes, BigDecimal saldoInicial, BigDecimal juro, BigDecimal saldoFinal) {
        this.mes = mes;
        this.saldoInicial = saldoInicial;
        this.juro = juro;
        this.saldoFinal = saldoFinal;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Simulacao getSimulacao() {
        return simulacao;
    }

    public void setSimulacao(Simulacao simulacao) {
        this.simulacao = simulacao;
    }

    public Integer getMes() {
        return mes;
    }

    public void setMes(Integer mes) {
        this.mes = mes;
    }

    public BigDecimal getSaldoInicial() {
        return saldoInicial;
    }

    public void setSaldoInicial(BigDecimal saldoInicial) {
        this.saldoInicial = saldoInicial;
    }

    public BigDecimal getJuro() {
        return juro;
    }

    public void setJuro(BigDecimal juro) {
        this.juro = juro;
    }

    public BigDecimal getSaldoFinal() {
        return saldoFinal;
    }

    public void setSaldoFinal(BigDecimal saldoFinal) {
        this.saldoFinal = saldoFinal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MemoriaCalculo that = (MemoriaCalculo) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "MemoriaCalculo{" +
                "id=" + id +
                ", mes=" + mes +
                ", saldoInicial=" + saldoInicial +
                ", juro=" + juro +
                ", saldoFinal=" + saldoFinal +
                '}';
    }
}
