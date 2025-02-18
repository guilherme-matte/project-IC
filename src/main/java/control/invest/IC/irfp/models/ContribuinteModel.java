package control.invest.IC.irfp.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "tb_contribuinte")
public class ContribuinteModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String cpf;
    private String nome;


    @OneToMany(mappedBy = "contribuinte", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    private List<DependenteModel> dependentes;
    @OneToMany(mappedBy = "contribuinte", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PagamentoModel> pagamentos;
    @OneToMany(mappedBy = "contribuinte", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<IrpfModel> irpf;

    public List<IrpfModel> getIrpf() {
        return irpf;
    }

    public void setIrpf(List<IrpfModel> irpf) {
        this.irpf = irpf;
    }

    public List<PagamentoModel> getPagamentos() {
        return pagamentos;
    }

    public void setPagamentos(List<PagamentoModel> pagamentos) {
        this.pagamentos = pagamentos;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<DependenteModel> getDependentes() {
        return dependentes;
    }

    public void setDependentes(List<DependenteModel> dependentes) {
        this.dependentes = dependentes;
    }
}
