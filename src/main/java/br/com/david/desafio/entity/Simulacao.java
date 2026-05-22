package br.com.david.desafio.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Entidade que representa uma simulação de financiamento.
 * Armazena os parâmetros de entrada e os resultados calculados.
 */
@Entity
@Table(name = "simulacao")
public class Simulacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "valor_inicial", nullable = false, precision = 19, scale = 2)
    private BigDecimal valorInicial;

    @Column(name = "taxa_juros_mensal", nullable = false, precision = 10, scale = 8)
    private BigDecimal taxaJurosMensal;

    @Column(name = "prazo_meses", nullable = false)
    private Integer prazoMeses;

    @Column(name = "valor_total_final", nullable = false, precision = 19, scale = 2)
    private BigDecimal valorTotalFinal;

    @Column(name = "valor_total_juros", nullable = false, precision = 19, scale = 2)
    private BigDecimal valorTotalJuros;

    @Column(name = "data_criacao", nullable = false)
    private LocalDateTime dataCriacao;

    @OneToMany(mappedBy = "simulacao", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @OrderBy("mes ASC")
    private List<MemoriaCalculo> memoriaCalculos = new ArrayList<>();

    public Simulacao() {
        this.dataCriacao = LocalDateTime.now();
    }

    public Simulacao(BigDecimal valorInicial, BigDecimal taxaJurosMensal, Integer prazoMeses) {
        this();
        this.valorInicial = valorInicial;
        this.taxaJurosMensal = taxaJurosMensal;
        this.prazoMeses = prazoMeses;
    }

    // Métodos auxiliares
    public void adicionarMemoriaCalculo(MemoriaCalculo memoria) {
        memoriaCalculos.add(memoria);
        memoria.setSimulacao(this);
    }

    public void calcularTotais() {
        if (!memoriaCalculos.isEmpty()) {
            MemoriaCalculo ultimaMemoria = memoriaCalculos.get(memoriaCalculos.size() - 1);
            this.valorTotalFinal = ultimaMemoria.getSaldoFinal();
            this.valorTotalJuros = this.valorTotalFinal.subtract(this.valorInicial);
        }
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getValorInicial() {
        return valorInicial;
    }

    public void setValorInicial(BigDecimal valorInicial) {
        this.valorInicial = valorInicial;
    }

    public BigDecimal getTaxaJurosMensal() {
        return taxaJurosMensal;
    }

    public void setTaxaJurosMensal(BigDecimal taxaJurosMensal) {
        this.taxaJurosMensal = taxaJurosMensal;
    }

    public Integer getPrazoMeses() {
        return prazoMeses;
    }

    public void setPrazoMeses(Integer prazoMeses) {
        this.prazoMeses = prazoMeses;
    }

    public BigDecimal getValorTotalFinal() {
        return valorTotalFinal;
    }

    public void setValorTotalFinal(BigDecimal valorTotalFinal) {
        this.valorTotalFinal = valorTotalFinal;
    }

    public BigDecimal getValorTotalJuros() {
        return valorTotalJuros;
    }

    public void setValorTotalJuros(BigDecimal valorTotalJuros) {
        this.valorTotalJuros = valorTotalJuros;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public List<MemoriaCalculo> getMemoriaCalculos() {
        return memoriaCalculos;
    }

    public void setMemoriaCalculos(List<MemoriaCalculo> memoriaCalculos) {
        this.memoriaCalculos = memoriaCalculos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Simulacao simulacao = (Simulacao) o;
        return Objects.equals(id, simulacao.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Simulacao{" +
                "id=" + id +
                ", valorInicial=" + valorInicial +
                ", taxaJurosMensal=" + taxaJurosMensal +
                ", prazoMeses=" + prazoMeses +
                ", valorTotalFinal=" + valorTotalFinal +
                ", valorTotalJuros=" + valorTotalJuros +
                '}';
    }
}
