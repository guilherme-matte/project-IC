package control.invest.IC.irfp.repositories;

import control.invest.IC.irfp.models.ContribuinteModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContribuinteRepository extends JpaRepository<ContribuinteModel, Long> {
    ContribuinteModel findByCpf(String cpf);
}
