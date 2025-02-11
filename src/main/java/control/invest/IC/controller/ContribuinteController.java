package control.invest.IC.controller;

import control.invest.IC.models.ContribuinteModel;
import control.invest.IC.repositories.ContribuinteRepository;
import control.invest.IC.repositories.DependenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ContribuinteController {
    @Autowired
    ContribuinteRepository contribuinteRepository;
    @Autowired
    DependenteRepository dependenteRepository;



    @GetMapping("/get/contribuinte/{cpfContribuinte}")
    public ResponseEntity<String> getContribuinte(@PathVariable String cpfContribuinte) {
        try {
            ContribuinteModel result = contribuinteRepository.findByCpf(cpfContribuinte);
            if (result == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("CPF não cadastrado");
            } else {
                return ResponseEntity.status(HttpStatus.OK).body("CPF cadastrado");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao consultar cpf na base de dados - " + e.getMessage());
        }

    }
}
