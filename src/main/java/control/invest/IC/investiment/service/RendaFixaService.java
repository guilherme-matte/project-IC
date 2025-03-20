package control.invest.IC.investiment.service;

import control.invest.IC.investiment.model.AcaoModel;
import control.invest.IC.investiment.model.RendaFixaModel;
import control.invest.IC.investiment.repository.RendaFixaRepository;
import control.invest.IC.irfp.models.ContribuinteModel;
import control.invest.IC.irfp.repositories.ContribuinteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

@Service
public class RendaFixaService {
    @Autowired
    private ContribuinteRepository contribuinteRepository;
    @Autowired
    private RendaFixaRepository rendaFixaRepository;

    public void rotinaAtualizarRendaFixa() {

        List<RendaFixaModel> rendaFixaList = rendaFixaRepository.findAll();
        int quantidade = 0;
        Long inicio = System.currentTimeMillis();
        for (int i = 0; i < rendaFixaList.size(); i++) {
            RendaFixaModel rendaFixa = rendaFixaList.get(i);

            double cdi = 13.25 * (rendaFixa.getCdi() / 100);

            rendaFixa.setRendimento(rendaFixa.getRendimento() + ((rendaFixa.getSaldo() * (cdi / 100)) / 220));

            rendaFixa.setImposto(rendaFixa.getRendimento() * 0.225);//para fazer: calcular o imposto referente a data

            rendaFixa.setSaldo(rendaFixa.getSaldo() + rendaFixa.getRendimento());

            rendaFixaRepository.save(rendaFixa);

            quantidade++;

        }
        long fim = System.currentTimeMillis();
        long tempo = fim - inicio;
        System.out.println("Tempo de execução: " + tempo);
        System.out.println(quantidade + " indices atualizados");
    }

    public LinkedHashMap<String, Object> getRendaFixa(ContribuinteModel contribuinte) {
        List<RendaFixaModel> rfList = rendaFixaRepository.findByContribuinteId(contribuinte.getId());
        LinkedHashMap<String, Object> rendaFixaMap = new LinkedHashMap<>();

        for (int i = 0; i < rfList.size(); i++) {
            RendaFixaModel rendaFixa = rfList.get(i);
            rendaFixaMap.put("Emissor", rendaFixa.getEmissor());
            rendaFixaMap.put("Saldo", rendaFixa.getSaldo());
            rendaFixaMap.put("CDI", rendaFixa.getCdi());
            rendaFixaMap.put("Rendimento", rendaFixa.getRendimento());
            rendaFixaMap.put("Data", rendaFixa.getData());
            rendaFixaMap.put("imposto", rendaFixa.getImposto());

        }
        return rendaFixaMap;
    }
}
