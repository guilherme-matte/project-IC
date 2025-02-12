package control.invest.IC.irfp.repositories;

import control.invest.IC.irfp.models.DependenteModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DependenteRepository extends JpaRepository<DependenteModel, Long> {
    DependenteModel findByCpf(String cpf);
    List<DependenteModel> findByContribuinte_Cpf(String cpfContribuinte);
}
