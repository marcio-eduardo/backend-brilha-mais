package br.com.positivo.brilhamais.repositories;

import br.com.positivo.brilhamais.models.ApuracaoMensal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ApuracaoMensalRepository extends JpaRepository<ApuracaoMensal, Integer> {
    
    Optional<ApuracaoMensal> findByTecnicoIdTecnicoAndMesAno(Integer idTecnico, LocalDate mesAno);
    
    @Query("SELECT a FROM ApuracaoMensal a JOIN FETCH a.tecnico t WHERE a.mesAno = :mesAno ORDER BY a.pontuacaoTotal DESC")
    List<ApuracaoMensal> findRankingByMesAno(LocalDate mesAno);
}
