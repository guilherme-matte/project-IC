package control.invest.IC.authentication.controller;

import control.invest.IC.authentication.dto.PasswordResetDTO;
import control.invest.IC.authentication.dto.UserDTO;
import control.invest.IC.authentication.dto.UserLogin;
import control.invest.IC.authentication.model.UserModel;
import control.invest.IC.authentication.repositories.UserRepository;
import control.invest.IC.authentication.service.ApiResponseDTO;
import control.invest.IC.authentication.service.EmailService;
import control.invest.IC.authentication.service.SenhaService;
import control.invest.IC.authentication.service.UserService;
import control.invest.IC.irfp.models.ContribuinteModel;
import control.invest.IC.irfp.repositories.ContribuinteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
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
    public ResponseEntity<ApiResponseDTO> cadUsuario(@RequestBody UserModel request) {
        String resposta = userService.cadUsuario(request);
        if (resposta.equals("Usuário cadastrado com sucesso")) {
            ApiResponseDTO response = new ApiResponseDTO(null, resposta, 200);
            return ResponseEntity.status(200).body(response);
        }
        ApiResponseDTO response = new ApiResponseDTO(null, resposta, 409);
        return ResponseEntity.status(409).body(response);
    }

    @PostMapping("/auth/login")
    public ResponseEntity<ApiResponseDTO> login(@RequestBody UserLogin userLogin) {
        Optional<UserModel> usuario = userRepository.findByEmail(userLogin.getEmail());
        if (usuario.isPresent() && usuario.get().isSenhaTemporariaBoolean() && senhaService.verificarSenha(userLogin.getSenha(), usuario.get().getSenhaTemporaria())) {

            ApiResponseDTO response = new ApiResponseDTO(null, "Login realizado com sucesso\ncrie uma nova senha!", 200);

            return ResponseEntity.status(200).body(response);
        }
        if (usuario.isPresent() && senhaService.verificarSenha(userLogin.getSenha(), usuario.get().getSenha())) {

            if (usuario.get().isSenhaTemporariaBoolean()) {

                redefinirSenhaTemporaria(usuario.get());

            }
            ApiResponseDTO response = new ApiResponseDTO(null, "login realizado com sucesso!", 200);
            return ResponseEntity.status(200).body(response);
        } else {
            ApiResponseDTO response = new ApiResponseDTO(null, "Senha ou email incorretos", 401);
            return ResponseEntity.status(401).body(response);
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


        if (senhaService.verificarSenha(password.getSenhaAtual(), usuario.get().getSenha()) || senhaService.verificarSenha(password.getSenhaAtual(), usuario.get().getSenhaTemporaria())) {

            if (!Objects.equals(password.getSenhaNova(), password.getSenhaNovaConfirmacao())) {
                ApiResponseDTO response = new ApiResponseDTO(null, "Os campos da nova senha não são iguais!", 400);
                return ResponseEntity.status(400).body(response);
            }

            if (Objects.equals(password.getSenhaNova(), password.getSenhaNovaConfirmacao())) {
                usuario.get().setSenha(senhaService.hashSenha(password.getSenhaNova()));

                userRepository.save(usuario.get());

                sendEmailNovaSenhaCriada(email);

                if (usuario.get().isSenhaTemporariaBoolean()) {
                    redefinirSenhaTemporaria(usuario.get());
                }

                ApiResponseDTO response = new ApiResponseDTO(null, "Senha alterada com sucesso", 200);
                return ResponseEntity.status(200).body(response);
            }
        }

        ApiResponseDTO response = new ApiResponseDTO(null, "Senha atual não confere", 401);

        return ResponseEntity.status(401).body(response);
    }

    @GetMapping("/get/user/{cpf}")
    public ResponseEntity<ApiResponseDTO> getUser(@PathVariable String cpf) {
        Optional<UserModel> userModel = userRepository.findByCpf(cpf);

        if (userModel.isEmpty()) {
            ApiResponseDTO response = new ApiResponseDTO(null, "Usuário não encontrado", 404);
            return ResponseEntity.status(404).body(response);
        }

        UserModel usuario = userModel.get();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        LinkedHashMap user = new LinkedHashMap();
        user.put("id", usuario.getId());
        user.put("nome", usuario.getNome());
        user.put("sobrenome", usuario.getSobrenome());
        user.put("email", usuario.getEmail());
        user.put("celular", usuario.getCelular());
        user.put("nascimento", usuario.getDataNascimento().format(formatter));
        user.put("cpf", usuario.getCpf());

        ApiResponseDTO response = new ApiResponseDTO(user, "Usuário encontrado com sucesso", 200);
        return ResponseEntity.status(200).body(response);
    }

    @PutMapping("/update/user/{cpf}")
    public ResponseEntity<ApiResponseDTO> updUser(@PathVariable String cpf, @RequestBody UserDTO userDTO) {
        Optional<UserModel> usuario = userRepository.findByCpf(cpf);

        if (usuario.isEmpty()) {
            ApiResponseDTO response = new ApiResponseDTO(null, "Usuário não encontrado", 404);
            return ResponseEntity.status(404).body(response);
        }
        Optional<UserModel> verEmail = userRepository.findByEmail(userDTO.getEmail());
        if (verEmail.isPresent()) {
            ApiResponseDTO response = new ApiResponseDTO(null, "Conta já cadastrada para o email informado", 409);
            return ResponseEntity.status(409).body(response);
        }

        UserModel userExistente = usuario.get();

        userExistente.setNome(userDTO.getNome());
        userExistente.setSobrenome(userDTO.getSobrenome());
        userExistente.setCelular(userDTO.getCelular());
        userExistente.setEmail(userDTO.getEmail());
        userExistente.setDataNascimento(userDTO.getDataNasc());
        userRepository.save(userExistente);

        ApiResponseDTO response = new ApiResponseDTO(userExistente, "Usuario alterado com sucesso!", 200);
        return ResponseEntity.status(200).body(response);
    }

    @Autowired
    ContribuinteRepository contribuinteRepository;

    @DeleteMapping("/delete/user/{email}")
    public ResponseEntity<ApiResponseDTO> deleteUser(@PathVariable String email) {
        Optional<UserModel> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            ApiResponseDTO response = new ApiResponseDTO(null, "Não foi possivel deletar usuário, email inválido.", 404);

            return ResponseEntity.status(404).body(response);
        }
        ContribuinteModel contribuinte = contribuinteRepository.findByCpf(user.get().getCpf());
        if (contribuinte != null) {
            contribuinteRepository.delete(contribuinte);
        }

        userRepository.delete(user.get());

        ApiResponseDTO response = new ApiResponseDTO(null, "Usuário deletado com sucesso!", 200);
        return ResponseEntity.status(200).body(response);
    }
}
