package control.invest.IC.investiment.repository;

import control.invest.IC.investiment.model.AcoesModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AcoesRepository extends JpaRepository<AcoesModel, Long> {

    Optional<AcoesModel> findBySiglaAndContribuinteId(String sigla, Long contribuinteId);

    List<AcoesModel> findByContribuinteId(Long contribuinteId);

}
