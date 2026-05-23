package br.com.positivo.brilhamais.controllers;

import br.com.positivo.brilhamais.dto.RankingDTO;
import br.com.positivo.brilhamais.services.DashboardService;
import br.com.positivo.brilhamais.services.MotorCalculoService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;
    private final MotorCalculoService motorCalculoService;

    @GetMapping("/ranking")
    public ResponseEntity<List<RankingDTO>> getRanking(
            @RequestParam(name = "mesAno", required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate mesAno
    ) {
        if (mesAno == null) {
            mesAno = LocalDate.now().withDayOfMonth(1);
        }
        return ResponseEntity.ok(dashboardService.getRankingMensal(mesAno));
    }

    @PostMapping("/calcular")
    public ResponseEntity<String> forceCalculate(
            @RequestParam(name = "mesAno") 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate mesAno
    ) {
        motorCalculoService.calcularEProcessarMes(mesAno);
        return ResponseEntity.ok("Cálculo e consolidação concluídos com sucesso para: " + mesAno);
    }
}
