package control.invest.IC.investiment.DTO;

public class TransacaoDTO {

    private String sigla;
    private int cotas;
    private String tipo;
    private double valorCota;
    private AtivoDTO ativoDTO;

    public AtivoDTO getAtivoDTO() {
        return ativoDTO;
    }

    public double getValorCota() {
        return valorCota;
    }

    public void setValorCota(double valorCota) {
        this.valorCota = valorCota;
    }

    public void setAtivoDTO(AtivoDTO ativoDTO) {
        this.ativoDTO = ativoDTO;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    public int getCotas() {
        return cotas;
    }

    public void setCotas(int cotas) {
        this.cotas = cotas;
    }
}
