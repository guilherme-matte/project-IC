package control.invest.IC.test;

import control.invest.IC.dtos.DadosRequestDTO;
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
    public ResponseEntity<String> testCreateContribuinte(@RequestBody DadosRequestDTO requestDTO) {
        //Metodo para criação de usuarios para fins de teste
        try {
            System.out.println(requestDTO.getContribuinteModel().getNome());
            contribuinteRepository.save(requestDTO.getContribuinteModel());


            for (int i = 0; i < requestDTO.getDependenteQuantidade(); i++) {
                DependenteModel dependente = new DependenteModel();
                dependente.setCpf(requestDTO.getDependenteModel().getCpf() + " - " + (i+1));
                dependente.setNome(requestDTO.getDependenteModel().getNome() + " - " + (i+1));
                dependente.setContribuinte(requestDTO.getContribuinteModel());
                dependenteRepository.save(dependente);
            }
            return ResponseEntity.ok("Criação do contribuinte " + requestDTO.getContribuinteModel().getCpf() + "| E mais " + requestDTO.getDependenteQuantidade() + " dependentes");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro no teste 1 - " + e.getMessage());
        }
    }

}
