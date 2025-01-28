package control.invest.IC.test;

import control.invest.IC.models.ContribuinteModel;
import control.invest.IC.models.DependenteModel;
import control.invest.IC.repositories.ContribuinteRepository;
import control.invest.IC.repositories.DependenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/test")
public class Test {
    @Autowired
    ContribuinteRepository contribuinteRepository;
    @Autowired
    DependenteRepository dependenteRepository;

    @PostMapping("/contribuinte")
    public ResponseEntity<String> testCreateContribuinte(@RequestBody ContribuinteModel contribuinteModel, @RequestBody DependenteModel dependenteModel, @RequestBody int dependentesQuantidade) {
        //Metodo para criação de usuarios para fins de teste
        try {
            contribuinteRepository.save(contribuinteModel);

            DependenteModel dependente = new DependenteModel();
            for (int i = 0; i < dependentesQuantidade; i++) {
                dependente.setCpf(dependenteModel.getCpf() + " - " + i);
                dependente.setNome(dependenteModel.getNome() + " - " + i);
                dependente.setContribuinte(contribuinteModel);
                dependenteRepository.save(dependente);
            }
            return ResponseEntity.ok("Criação do contribuinte " + contribuinteModel.getCpf() + "| E mais "+dependentesQuantidade+ " dependentes");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro no teste 1 - " + e.getMessage());
        }
    }

}
