package control.invest.IC.irfp.dtos;

import control.invest.IC.irfp.models.ContribuinteModel;
import control.invest.IC.irfp.models.DependenteModel;
import control.invest.IC.irfp.models.IrpfModel;

public class DadosRequestDTO {

    private IrpfModel irpfModel;
    private ContribuinteModel contribuinteModel;
    private DependenteModel dependenteModel;


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

    public double getPagamento() {
        return pagamento;
    }

    public void setPagamento(double pagamento) {
        this.pagamento = pagamento;
    }
}
