package control.invest.IC.controller;

import control.invest.IC.models.ContribuinteModel;
import control.invest.IC.models.DependenteModel;
import control.invest.IC.repositories.ContribuinteRepository;
import control.invest.IC.repositories.DependenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/get")
public class DependenteController {
    @Autowired
    DependenteRepository dependenteRepository;
    @Autowired
    ContribuinteRepository contribuinteRepository;

    @GetMapping("/dependentes/{cpfContribuinte}")
//retorna uma lista com todos os dependentes vinculados ao contribuinte, ou retorna uma lista vazia, caso o contribuinte não tenha nenhum dependente
    public ResponseEntity<List<DependenteModel>> getDependentes(@PathVariable String cpfContribuinte) {
        ContribuinteModel contribuinte = contribuinteRepository.findByCpf(cpfContribuinte);
        if (contribuinte == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());//retorna status not found caso não encontre o contribuinte
        }
        List<DependenteModel> dependentes = dependenteRepository.findByContribuinte_Cpf(cpfContribuinte);
        return ResponseEntity.ok(dependentes);

    }


}
