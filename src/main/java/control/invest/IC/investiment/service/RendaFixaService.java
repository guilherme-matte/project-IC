package control.invest.IC.investiment.service;

import control.invest.IC.irfp.models.ContribuinteModel;
import control.invest.IC.irfp.repositories.ContribuinteRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class RendaFixaService {
    @Autowired
    private ContribuinteModel contribuinte;
    @Autowired
    private ContribuinteRepository contribuinteRepository;
}
