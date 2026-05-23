package br.com.positivo.brilhamais.services;

import br.com.positivo.brilhamais.dto.RankingDTO;
import br.com.positivo.brilhamais.models.ApuracaoMensal;
import br.com.positivo.brilhamais.repositories.ApuracaoMensalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final ApuracaoMensalRepository apuracaoRepository;

    public List<RankingDTO> getRankingMensal(LocalDate mesAno) {
        List<ApuracaoMensal> apuracoes = apuracaoRepository.findRankingByMesAno(mesAno);
        List<RankingDTO> ranking = new ArrayList<>();
        
        int posicao = 1;
        for (ApuracaoMensal apuracao : apuracoes) {
            ranking.add(RankingDTO.builder()
                    .posicao(posicao++)
                    .tecnico(apuracao.getTecnico().getNomeCompleto())
                    .notaFinal(apuracao.getPontuacaoTotal())
                    .percentualSla(apuracao.getAtingimentoSla().multiply(new BigDecimal("100")))
                    .pontosSla(apuracao.getPontosSla())
                    .elegivel(apuracao.getStatusElegibilidade())
                    .motivoInelegibilidade(apuracao.getMotivoInelegibilidade())
                    .mesReferencia(apuracao.getMesAno())
                    .build());
        }
        
        return ranking;
    }
}
