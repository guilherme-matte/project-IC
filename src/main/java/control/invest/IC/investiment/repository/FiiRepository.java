package control.invest.IC.investiment.repository;

import control.invest.IC.investiment.model.FiiModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FiiRepository extends JpaRepository<FiiModel, Long> {

    Optional<FiiModel> findBySiglaAndContribuinteId(String sigla,Long contribuinteId);
    List<FiiModel> findByContribuinteId(Long contribuinteId);

}
