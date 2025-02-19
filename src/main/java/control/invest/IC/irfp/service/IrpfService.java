package control.invest.IC.irfp.service;

import control.invest.IC.authentication.service.ApiResponseDTO;
import control.invest.IC.irfp.dtos.IrpfDTO;
import control.invest.IC.irfp.models.ContribuinteModel;
import control.invest.IC.irfp.models.DependenteModel;
import control.invest.IC.irfp.models.IrpfModel;
import control.invest.IC.irfp.models.PagamentoModel;
import control.invest.IC.irfp.repositories.ContribuinteRepository;
import control.invest.IC.irfp.repositories.DependenteRepository;
import control.invest.IC.irfp.repositories.IrpfRepository;
import control.invest.IC.irfp.repositories.PagamentosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Optional;


@Service
public class IrpfService {
    @Autowired
    ContribuinteRepository contribuinteRepository;
    @Autowired
    DependenteRepository dependenteRepository;
    @Autowired
    PagamentosRepository pagamentosRepository;
    @Autowired
    IrpfRepository irpfRepository;

    public int getNumDependentes(String cpfContribuinte) {
        ContribuinteModel contribuinte = contribuinteRepository.findByCpf(cpfContribuinte);
        if (contribuinte == null) {
            return 0;
        }
        List<DependenteModel> dependentes = dependenteRepository.findByContribuinte_Cpf(cpfContribuinte);
        return dependentes.size();
    }

    public Double getTotalPagamentos(String cpfContribuinte) {
        List<PagamentoModel> pagamentos = pagamentosRepository.findByContribuinte_Cpf(cpfContribuinte);
        Double total = 0d;
        for (int i = 0; i < pagamentos.size(); i++) {

            total += pagamentos.get(i).getValor();

        }
        return total;
    }

    public IrpfDTO totalFolhas(String cpfContribuinte) {
        List<IrpfModel> result = irpfRepository.findByContribuinte_Cpf(cpfContribuinte);
        Double rendimentos = 0d;
        Double deducoes = 0d;
        Double impostoRetido = 0d;
        for (int i = 0; i < result.size(); i++) {
            rendimentos += result.get(i).getRendimentosTotais();
            deducoes += result.get(i).getContribPrevSocial() + result.get(i).getFapi();
            impostoRetido += result.get(i).getImpostoRetido();
        }

        IrpfDTO irpfDTO = new IrpfDTO();
        irpfDTO.setRendimentos(rendimentos);
        irpfDTO.setDeducoes(deducoes);
        irpfDTO.setImpostoRetido(impostoRetido);

        return irpfDTO;
    }

    public void putFolha (Long id,IrpfModel irpfModel) {
        Optional<IrpfModel> result = irpfRepository.findById(id);

        irpfModel = result.get();
        irpfRepository.save(irpfModel);

    }
}
