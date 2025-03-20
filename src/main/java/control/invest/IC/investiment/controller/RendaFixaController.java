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
}
