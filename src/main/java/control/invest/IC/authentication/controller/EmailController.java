package control.invest.IC.authentication.controller;

import control.invest.IC.authentication.dto.EmailRequest;
import control.invest.IC.authentication.model.UserModel;
import control.invest.IC.authentication.repositories.UserRepository;
import control.invest.IC.authentication.service.EmailService;
import control.invest.IC.authentication.service.SenhaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/email")
public class EmailController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmailService emailService;

    @PostMapping("/reset-password")
    public ResponseEntity<String> sendEmail(@RequestBody EmailRequest request) {
        Optional<UserModel> result = userRepository.findByEmail(request.getTo());

        if (result.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("sem cadastro para o email: " + request.getTo());
        }

        UserModel usuario = result.get();

        String senhaTemporaria = SenhaService.gerarSenha();
        System.out.println("Senha temporaria: " + senhaTemporaria);

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        usuario.setSenhaTemporaria(passwordEncoder.encode(senhaTemporaria));
        usuario.setSenhaTemporariaBoolean(true);
        userRepository.save(usuario);


        String text;
        text = "Olá " + usuario.getNome() + ", recebemos uma redefinição de senha para o seu usuario no IC: " + senhaTemporaria;

        emailService.sendEmail(request.getTo(), "Redefinição de senha", text);
        return ResponseEntity.status(HttpStatus.OK).body("Email enviado com sucesso");
    }
}
