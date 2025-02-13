package control.invest.IC.authentication.controller;

import control.invest.IC.authentication.dto.UserLogin;
import control.invest.IC.authentication.model.UserModel;
import control.invest.IC.authentication.repositories.UserRepository;
import control.invest.IC.authentication.service.SenhaService;
import control.invest.IC.authentication.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private SenhaService senhaService;
    @Autowired
    private UserRepository userRepository;

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
}
