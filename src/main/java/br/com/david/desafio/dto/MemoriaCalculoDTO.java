package br.com.david.desafio.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.math.BigDecimal;

/**
 * DTO para representar a memória de cálculo de cada mês.
 */
@Schema(description = "Detalhamento da evolução financeira em um mês específico")
public class MemoriaCalculoDTO {

    @JsonProperty("mes")
    @Schema(description = "Número do mês", example = "1")
    private Integer mes;

    @JsonProperty("saldoInicial")
    @Schema(description = "Saldo devedor no início do mês", example = "1000.00")
    private BigDecimal saldoInicial;

    @JsonProperty("juro")
    @Schema(description = "Valor dos juros aplicados no mês", example = "15.00")
    private BigDecimal juro;

    @JsonProperty("saldoFinal")
    @Schema(description = "Saldo devedor no final do mês", example = "1015.00")
    private BigDecimal saldoFinal;

    public MemoriaCalculoDTO() {
    }

    public MemoriaCalculoDTO(Integer mes, BigDecimal saldoInicial, BigDecimal juro, BigDecimal saldoFinal) {
        this.mes = mes;
        this.saldoInicial = saldoInicial;
        this.juro = juro;
        this.saldoFinal = saldoFinal;
    }

    // Getters and Setters
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
    public String toString() {
        return "MemoriaCalculoDTO{" +
                "mes=" + mes +
                ", saldoInicial=" + saldoInicial +
                ", juro=" + juro +
                ", saldoFinal=" + saldoFinal +
                '}';
    }
}
