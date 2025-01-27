package control.invest.IC.service;

import control.invest.IC.models.ContribuinteModel;
import control.invest.IC.repositories.ContribuinteRepository;
import control.invest.IC.repositories.DependenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CadastroService {
    @Autowired
    ContribuinteService contribuinteService;
    @Autowired
    ContribuinteRepository contribuinteRepository;
    @Autowired
    DependenteService dependenteService;
    @Autowired
    DependenteRepository dependenteRepository;

    public void cadastrarContribuinte(ContribuinteModel contribuinteModel) {
        if (!contribuinteService.consultaCpf(contribuinteModel.getCpf())) {
            contribuinteRepository.save(contribuinteModel);
        }
    }
    
}
