package control.invest.IC.investiment.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AtivoDTO {
    @JsonProperty("symbol")
    private String sigla;

    @JsonProperty("shortName")
    private String nomeCurto;

    @JsonProperty("longName")
    private String nomeCompleto;
    @JsonProperty("regularMarketPrice")
    private Double precoAtual;

    @JsonProperty("regularMarketDayHigh")
    private Double maximoDia;

    @JsonProperty("regularMarketDayLow")
    private Double minimoDia;

    @JsonProperty("fiftyTwoWeekRange")
    private String faixa52Semanas;

    @JsonProperty("logourl")
    private String logoUrl;

    public String getSigla() {
        return sigla;
    }

    public String getNomeCurto() {
        return nomeCurto;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public void setNomeCurto(String nomeCurto) {
        this.nomeCurto = nomeCurto;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }


    public Double getPrecoAtual() {
        return precoAtual;
    }

    public void setPrecoAtual(Double precoAtual) {
        this.precoAtual = precoAtual;
    }

    public Double getMaximoDia() {
        return maximoDia;
    }

    public void setMaximoDia(Double maximoDia) {
        this.maximoDia = maximoDia;
    }

    public Double getMinimoDia() {
        return minimoDia;
    }

    public void setMinimoDia(Double minimoDia) {
        this.minimoDia = minimoDia;
    }

    public String getFaixa52Semanas() {
        return faixa52Semanas;
    }

    public void setFaixa52Semanas(String faixa52Semanas) {
        this.faixa52Semanas = faixa52Semanas;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }
}
