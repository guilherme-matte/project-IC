package control.invest.IC.irfp.repositories;

import control.invest.IC.irfp.models.PagamentoModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PagamentosRepository extends JpaRepository<PagamentoModel, Long> {
    List<PagamentoModel> findByContribuinte_Cpf(String cpfContribuinte);
}
