package control.invest.IC.authentication.controller;

import control.invest.IC.authentication.model.UserModel;
import control.invest.IC.authentication.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/auth/cad/user")
    public ResponseEntity<String> cadUsuario(@RequestBody UserModel request) {
        String resposta = userService.cadUsuario(request);
        if (resposta.equals("Usuário cadastrado com sucesso")) {
            return ResponseEntity.status(HttpStatus.OK).body(resposta);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resposta);
    }

}
