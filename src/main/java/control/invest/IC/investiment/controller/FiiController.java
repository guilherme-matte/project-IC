package control.invest.IC.investiment.controller;

import control.invest.IC.authentication.service.ApiResponseDTO;
import control.invest.IC.config.BrapiConfig;
import control.invest.IC.investiment.DTO.AtivoDTO;
import control.invest.IC.investiment.DTO.TransacaoDTO;
import control.invest.IC.investiment.model.FiiModel;
import control.invest.IC.investiment.repository.FiiRepository;
import control.invest.IC.investiment.service.AtivoService;
import control.invest.IC.irfp.models.ContribuinteModel;
import control.invest.IC.irfp.repositories.ContribuinteRepository;
import control.invest.IC.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class FiiController {
    @Autowired
    private BrapiConfig brapiConfig;
    @Autowired
    private AtivoService ativoService;
    @Autowired
    private ContribuinteRepository contribuinteRepository;
    @Autowired
    private FiiRepository fiiRepository;
    @Autowired
    private Response response;


    @GetMapping("/fii/{siglaFii}")
    public ResponseEntity<ApiResponseDTO> retornarFii(@PathVariable String siglaFii) {
        ApiResponseDTO response = new ApiResponseDTO(ativoService.buscarAtivo(siglaFii.toUpperCase()), "Fii encontrado com sucesso", 200);
        return ResponseEntity.status(200).body(response);
    }

    @PostMapping("/fii/cad/{cpfContribuinte}")
    public ResponseEntity<ApiResponseDTO> cadFii(@PathVariable String cpfContribuinte, @RequestBody TransacaoDTO dto) {
        ContribuinteModel result = contribuinteRepository.findByCpf(cpfContribuinte);
        if (result == null) {
            ApiResponseDTO response = new ApiResponseDTO(null, "CPF " + cpfContribuinte + " não encontrado", 404);
            return ResponseEntity.status(404).body(response);
        }
        Optional<FiiModel> resultFii = fiiRepository.findBySiglaAndContribuinteId(dto.getSigla().toUpperCase(), result.getId());
        if (resultFii.isPresent()) {
            return response.response(resultFii, "Fii já encontrado para este usuário.", 409);
        }
        AtivoDTO ativoDTO = ativoService.buscarAtivo(dto.getSigla());

        FiiModel fii = new FiiModel();
        fii.setSigla(ativoDTO.getSigla());
        fii.setContribuinte(result);
        fii.setCotas(dto.getCotas());
        fii.setTotalValor(ativoDTO.getPrecoAtual() * dto.getCotas());

        fiiRepository.save(fii);

        ApiResponseDTO response = new ApiResponseDTO(null, "Cadastrado com sucesso", 200);
        return ResponseEntity.status(200).body(response);
    }

    @PutMapping("/fii/upd/{cpfContribuinte}")
    public ResponseEntity<ApiResponseDTO> updFii(@PathVariable String cpfContribuinte, @RequestBody TransacaoDTO dto) {

        ContribuinteModel result = contribuinteRepository.findByCpf(cpfContribuinte);

        if (result == null) {
            return response.response(null, "CPF " + cpfContribuinte + " não encontrado", 404);
        }

        Optional<FiiModel> fiiResult = fiiRepository.findBySiglaAndContribuinteId(dto.getSigla(), result.getId());

        if (fiiResult.isEmpty()) {
            return response.response(null, "Fii não encontrado para o usuário " + cpfContribuinte, 404);
        }

        AtivoDTO ativoDTO = ativoService.buscarAtivo(dto.getSigla().toUpperCase());
        FiiModel fiiNovo = fiiResult.get();

        String resposta;

        if (dto.getTipo().equalsIgnoreCase("compra")) {
            fiiNovo.setTotalValor(fiiNovo.getTotalValor() + (dto.getCotas() * ativoDTO.getPrecoAtual()));

            fiiNovo.setCotas(fiiNovo.getCotas() + dto.getCotas());
            resposta = "Fii comprado com sucesso!";
        } else {
            fiiNovo.setTotalValor(fiiNovo.getTotalValor() - (dto.getCotas() * ativoDTO.getPrecoAtual()));

            fiiNovo.setCotas(fiiNovo.getCotas() - dto.getCotas());
            resposta = "Fii vendido com sucesso!";
        }


        fiiRepository.save(fiiNovo);

        return response.response(fiiNovo, resposta, 200);
    }
}
