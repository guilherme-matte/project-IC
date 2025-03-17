package control.invest.IC.investiment.service;

import control.invest.IC.investiment.DTO.AtivoDTO;
import control.invest.IC.investiment.model.AcaoModel;
import control.invest.IC.investiment.repository.AcaoRepository;
import control.invest.IC.irfp.models.ContribuinteModel;
import control.invest.IC.irfp.repositories.ContribuinteRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.DecimalFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

public class CalculoAcaoService {
    @Autowired
    ContribuinteRepository contribuinteRepository;
    @Autowired
    AtivoService ativoService;
    @Autowired
    AcaoRepository acaoRepository;

    private Double formatNumber(Double number) {
        DecimalFormat df = new DecimalFormat("0.##");
        return Double.parseDouble(df.format(number).replace(".", "").replace(",", "."));
    }

    public LinkedHashMap<String, Object> calcularAcao(ContribuinteModel contribuinte, String siglaAcao) {
        Optional<AcaoModel> result = acaoRepository.findBySiglaAndContribuinteId(siglaAcao, contribuinte.getId());

        AcaoModel acao = result.get();
        AtivoDTO acaoAtual = ativoService.buscarAtivo(siglaAcao);

        double precoMedio = acao.getTotalValor() / acao.getCotas();
        double saldoTotal = acao.getCotas() * acaoAtual.getPrecoAtual();
        double precoAtual = acaoAtual.getPrecoAtual();
        double variacao = ((precoAtual - precoMedio) / precoMedio) * 100;
        LinkedHashMap<String, Object> acaoMap = new LinkedHashMap<>();

        acaoMap.put("Ativo", acao.getSigla());
        acaoMap.put("Cotas", acao.getCotas());
        acaoMap.put("Preço médio", formatNumber(precoMedio));
        acaoMap.put("Preço atual", formatNumber(precoAtual));
        acaoMap.put("Saldo total", formatNumber(saldoTotal));
        acaoMap.put("Variação", formatNumber(variacao));

        return acaoMap;
    }

    public LinkedHashMap<String, Object> calcularCarteiraStock(ContribuinteModel contribuinte) {
        List<AcaoModel> stocks = acaoRepository.findByContribuinteId(contribuinte.getId());
        double precoMedioCarteira = 0d, precoAtualCarteira = 0d, saldoTotalCarteira = 0d, variacaoCarteira = 0d;
        double saldoRealAplicado = 0d;
        LinkedHashMap<String, Object> stockList = new LinkedHashMap<>();
        for (int i = 0; i < stocks.size(); i++) {
            AcaoModel acao = stocks.get(i);
            AtivoDTO ativoAtual = ativoService.buscarAtivo(acao.getSigla());
            double precoAtual = ativoAtual.getPrecoAtual();
            double precoMedio = acao.getTotalValor() / acao.getCotas();
            double saldoTotal = acao.getCotas() * ativoAtual.getPrecoAtual();
            double variacao = ((precoAtual - precoMedio) / precoMedio) * 100;
            precoAtualCarteira += precoAtual;
            precoMedioCarteira += precoMedio;
            precoAtualCarteira += acao.getTotalValor();

            LinkedHashMap<String, Object> stockMap = new LinkedHashMap<>();
            stockMap.put("Cotas", acao.getCotas());
            stockMap.put("Preço médio", formatNumber(precoMedio));
            stockMap.put("Preço atual", formatNumber(precoAtual));
            stockMap.put("Saldo total", formatNumber(saldoTotal));
            stockMap.put("Variação", formatNumber(variacao));
            stockList.put(acao.getSigla(), stockMap);
        }

        variacaoCarteira = ((saldoTotalCarteira - saldoRealAplicado) / saldoRealAplicado) * 100;
        stockList.put("Variação total", formatNumber(variacaoCarteira));
        stockList.put("Saldo atual carteira", formatNumber(saldoTotalCarteira));
        stockList.put("Saldo real aplicado", formatNumber(saldoRealAplicado));
        return stockList;
    }

}
