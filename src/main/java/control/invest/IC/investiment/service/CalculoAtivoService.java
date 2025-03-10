package control.invest.IC.investiment.service;

import control.invest.IC.authentication.service.ApiResponseDTO;
import control.invest.IC.investiment.DTO.AtivoDTO;
import control.invest.IC.investiment.model.FiiModel;
import control.invest.IC.investiment.repository.FiiRepository;
import control.invest.IC.irfp.models.ContribuinteModel;
import control.invest.IC.irfp.repositories.ContribuinteRepository;
import control.invest.IC.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class CalculoAtivoService {

    @Autowired
    private Response response;
    @Autowired
    private FiiRepository fiiRepository;
    @Autowired
    private ContribuinteRepository contribuinteRepository;
    @Autowired
    private AtivoService ativoService;

    public ResponseEntity<ApiResponseDTO> calcularFii(FiiModel fiiModel, String cpf) {
        ContribuinteModel contribuinte = contribuinteRepository.findByCpf(cpf);

        if (contribuinte == null) {

            return response.response(null, "Contribuinte " + cpf + " não encontrado", 404);

        }

        Optional<FiiModel> fii = fiiRepository.findBySiglaAndContribuinteId(fiiModel.getSigla(), contribuinte.getId());

        if (fii.isEmpty()) {

            return response.response(null, "Fii para o contribuinte", 404);

        }
        AtivoDTO fiiAtual = ativoService.buscarAtivo(fiiModel.getSigla().toUpperCase());

        Double precoMedio = fiiModel.getTotalValor() / fiiModel.getCotas();
        Double saldoTotal = fiiModel.getCotas() * fiiAtual.getPrecoAtual();
        Double precoAtual = fiiAtual.getPrecoAtual();
        Double variacao = (precoMedio / precoAtual) - 1;

        Map<String, Object> calculoAtivo = new LinkedHashMap<>();

        calculoAtivo.put("Cotas", fiiModel.getCotas());
        calculoAtivo.put("Preço Médio", precoMedio);
        calculoAtivo.put("Preço Atual", precoAtual);
        calculoAtivo.put("Saldo", saldoTotal);

        calculoAtivo.put("Variação", variacao);

        return response.response(calculoAtivo, "Calculo do ativo " + fiiModel.getSigla() + " realizado com sucesso!", 200);

    }

}
