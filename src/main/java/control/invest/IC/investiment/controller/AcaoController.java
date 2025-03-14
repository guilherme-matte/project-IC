package control.invest.IC.investiment.controller;

import control.invest.IC.authentication.service.ApiResponseDTO;
import control.invest.IC.investiment.DTO.AtivoDTO;
import control.invest.IC.investiment.DTO.TransacaoDTO;
import control.invest.IC.investiment.repository.AcaoRepository;
import control.invest.IC.investiment.repository.FiiRepository;
import control.invest.IC.investiment.service.AtivoService;
import control.invest.IC.investiment.service.CalculoFiiService;
import control.invest.IC.irfp.models.ContribuinteModel;
import control.invest.IC.irfp.repositories.ContribuinteRepository;
import control.invest.IC.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AcaoController {
    @Autowired
    private AtivoService ativoService;
    @Autowired
    private ContribuinteRepository contribuinteRepository;
    @Autowired
    private AcaoRepository acaoRepository;
    @Autowired
    private Response response;
    @Autowired
    private CalculoFiiService calculoService;

    @GetMapping("/stock/{siglaStock}")
    public ResponseEntity<ApiResponseDTO> getStock(@PathVariable String siglaStock) {
        AtivoDTO ativo = ativoService.buscarAtivo(siglaStock.toUpperCase());

        if (ativo == null) {
            return response.response(null, "Ativo não encotrado", 404);
        }
        return response.response(ativo, "Ativo encontrado com sucesso!", 200);
    }

    @PostMapping("/stock/cad{cpfContribuinte}")
    public ResponseEntity<ApiResponseDTO> cadStock(@PathVariable String cpfContribuinte, @RequestBody TransacaoDTO dto) {
        ContribuinteModel contribuinte = contribuinteRepository.findByCpf(cpfContribuinte);

        if (contribuinte == null) {
            return response.response(null, "CPF " + cpfContribuinte + " não encontado", 404);
        }
        
    }
}
