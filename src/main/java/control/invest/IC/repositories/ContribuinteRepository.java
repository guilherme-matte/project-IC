package control.invest.IC.repositories;

import control.invest.IC.models.ContribuinteModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContribuinteRepository extends JpaRepository<ContribuinteModel, Long> {
    ContribuinteModel findByCpf(String cpf);
}
