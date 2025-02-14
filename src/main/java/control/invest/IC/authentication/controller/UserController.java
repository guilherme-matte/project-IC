package control.invest.IC.authentication.controller;

import control.invest.IC.authentication.dto.PasswordResetDTO;
import control.invest.IC.authentication.dto.UserLogin;
import control.invest.IC.authentication.model.UserModel;
import control.invest.IC.authentication.repositories.UserRepository;
import control.invest.IC.authentication.service.ApiResponseDTO;
import control.invest.IC.authentication.service.EmailService;
import control.invest.IC.authentication.service.SenhaService;
import control.invest.IC.authentication.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;
import java.util.Optional;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private SenhaService senhaService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmailService emailService;

    private void redefinirSenhaTemporaria(UserModel userModel) {

        userModel.setSenhaTemporaria(null);
        userModel.setSenhaTemporariaBoolean(false);
        userRepository.save(userModel);

    }

    @PostMapping("/auth/cad/user")
    public ResponseEntity<String> cadUsuario(@RequestBody UserModel request) {
        String resposta = userService.cadUsuario(request);
        if (resposta.equals("Usuário cadastrado com sucesso")) {
            return ResponseEntity.status(HttpStatus.OK).body(resposta);
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body(resposta);
    }

    @PostMapping("/auth/login")
    public ResponseEntity<String> login(@RequestBody UserLogin userLogin) {
        Optional<UserModel> usuario = userRepository.findByEmail(userLogin.getEmail());
        if (usuario.isPresent() && usuario.get().isSenhaTemporariaBoolean() && senhaService.verificarSenha(userLogin.getSenha(), usuario.get().getSenhaTemporaria())) {

            redefinirSenhaTemporaria(usuario.get());

            return ResponseEntity.ok("Login realizado com sucesso");
        }
        if (usuario.isPresent() && senhaService.verificarSenha(userLogin.getSenha(), usuario.get().getSenha())) {

            if (usuario.get().isSenhaTemporariaBoolean()) {

                redefinirSenhaTemporaria(usuario.get());

            }
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Login realizado com sucesso!");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Email ou senha incorretos!");
        }
    }

    public void sendEmailNovaSenhaCriada(String email) {
        Optional<UserModel> userModel = userRepository.findByEmail(email);
        if (userModel.isPresent()) {
            UserModel usuario = userModel.get();
            String text = "Olá " + usuario.getNome() + " sua senha foi alterada com sucesso!\n\n MENSAGEM PARA FINS DE TESTE!";
            emailService.sendEmail(email, "Nova senha criada", text);
        }


    }

    @PostMapping("/alterar-senha/{email}")
    public ResponseEntity<ApiResponseDTO> alterarSenha(@RequestBody PasswordResetDTO password, @PathVariable String email) {
        Optional<UserModel> usuario = userRepository.findByEmail(email);

        if (usuario.isEmpty()) {
            ApiResponseDTO response = new ApiResponseDTO(null, "Usuário não encontrado", 404);
            return ResponseEntity.status(404).body(response);
        }

        ;
        if (senhaService.verificarSenha(password.getSenhaAtual(), usuario.get().getSenha())) {

            if (!Objects.equals(password.getSenhaNova(), password.getSenhaNovaConfirmacao())) {
                ApiResponseDTO response = new ApiResponseDTO(null, "Os campos da nova senha não são iguais!", 400);
                return ResponseEntity.status(400).body(response);
            }

            if (Objects.equals(password.getSenhaNova(), password.getSenhaNovaConfirmacao())) {
                usuario.get().setSenha(senhaService.hashSenha(password.getSenhaNova()));

                userRepository.save(usuario.get());

                sendEmailNovaSenhaCriada(email);

                ApiResponseDTO response = new ApiResponseDTO(null, "Senha alterada com sucesso", 200);
                return ResponseEntity.status(200).body(response);
            }
        }

        ApiResponseDTO response = new ApiResponseDTO(null, "Senha atual não confere", 401);

        return ResponseEntity.status(401).body(response);
    }


}
