package control.invest.IC.irfp.controller;

import control.invest.IC.authentication.service.ApiResponseDTO;
import control.invest.IC.irfp.models.ContribuinteModel;
import control.invest.IC.irfp.models.IrpfModel;
import control.invest.IC.irfp.repositories.ContribuinteRepository;
import control.invest.IC.irfp.repositories.IrpfRepository;
import control.invest.IC.irfp.service.IrpfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class IrpfController {
    @Autowired
    ContribuinteRepository contribuinteRepository;
    @Autowired
    IrpfRepository irpfRepository;
    @Autowired
    IrpfService irpfService;

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

    @PutMapping("/update/irpf/{idFolha}")
    public ResponseEntity<ApiResponseDTO> atualizarFolha(@PathVariable Long id, @RequestBody IrpfModel irpfModel) {

        Optional<IrpfModel> result = irpfRepository.findById(id);

        if (result.isEmpty()) {
            ApiResponseDTO response = new ApiResponseDTO(null, "Não foi possível alterar folha de rendimentos, folha não encontrada", 404);
            return ResponseEntity.status(404).body(response);
        }

        irpfService.putFolha(id, irpfModel);

        ApiResponseDTO response = new ApiResponseDTO(null, "Folha alterada om sucesso!", 200);
        return ResponseEntity.status(200).body(response);
    }


    
}
