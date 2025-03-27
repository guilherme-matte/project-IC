package control.invest.IC.investiment.controller;

import control.invest.IC.authentication.service.ApiResponseDTO;
import control.invest.IC.investiment.model.RendaFixaModel;
import control.invest.IC.investiment.repository.RendaFixaRepository;
import control.invest.IC.investiment.service.RendaFixaService;
import control.invest.IC.irfp.models.ContribuinteModel;
import control.invest.IC.irfp.repositories.ContribuinteRepository;
import control.invest.IC.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;


@EnableScheduling
@RestController
public class RendaFixaController {
    @Autowired
    private RendaFixaService rendaFixaService;
    @Autowired
    private ContribuinteRepository contribuinteRepository;
    @Autowired
    private Response apiResponse;
    @Autowired
    private RendaFixaRepository rendaFixaRepository;


    @Scheduled(cron = "0 0 12 * * *", zone = "America/Sao_Paulo")

    public void atualizarRendaFixa() {

        rendaFixaService.rotinaAtualizarRendaFixa();
        System.out.println("Rotina (556) executada com sucesso");

    }

    @PostMapping("/cad/renda-fixa/{cpfContribuinte}")
    public ResponseEntity<ApiResponseDTO> cadRendaFixa(@PathVariable String cpfContribuinte, @RequestBody RendaFixaModel rendaFixa) {

        ContribuinteModel contribuinte = contribuinteRepository.findByCpf(cpfContribuinte);

        if (contribuinte == null) {
            return apiResponse.response(null, "Contribuinte não encontrado", 404);
        }

        rendaFixa.setContribuinte(contribuinte);

        rendaFixaRepository.save(rendaFixa);

        return apiResponse.response(null, "Renda fixa cadastrada com sucesso", 200);

    }

    @GetMapping("/renda-fixa/{cpfContribuinte}")
    public ResponseEntity<ApiResponseDTO> getRendaFixa(@PathVariable String cpfContribuinte) {
        ContribuinteModel contribuinte = contribuinteRepository.findByCpf(cpfContribuinte);
        if (contribuinte == null) {
            return apiResponse.response(null, "CPF não encontrado", 404);
        }
        return apiResponse.response(rendaFixaService.getRendaFixa(contribuinte), "Lista gerada com sucesso", 200);
    }

    @GetMapping("/renda-fixa/{cpfContribuinte}/{rendaFixaId}")
    public ResponseEntity<ApiResponseDTO> getRendaFixaById(@PathVariable Long rendaFixaId, @PathVariable String cpfContribuinte) {
        Optional<RendaFixaModel> rendaFixa = rendaFixaRepository.findById(rendaFixaId);
        if (rendaFixa.isEmpty()) {
            return apiResponse.response(null, "Aplicação não encontrado", 404);
        }
        ContribuinteModel contribuinte = contribuinteRepository.findByCpf(cpfContribuinte);
        if (contribuinte == null) {
            return apiResponse.response(null, "Contribuinte não encontrado", 404);
        }
        if (!Objects.equals(contribuinte.getId(), rendaFixa.get().getContribuinte().getId())) {
            return apiResponse.response(null, "Ativo não vinculado ao contribuinte", 404);
        }

        return apiResponse.response(rendaFixa, "Ativo encontrado com sucesso", 200);

    }

    public boolean verificarIdRendaFixa_Contribuinte(ContribuinteModel contribuinte, RendaFixaModel rendaFixa) {
        long contribuinteId = contribuinte.getId();
        long rendaFixaId = rendaFixa.getContribuinte().getId();

        if (contribuinteId != rendaFixaId) {
            return true;
        } else {
            return false;
        }
    }

    @PutMapping("/renda-fixa/{cpfContribuinte}/{rendaFixaId}")
    public ResponseEntity<ApiResponseDTO> putRendaFixa(@PathVariable String cpfContribuinte, @PathVariable Long rendaFixaId, @RequestBody RendaFixaModel rendaFixaBody) {
        ContribuinteModel contribuinte = contribuinteRepository.findByCpf(cpfContribuinte);
        if (contribuinte == null) {
            return apiResponse.response(null, "CPF não encontrado", 404);
        }
        RendaFixaModel rendaFixa = rendaFixaRepository.findByContribuinteIdAndId(contribuinte.getId(), rendaFixaId);

        if (rendaFixa == null) {
            return apiResponse.response(null, "Ativo não encontrado", 404);
        }

        rendaFixa.setSaldo(rendaFixaBody.getSaldo());
        rendaFixa.setCdi(rendaFixaBody.getCdi());

        rendaFixaRepository.save(rendaFixa);
        return apiResponse.response(rendaFixa, "Ativo alterado com sucesso", 200);
    }

    @DeleteMapping("/renda-fixa/{cpfContribuinte}/")
    public ResponseEntity<ApiResponseDTO> deleteRendaFixa(@PathVariable String cpfContribuinte, @PathVariable Long rendaFixaId) {

        ContribuinteModel contribuinte = contribuinteRepository.findByCpf(cpfContribuinte);
        if (contribuinte == null) {
            return apiResponse.response(null, "CPF não encontrado", 404);
        }
        RendaFixaModel rendaFixa = rendaFixaRepository.findByContribuinteIdAndId(contribuinte.getId(), rendaFixaId);
        if (rendaFixa == null) {
            return apiResponse.response(null, "Ativo não encontrado", 404);
        }

        if (verificarIdRendaFixa_Contribuinte(contribuinte, rendaFixa)) {
            return apiResponse.response(null, "Ativo não vinculado ao contribuinte", 404);
        }
        rendaFixaRepository.delete(rendaFixa);
        return apiResponse.response(null, "Ativo deletado com sucesso", 200);
    }

}
