package control.invest.IC.irfp.service;

import control.invest.IC.irfp.models.ContribuinteModel;
import control.invest.IC.irfp.models.DependenteModel;
import control.invest.IC.irfp.repositories.ContribuinteRepository;
import control.invest.IC.irfp.repositories.DependenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;



@Service
public class IrpfService {
    @Autowired
    ContribuinteRepository contribuinteRepository;
    @Autowired
    DependenteRepository dependenteRepository;
    public int getNumDependentes(String cpfContribuinte) {
        ContribuinteModel contribuinte = contribuinteRepository.findByCpf(cpfContribuinte);
        if (contribuinte == null) {
            return 0;
        }
        List<DependenteModel> dependentes = dependenteRepository.findByContribuinte_Cpf(cpfContribuinte);
        return dependentes.size();
    }
}
