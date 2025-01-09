package control.invest.IC.dtos;

import control.invest.IC.models.IrpfModel;

public class CalculatorDTO {

private IrpfModel irpfModel;

private int dependente;

private double pagamento;//todos os pagamentos do usuario

    public IrpfModel getIrpfModel() {
        return irpfModel;
    }

    public void setIrpfModel(IrpfModel irpfModel) {
        this.irpfModel = irpfModel;
    }

    public int getDependente() {
        return dependente;
    }

    public void setDependente(int dependente) {
        this.dependente = dependente;
    }

    public double getPagamento() {
        return pagamento;
    }

    public void setPagamento(double pagamento) {
        this.pagamento = pagamento;
    }
}
