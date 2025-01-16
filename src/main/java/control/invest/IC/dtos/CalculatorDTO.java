package control.invest.IC.dtos;

import control.invest.IC.models.ContribuinteModel;
import control.invest.IC.models.DependenteModel;
import control.invest.IC.models.IrpfModel;

public class CalculatorDTO {

    private IrpfModel irpfModel;
    private ContribuinteModel contribuinteModel;
    private DependenteModel dependenteModel;

    private int dependente;

    private double pagamento;//todos os pagamentos do usuario

    public ContribuinteModel getContribuinteModel() {
        return contribuinteModel;
    }

    public void setContribuinteModel(ContribuinteModel contribuinteModel) {
        this.contribuinteModel = contribuinteModel;
    }

    public DependenteModel getDependenteModel() {
        return dependenteModel;
    }

    public void setDependenteModel(DependenteModel dependenteModel) {
        this.dependenteModel = dependenteModel;
    }

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
