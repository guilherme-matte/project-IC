package control.invest.IC.investiment.service;

import control.invest.IC.config.BrapiConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AtivoService {
    private RestTemplate restTemplate;
    @Autowired
    private BrapiConfig token;


    public String buscarAtivo(String sigla) {
        String url = "https://brapi.dev/api/quote/" + sigla + "?token=" + token.getBrapiToken();
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        return response.getBody();
    }
}
