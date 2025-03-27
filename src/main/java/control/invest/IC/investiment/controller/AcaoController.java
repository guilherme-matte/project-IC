package control.invest.IC.investiment.controller;

import control.invest.IC.authentication.service.ApiResponseDTO;
import control.invest.IC.investiment.DTO.AtivoDTO;
import control.invest.IC.investiment.DTO.TransacaoDTO;
import control.invest.IC.investiment.model.AcaoModel;
import control.invest.IC.investiment.repository.AcaoRepository;
import control.invest.IC.investiment.service.AtivoService;
import control.invest.IC.investiment.service.CalculoFiiService;
import control.invest.IC.irfp.models.ContribuinteModel;
import control.invest.IC.irfp.repositories.ContribuinteRepository;
import control.invest.IC.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.Optional;

@RestController
public class AcaoController {
    @Autowired
    private AtivoService ativoService;
    @Autowired
    private ContribuinteRepository contribuinteRepository;
    @Autowired
    private AcaoRepository acaoRepository;
    @Autowired
    private CalculoFiiService calculoService;
    @Autowired
    private Response apiResponse;

    @GetMapping("/stock/{siglaStock}")
    public ResponseEntity<ApiResponseDTO> getStock(@PathVariable String siglaStock) {
        AtivoDTO ativo = ativoService.buscarAtivo(siglaStock.toUpperCase());

        if (ativo == null) {
            return apiResponse.response(null, "Ativo não encotrado", 404);
        }
        return apiResponse.response(ativo, "Ativo encontrado com sucesso!", 200);
    }

    private boolean verificarAtivo(String siglaStock, long contribuinteId) {
        Optional<AcaoModel> stock = acaoRepository.findBySiglaAndContribuinteId(siglaStock, contribuinteId);
        if (stock.isPresent()) {
            return true;

        } else {
            return false;
        }
    }

    @PostMapping("/stock/cad{cpfContribuinte}")
    public ResponseEntity<ApiResponseDTO> cadStock(@PathVariable String cpfContribuinte, @RequestBody TransacaoDTO dto) {
        ContribuinteModel contribuinte = contribuinteRepository.findByCpf(cpfContribuinte);

        if (contribuinte == null) {
            return apiResponse.response(null, "CPF " + cpfContribuinte + " não encontado", 404);
        }
        if (verificarAtivo(dto.getSigla(), contribuinte.getId())) {
            return apiResponse.response(null, "Ativo já cadastrado para o contribuinte", 409);
        }

        AtivoDTO stockAtual = ativoService.buscarAtivo(dto.getSigla().toUpperCase());

        AcaoModel stock = new AcaoModel();
        stock.setContribuinte(contribuinte);
        stock.setSigla(dto.getSigla());
        stock.setCotas(dto.getCotas());
        stock.setTotalValor(dto.getCotas() * stockAtual.getPrecoAtual());

        acaoRepository.save(stock);

        return apiResponse.response(stock, "Ativo cadastrado com sucesso", 200);
    }

    @PutMapping("/stock/{cpfContribuinte}")
    public ResponseEntity<ApiResponseDTO> putStock(@PathVariable String cpfContribuinte, @RequestBody TransacaoDTO dto) {
        ContribuinteModel contribuinte = contribuinteRepository.findByCpf(cpfContribuinte);
        if (contribuinte == null) {
            return apiResponse.response(null, "CPF não encontrado", 404);
        }
        Optional<AcaoModel> stock = acaoRepository.findBySiglaAndContribuinteId(dto.getSigla(), contribuinte.getId());
        if (stock.isEmpty()) {
            return apiResponse.response(null, "Ativo não encontrado", 404);
        }
        if (Objects.equals(dto.getTipo(), "compra")) {
            stock.get().setCotas(stock.get().getCotas() + dto.getCotas());

            stock.get().setTotalValor(stock.get().getTotalValor() + (dto.getCotas() * dto.getValorCota()));
        } else {
            if (dto.getCotas() > stock.get().getCotas()) {
                stock.get().setCotas(0);
            } else {
                stock.get().setCotas(stock.get().getCotas() - dto.getCotas());
            }

            stock.get().setTotalValor(stock.get().getTotalValor() - (dto.getCotas() * dto.getValorCota()));
        }
        acaoRepository.save(stock.get());
        return apiResponse.response(stock.get(), dto.getTipo() + " realizado com sucesso", 200);
    }

    @DeleteMapping("/stock/{cpfContribuinte}/{siglaStock}")
    public ResponseEntity<ApiResponseDTO> deleteStock(@PathVariable String cpfContribuinte, @PathVariable String siglaStock) {
        ContribuinteModel contribuinte = contribuinteRepository.findByCpf(cpfContribuinte);
        if (contribuinte == null) {
            return apiResponse.response(null, "CPF não encontrado", 404);
        }
        Optional<AcaoModel> stock = acaoRepository.findBySiglaAndContribuinteId(siglaStock, contribuinte.getId());
        if (stock.isEmpty()) {
            return apiResponse.response(null, "Ativo não encontrado", 404);
        }
        acaoRepository.delete(stock.get());
        return apiResponse.response(null, "Ativo deletado com sucesso", 200);
    }

    
}
