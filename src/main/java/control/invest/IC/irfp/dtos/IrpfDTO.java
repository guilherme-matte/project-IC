package control.invest.IC.irfp.dtos;

public class IrpfDTO {
    private Double rendimentos;
    private Double deducoes;
    private Double impostoRetido;

    public Double getImpostoRetido() {
        return impostoRetido;
    }

    public void setImpostoRetido(Double impostoRetido) {
        this.impostoRetido = impostoRetido;
    }

    public Double getRendimentos() {
        return rendimentos;
    }

    public void setRendimentos(Double rendimentos) {
        this.rendimentos = rendimentos;
    }

    public Double getDeducoes() {
        return deducoes;
    }

    public void setDeducoes(Double deducoes) {
        this.deducoes = deducoes;
    }
}
