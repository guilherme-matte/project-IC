package control.invest.IC.authentication.service;

import control.invest.IC.authentication.model.UserModel;
import control.invest.IC.authentication.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    UserRepository userRepository;

    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);//para quem será enviado o email
        message.setSubject(subject);//Assunto
        message.setText(text);//corpo do email
        message.setFrom("redefinicao.ic@gmail.com");

        mailSender.send(message);
    }

    public void emailBoasVindas(String email) {
        //função que envia um email para o usuário no momento que a conta é criada
        Optional<UserModel> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            String message = "Boas vindas " + user.get().getNome() + ", usuário criado com sucesso!\n**EMAIL PARA FINS DE TESTE**";
            sendEmail(email, "Usuário criado com sucesso", message);
        }
    }

}
