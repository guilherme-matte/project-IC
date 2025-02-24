package control.invest.IC.investiment.controller;

import control.invest.IC.authentication.service.ApiResponseDTO;
import control.invest.IC.config.BrapiConfig;
import control.invest.IC.investiment.service.AtivoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FiiController {
    @Autowired
    private BrapiConfig brapiConfig;
    @Autowired
    private AtivoService ativoService;

    @GetMapping("/token")
    public void mostrarToken() {
        System.out.println("TOKEN: " + brapiConfig.getBrapiToken());
    }

    @GetMapping("/fii/{siglaFii}")
    public ResponseEntity<ApiResponseDTO> retornarFii(@PathVariable String siglaFii) {
        ApiResponseDTO response = new ApiResponseDTO(ativoService.buscarAtivo(siglaFii.toUpperCase()), "Fii encontrado com sucesso", 200);
        return ResponseEntity.status(200).body(response);
    }

}
