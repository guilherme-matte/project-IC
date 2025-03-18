package control.invest.IC.investiment.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
@RestController
public class RendaFixaController {
    @GetMapping("/tempoAtual")
    public String verHoras() {
        Date hora = new Date();
        return hora.toString();
    }
}
