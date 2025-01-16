package control.invest.IC.service;

import control.invest.IC.models.ContribuinteModel;
import control.invest.IC.models.DependenteModel;
import control.invest.IC.repositories.ContribuinteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContribuinteService {
    @Autowired
    ContribuinteRepository contribuinteRepository;

    public void criarContribuinte(DependenteModel dependenteModel, ContribuinteModel contribuinteModel) {
        try {
            DependenteService dependenteService = new DependenteService();
           

            contribuinteRepository.save(contribuinteModel);
            dependenteService.criarDependente(dependenteModel, contribuinteModel.getCpf());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("######### | Não foi possível criar contribuinte | #########");
        }
    }

}
