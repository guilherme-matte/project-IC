package control.invest.IC.authentication.service;

import control.invest.IC.authentication.model.UserModel;
import control.invest.IC.authentication.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {


    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SenhaService senhaService;

    public String cadUsuario(UserModel request) {
        Optional<UserModel> existingUserByEmail = userRepository.findByEmail(request.getEmail());
        Optional<UserModel> existingUserByCpf = userRepository.findByCpf(request.getCpf());

        if (existingUserByCpf.isPresent() || existingUserByEmail.isPresent()) {
            throw new RuntimeException("cpf ou email já cadastrado");
        }
        UserModel user = new UserModel();
        user.setNome(request.getNome());
        user.setSobrenome(request.getSobrenome());
        user.setDataNascimento(request.getDataNascimento());
        user.setCpf(request.getCpf());
        user.setEmail(request.getEmail());
        user.setCelular(request.getCelular());
        user.setSenha(senhaService.hashSenha(request.getSenha()));
        userRepository.save(user);
        return "Usuário cadastrado com sucesso";
    }

}
