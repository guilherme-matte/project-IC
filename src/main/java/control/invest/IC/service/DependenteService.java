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

    public boolean verificaCpf(String cpf) {
        try {
            DependenteModel result = dependenteRepository.findByCpf(cpf);
            return result != null;
        } catch (Exception e) {
            System.out.println("Erro ao consultar CPF DEPENDENTE: " + e.getMessage());
            return false;

        }
    }

    public void criarDependente(DependenteModel dependenteModel, ContribuinteModel contribuinteModel) {
        try {
            ContribuinteModel result = contribuinteRepository.findByCpf(contribuinteModel.getCpf());

            dependenteModel.setContribuinte(result);
            dependenteModel.setCpf(dependenteModel.getCpf());
            dependenteModel.setNome(dependenteModel.getNome());
            dependenteRepository.save(dependenteModel);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("######### | Não foi possível criar dependente | #########");
        }

    }
}
