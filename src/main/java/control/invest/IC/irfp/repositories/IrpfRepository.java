package control.invest.IC.irfp.repositories;

import control.invest.IC.irfp.models.IrpfModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IrpfRepository extends JpaRepository<IrpfModel, Long> {
    List<IrpfModel> findByContribuinte_Cpf(String cpfContribuinte);
}
