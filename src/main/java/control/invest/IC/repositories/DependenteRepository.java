package control.invest.IC.repositories;

import control.invest.IC.models.DependenteModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DependenteRepository extends JpaRepository<DependenteModel, Long> {
    DependenteModel findByCpf(String cpf);
}
