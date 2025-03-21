package control.invest.IC.investiment.repository;

import control.invest.IC.investiment.model.AcaoModel;
import control.invest.IC.investiment.model.RendaFixaModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface RendaFixaRepository extends JpaRepository<RendaFixaModel, Long> {

    List<RendaFixaModel> findByContribuinteId(Long contribuinteId);

    RendaFixaModel findByContribuinteIdAndRendaFixaId(Long contribuinteId, Long RendaFixaId);
}
