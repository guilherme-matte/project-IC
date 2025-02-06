package control.invest.IC.controller;

import control.invest.IC.models.ContribuinteModel;
import control.invest.IC.models.PagamentoModel;
import control.invest.IC.repositories.ContribuinteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PagamentoController {
    @Autowired
    ContribuinteRepository contribuinteRepository;

    @PostMapping("/cad/pagamento/{cpfContribuinte}")

    public ResponseEntity<String> cadPagamento(@PathVariable String cpfContribuinte, PagamentoModel pagamentoModel) {
        try {
            ContribuinteModel result = contribuinteRepository.findByCpf(cpfContribuinte);

            if (result == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Contribuinte não encontrado para o cpf: " + cpfContribuinte);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao cadastrar");
        }
    }
}
