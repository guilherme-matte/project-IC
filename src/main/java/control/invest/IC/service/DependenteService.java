package control.invest.IC.service;

import control.invest.IC.models.ContribuinteModel;
import control.invest.IC.models.DependenteModel;
import control.invest.IC.repositories.ContribuinteRepository;
import control.invest.IC.repositories.DependenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
public class DependenteService {
    @Autowired
    DependenteRepository dependenteRepository;
    @Autowired
    ContribuinteRepository contribuinteRepository;

    public void criarDependente(DependenteModel dependenteModel, String cpf) {
        ContribuinteModel contribuinte = contribuinteRepository.findByCpf(cpf);
        dependenteModel.setContribuinte(contribuinte);
        dependenteModel.setCpf(dependenteModel.getCpf());
        dependenteModel.setNome(dependenteModel.getNome());
        try {
            dependenteRepository.save(dependenteModel);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("######### | Não foi possível criar dependente | #########");
        }

    }
}
