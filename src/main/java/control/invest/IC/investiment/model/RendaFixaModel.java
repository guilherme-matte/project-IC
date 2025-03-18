package control.invest.IC.investiment.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import control.invest.IC.irfp.models.ContribuinteModel;
import jakarta.persistence.*;

@Entity
@Table(name = "tb_rendaFixa")
public class RendaFixaModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "contribuinte_id", referencedColumnName = "id")
    @JsonBackReference
    private ContribuinteModel contribuinte;

    private String emissor;
    private double saldo;
    private double rendimento;
    private double cdi;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ContribuinteModel getContribuinte() {
        return contribuinte;
    }

    public void setContribuinte(ContribuinteModel contribuinte) {
        this.contribuinte = contribuinte;
    }

    public String getEmissor() {
        return emissor;
    }

    public void setEmissor(String emissor) {
        this.emissor = emissor;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public double getRendimento() {
        return rendimento;
    }

    public void setRendimento(double rendimento) {
        this.rendimento = rendimento;
    }

    public double getCdi() {
        return cdi;
    }

    public void setCdi(double cdi) {
        this.cdi = cdi;
    }
}
