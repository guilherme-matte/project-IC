package control.invest.IC.authentication.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;

@Service
public class SenhaService {
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public String hashSenha(String senha) {
        return passwordEncoder.encode(senha);
    }

    public boolean verificarSenha(String senhaDigitada, String senhaHash) {
        return passwordEncoder.matches(senhaDigitada, senhaHash);
    }

    public static String gerarSenha() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[6];
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

}
