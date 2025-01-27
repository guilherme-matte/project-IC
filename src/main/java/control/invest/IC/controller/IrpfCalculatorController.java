package control.invest.IC.controller;

import control.invest.IC.dtos.CalculatorDTO;
import control.invest.IC.models.IrpfModel;
import control.invest.IC.repositories.ContribuinteRepository;
import control.invest.IC.utilities.Utilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;

@RestController
public class IrpfCalculatorController {
    @Autowired
    ContribuinteRepository contribuinteRepository;
    @Autowired
    ContribuinteController contribuinteController;
    @Autowired
    DependenteController dependenteController;

    Utilities utilities = new Utilities();

    private double calcularDependentes(int numDependente) {
        if (numDependente <= 0) {
            return 0;
        } else {
            return numDependente * 2275.08;
        }
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

    @PostMapping("/irpf/calculator")
    public ResponseEntity<LinkedHashMap<String, Object>> calcularIrpf(@RequestBody CalculatorDTO calculatorDTO) {
        try {
            IrpfModel irpfModel = calculatorDTO.getIrpfModel();
            double pagamento = calculatorDTO.getPagamento();
            int dependente = calculatorDTO.getDependente();

            double totalDependentes = calcularDependentes(dependente);

            double deducoes = totalDependentes + irpfModel.getContribPrevSocial() + pagamento + irpfModel.getFapi();


            double rendimentos = irpfModel.getRendimentosTotais() - deducoes;


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


            LinkedHashMap irpf = new LinkedHashMap();

            irpf.put("rendimento", valueFormat(rendimentos));
            irpf.put("imposto", valueFormat(imposto));
            irpf.put("deducoes", valueFormat(deducoes));

            if (imposto > irpfModel.getImpostoRetido() || imposto == irpfModel.getImpostoRetido()) {
                irpf.put("pagar", valueFormat(imposto - irpfModel.getImpostoRetido()));
            } else if (imposto < irpfModel.getImpostoRetido()) {
                irpf.put("restituir", valueFormat(irpfModel.getImpostoRetido() - imposto));
            }

            if (al == 0) {
                irpf.put("aliquota", "0%");
            } else {
                irpf.put("aliquota", utilities.formatarValor(al * 100) + "%");
            }

            return ResponseEntity.status(HttpStatus.OK).body(irpf);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private double valueFormat(double valor) {

        return Double.parseDouble(utilities.formatarValor(valor).replace(".", "").replace(",", "."));
    }
}
