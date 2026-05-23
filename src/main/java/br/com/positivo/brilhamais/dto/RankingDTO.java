package br.com.positivo.brilhamais.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RankingDTO {
    private Integer posicao;
    private String tecnico;
    private BigDecimal notaFinal;
    private BigDecimal percentualSla;
    private Integer pontosSla;
    private Boolean elegivel;
    private String motivoInelegibilidade;
    private LocalDate mesReferencia;
}
