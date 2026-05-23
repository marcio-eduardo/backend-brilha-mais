package br.com.positivo.brilhamais.config;

import br.com.positivo.brilhamais.models.Tecnico;
import br.com.positivo.brilhamais.repositories.TecnicoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final TecnicoRepository tecnicoRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // Criar usuário master '72916' caso não exista
        if (tecnicoRepository.findByMatriculaCpf("72916").isEmpty()) {
            Tecnico admin = Tecnico.builder()
                    .matriculaCpf("72916")
                    .senha(passwordEncoder.encode("admin"))
                    .nomeCompleto("Administrador Master")
                    .cargo("Administrador")
                    .ativo(true)
                    .isPrimeiroAcesso(false) // Admin já nasce ativo sem precisar resetar senha inicial
                    .build();
            
            tecnicoRepository.save(admin);
            System.out.println("✅ Usuário Master criado com sucesso! [Login: 72916 | Senha: admin]");
        }
    }
}
