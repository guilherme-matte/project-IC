package control.invest.IC.investiment.repository;

import control.invest.IC.investiment.model.AcaoModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AcaoRepository extends JpaRepository<AcaoModel, Long> {

    Optional<AcaoModel> findBySiglaAndContribuinteId(String sigla, Long contribuinteId);

    List<AcaoModel> findByContribuinteId(Long contribuinteId);

}
