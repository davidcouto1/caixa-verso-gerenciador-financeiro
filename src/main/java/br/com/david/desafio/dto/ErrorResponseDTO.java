package br.com.david.desafio.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DTO para padronizar as respostas de erro da API.
 */
@Schema(description = "Estrutura de resposta de erro")
public class ErrorResponseDTO {

    @JsonProperty("timestamp")
    @Schema(description = "Data e hora do erro")
    private LocalDateTime timestamp;

    @JsonProperty("status")
    @Schema(description = "Código HTTP do erro", example = "400")
    private Integer status;

    @JsonProperty("error")
    @Schema(description = "Descrição do erro", example = "Bad Request")
    private String error;

    @JsonProperty("message")
    @Schema(description = "Mensagem de erro", example = "Validation failed")
    private String message;

    @JsonProperty("path")
    @Schema(description = "Caminho da requisição", example = "/api/simulacoes")
    private String path;

    @JsonProperty("errors")
    @Schema(description = "Lista de erros de validação")
    private List<String> errors;

    public ErrorResponseDTO() {
        this.timestamp = LocalDateTime.now();
        this.errors = new ArrayList<>();
    }

    public ErrorResponseDTO(Integer status, String error, String message, String path) {
        this();
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }

    public void addError(String error) {
        this.errors.add(error);
    }

    // Getters and Setters
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
}
