package control.invest.IC.investiment.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import control.invest.IC.config.BrapiConfig;
import control.invest.IC.investiment.DTO.AtivoDTO;
import control.invest.IC.investiment.DTO.BrapiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AtivoService {
    private final RestTemplate restTemplate;
    @Autowired
    private BrapiConfig token;
    private final ObjectMapper objectMapper;

    public AtivoService() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    public AtivoDTO buscarAtivo(String sigla) {
        String url = "https://brapi.dev/api/quote/" + sigla + "?token=" + token.getBrapiToken();

        System.out.println("Consultando: " + url);

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        System.out.println(response.getBody());
        try {
            BrapiResponse brapiResponse = objectMapper.readValue(response.getBody(), BrapiResponse.class);

            return (brapiResponse.getResults() != null && !brapiResponse.getResults().isEmpty())
                    ? brapiResponse.getResults().get(0)
                    : null;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao converter resposta da API", e);
        }
    }
}
