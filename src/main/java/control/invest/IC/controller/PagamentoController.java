package control.invest.IC.controller;

import control.invest.IC.models.ContribuinteModel;
import control.invest.IC.models.PagamentoModel;
import control.invest.IC.repositories.ContribuinteRepository;
import control.invest.IC.repositories.PagamentosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
public class PagamentoController {
    @Autowired
    ContribuinteRepository contribuinteRepository;
    @Autowired
    PagamentosRepository pagamentosRepository;


    @PostMapping("/cad/pagamento/{cpfContribuinte}")
    public ResponseEntity<String> cadPagamento(@PathVariable String cpfContribuinte, @RequestBody PagamentoModel pagamentoModel) {
        try {
            ContribuinteModel result = contribuinteRepository.findByCpf(cpfContribuinte);

            if (result == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Contribuinte não encontrado para o cpf: " + cpfContribuinte);
            }

            pagamentoModel.setContribuinte(result);

            pagamentosRepository.save(pagamentoModel);
            return ResponseEntity.status(HttpStatus.OK).body("Pagamento cadastrado com sucesso");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao cadastrar");
        }
    }

    @GetMapping("/get/pagamento/{cpfContribuinte}")
    public ResponseEntity<List<PagamentoModel>> getPagamento(@PathVariable String cpfContribuinte) {

        ContribuinteModel result = contribuinteRepository.findByCpf(cpfContribuinte);

        if (result == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        }
        List<PagamentoModel> pagamentos = pagamentosRepository.findByContribuinte_Cpf(cpfContribuinte);

        return ResponseEntity.ok(pagamentos);
    }

    @DeleteMapping("/delete/pagamentos/{idPagamento}")
    public ResponseEntity<String> deletePagamento(@PathVariable Long idPagamento) {

        Optional<PagamentoModel> result = pagamentosRepository.findById(idPagamento);

        if (result.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi possível excluir pagamento, pagamento não encontrado!");
        }

        pagamentosRepository.delete(result.get());
        return ResponseEntity.ok("Pagamento excluído com sucesso!");
    }

    @PutMapping("/update/pagamentos/{idpagamento}")
    public ResponseEntity<String> updatePagamento(@PathVariable Long idPagamento, @RequestBody PagamentoModel novoPagamento) {
        Optional<PagamentoModel> result = pagamentosRepository.findById(idPagamento);

        if (result.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("pagamento não encontrado!");
        }

        PagamentoModel pagamentoExistente = result.get();
        pagamentoExistente.setValor(novoPagamento.getValor());
        pagamentoExistente.setIdentificacao(novoPagamento.getIdentificacao());
        pagamentoExistente.setNome(novoPagamento.getNome());

        pagamentosRepository.save(pagamentoExistente);
        return ResponseEntity.ok("Pagamento alterado com sucesso!");
    }
}
