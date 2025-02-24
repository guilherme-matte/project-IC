package control.invest.IC.investiment.controller;

import control.invest.IC.config.BrapiConfig;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FiiController {
    private BrapiConfig brapiConfig;

    @GetMapping("/token")
    public void mostrarToken() {
        System.out.println("TOKEN: " + brapiConfig.getBrapiToken());
    }


}
