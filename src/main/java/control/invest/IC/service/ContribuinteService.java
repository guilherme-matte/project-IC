package control.invest.IC.service;

import control.invest.IC.models.ContribuinteModel;
import control.invest.IC.models.DependenteModel;
import control.invest.IC.repositories.ContribuinteRepository;
import control.invest.IC.repositories.DependenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContribuinteService {
    @Autowired
    ContribuinteRepository contribuinteRepository;
    @Autowired
    DependenteRepository dependenteRepository;
    @Autowired
    DependenteService dependenteService;

    public boolean consultaCpf(String cpf) {
        try {
            ContribuinteModel result = contribuinteRepository.findByCpf(cpf);

            //verifica se já existe um cpf igual cadastrado
            return result != null;

        } catch (Exception e) {
            System.out.println("Erro ao consultar CPF CONTRIBUINTE: " + e.getMessage());
            return false;
        }
    }
public void atualizarContribuinte (DependenteModel dependenteModel, ContribuinteModel contribuinteModel){
        try {

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao atualizar contribuinte: "+contribuinteModel.getCpf()+" - "+e.getMessage());
        }
}
    public void criarContribuinte(DependenteModel dependenteModel, ContribuinteModel contribuinteModel) {
        try {
            if (!consultaCpf(contribuinteModel.getCpf())){
                contribuinteRepository.save(contribuinteModel);
                dependenteService.criarDependente(dependenteModel, contribuinteModel);
            }else{
                System.out.println("CPF já cadastrado!");
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("######### | Não foi possível criar contribuinte | #########");
        }
    }

}
