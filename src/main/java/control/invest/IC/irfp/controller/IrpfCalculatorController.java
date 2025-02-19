package control.invest.IC.irfp.controller;

import control.invest.IC.authentication.service.ApiResponseDTO;
import control.invest.IC.irfp.dtos.DadosRequestDTO;
import control.invest.IC.irfp.dtos.IrpfDTO;
import control.invest.IC.irfp.models.IrpfModel;
import control.invest.IC.irfp.repositories.ContribuinteRepository;
import control.invest.IC.irfp.repositories.IrpfRepository;
import control.invest.IC.irfp.service.IrpfService;
import control.invest.IC.irfp.utilities.Utilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

@RestController
public class IrpfCalculatorController {
    @Autowired
    ContribuinteRepository contribuinteRepository;
    @Autowired
    ContribuinteController contribuinteController;
    @Autowired
    DependenteController dependenteController;
    @Autowired
    IrpfRepository irpfRepository;


    IrpfService irpfService;

    Utilities utilities = new Utilities();

    private double calcularDependentes(String cpfContribuinte) {

        return 2275.08 * irpfService.getNumDependentes(cpfContribuinte);

    }

    private double calcularImposto(double rendimentos) {
        double imposto = 0;
        double parcelaDedutivel = 0;
        if (rendimentos > 55976.16) {
            imposto += (rendimentos - 55976.16) * 0.275;
            parcelaDedutivel = 896.00;
            rendimentos = 55976.16;
        }
        if (rendimentos > 45012.60) {
            imposto += (rendimentos - 45012.60) * 0.225;
            parcelaDedutivel = 662.77;
            rendimentos = 45012.60;
        }
        if (rendimentos > 33919.80) {
            imposto += (rendimentos - 33919.80) * 0.15;
            parcelaDedutivel = 381.44;
            rendimentos = 33919.80;

        }
        if (rendimentos > 24511.92) {
            imposto += (rendimentos - 24511.92) * 0.075;
            parcelaDedutivel = 169.44;
        }
        imposto -= parcelaDedutivel;
        return imposto;
    }

    @PostMapping("/irpf/somaFolha/{cpfContribuinte}")
    public ResponseEntity<ApiResponseDTO> somaFolha(@PathVariable String cpfContribuinte){
    IrpfDTO irpfDTO = irpfService.totalFolhas(cpfContribuinte);
        ApiResponseDTO response = new ApiResponseDTO(irpfDTO,"Folha calculada com sucesso!",200);
        return ResponseEntity.status(200).body(response);
    }
    @PostMapping("/irpf/calculator/{cpfContribuinte}")
    public ResponseEntity<ApiResponseDTO> calcularIrpf(@PathVariable String cpfContribuinte) {


        double pagamento = irpfService.getTotalPagamentos(cpfContribuinte);

        double totalDependentes = calcularDependentes(cpfContribuinte);
        IrpfDTO irpf = irpfService.totalFolhas(cpfContribuinte);

        double deducoes = totalDependentes + irpf.getDeducoes();


        double rendimentos = irpf.getRendimentos() - (deducoes - pagamento);

        if (irpf.getRendimentos() <= 0) {
            ApiResponseDTO response = new ApiResponseDTO(null, "Sem folha de pagamento para poder realizar o calculo \n Adicione uma folha e tente novamente", 400);
            return ResponseEntity.status(400).body(response);
        }


        double al = 0;
        double imposto = 0;
        if (rendimentos <= 24511.92) {
            al = 0;
            imposto = calcularImposto(rendimentos);
        } else if (rendimentos >= 24511.93 && rendimentos <= 33919.80) {
            al = 0.075;
            imposto = calcularImposto(rendimentos);

        } else if (rendimentos >= 33919.81 && rendimentos <= 45012.60) {
            al = 0.15;
            imposto = calcularImposto(rendimentos);

        } else if (rendimentos >= 45012.61 && rendimentos <= 55976.16) {
            al = 0.225;
            imposto = calcularImposto(rendimentos);

        } else if (rendimentos >= 55976.16) {
            al = 0.275;
            imposto = calcularImposto(rendimentos);

        }


        LinkedHashMap irpfMap = new LinkedHashMap();

        irpfMap.put("rendimento", valueFormat(rendimentos));
        irpfMap.put("imposto", valueFormat(imposto));
        irpfMap.put("deducoes", valueFormat(deducoes));

        if (imposto > irpf.getImpostoRetido() || imposto == irpf.getImpostoRetido()) {
            irpfMap.put("pagar", valueFormat(imposto - irpf.getImpostoRetido()));
        } else if (imposto < irpf.getImpostoRetido()) {
            irpfMap.put("restituir", valueFormat(irpf.getImpostoRetido() - imposto));
        }

        if (al == 0) {
            irpfMap.put("aliquota", "0%");
        } else {
            irpfMap.put("aliquota", utilities.formatarValor(al * 100) + "%");
        }
        ApiResponseDTO response = new ApiResponseDTO(irpfMap, "Calculo realizado com sucesso", 200);
        return ResponseEntity.status(200).body(response);

    }

    private double valueFormat(double valor) {

        return Double.parseDouble(utilities.formatarValor(valor).replace(".", "").replace(",", "."));
    }
}
