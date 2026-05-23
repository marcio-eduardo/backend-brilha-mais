package br.com.positivo.brilhamais.services;

import br.com.positivo.brilhamais.models.ApuracaoMensal;
import br.com.positivo.brilhamais.models.Tecnico;
import br.com.positivo.brilhamais.repositories.ApuracaoMensalRepository;
import br.com.positivo.brilhamais.repositories.TecnicoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MotorCalculoService {

    private final JdbcTemplate jdbcTemplate;
    private final TecnicoRepository tecnicoRepository;
    private final ApuracaoMensalRepository apuracaoRepository;

    @Transactional
    public void calcularEProcessarMes(LocalDate mesReferencia) {
        // Exemplo Simplificado: O motor executa as queries/views e persiste o resultado na tabela final.
        // Em um cenário real, você buscaria os dados agregados das views: vw_sla_por_tecnico, etc.
        
        List<Tecnico> tecnicos = tecnicoRepository.findAll();
        
        for (Tecnico tecnico : tecnicos) {
            if (!tecnico.getAtivo()) continue;
            
            // Busca dados da view vw_sla_por_tecnico
            String sqlSla = "SELECT \"% SLA Atingido\" FROM vw_sla_por_tecnico WHERE \"Técnico\" = ?";
            BigDecimal percentualSla = BigDecimal.ZERO;
            try {
                percentualSla = jdbcTemplate.queryForObject(sqlSla, BigDecimal.class, tecnico.getNomeCompleto());
            } catch (Exception e) {
                // Técnico sem chamados no período
            }

            // Normalizando para 0 a 1 (Ex: 95.00 -> 0.95)
            if (percentualSla != null && percentualSla.compareTo(BigDecimal.ZERO) > 0) {
                percentualSla = percentualSla.divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP);
            }

            // A lógica completa buscaria pontosSla das regras, NPS, Reincidências...
            // Este é o esqueleto do motor de consolidação que roda todo dia 1º.
            
            ApuracaoMensal apuracao = apuracaoRepository
                    .findByTecnicoIdTecnicoAndMesAno(tecnico.getIdTecnico(), mesReferencia)
                    .orElse(ApuracaoMensal.builder()
                            .tecnico(tecnico)
                            .mesAno(mesReferencia)
                            .build());

            apuracao.setAtingimentoSla(percentualSla);
            // apuracao.setPontosSla(... consulta tb_faixa_pontuacao ...)
            apuracao.setDataCalculo(LocalDateTime.now());
            
            apuracaoRepository.save(apuracao);
        }
    }
}
