package control.invest.IC.investiment.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BrapiResponse {
    private List<AtivoDTO> results;

    public List<AtivoDTO> getResults() {
        return results;
    }

    public void setResults(List<AtivoDTO> results) {
        this.results = results;
    }
}
