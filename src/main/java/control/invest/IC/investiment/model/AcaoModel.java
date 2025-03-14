package control.invest.IC.investiment.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import control.invest.IC.irfp.models.ContribuinteModel;
import jakarta.persistence.*;

@Entity
@Table(name = "tb_acoes")
public class AcaoModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "contribuinte_id", referencedColumnName = "id")
    @JsonBackReference
    private ContribuinteModel contribuinte;

    private String sigla;
    private int cotas;
    private Double totalValor;

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

    public Double getTotalValor() {
        return totalValor;
    }

    public void setTotalValor(Double totalValor) {
        this.totalValor = totalValor;
    }

}
