package control.invest.IC.irfp.controller;

import control.invest.IC.authentication.service.ApiResponseDTO;
import control.invest.IC.irfp.models.ContribuinteModel;
import control.invest.IC.irfp.models.IrpfModel;
import control.invest.IC.irfp.repositories.ContribuinteRepository;
import control.invest.IC.irfp.repositories.IrpfRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class IrpfController {
    @Autowired
    ContribuinteRepository contribuinteRepository;
    @Autowired
    IrpfRepository irpfRepository;

    @PostMapping("/cad/folha/{cpfContribuinte}")
    public ResponseEntity<ApiResponseDTO> cadFolha(@PathVariable String cpfContribuinte, @RequestBody IrpfModel irpfModel) {
        ContribuinteModel result = contribuinteRepository.findByCpf(cpfContribuinte);

        if (result == null) {
            ApiResponseDTO response = new ApiResponseDTO(null, "Usuário não encontrado", 404);
            return ResponseEntity.status(404).body(response);
        }

        irpfModel.setContribuinte(result);
        irpfRepository.save(irpfModel);
        ApiResponseDTO response = new ApiResponseDTO(null, "Folha cadastrada com sucesso", 200);
        return ResponseEntity.status(404).body(response);

    }

}
