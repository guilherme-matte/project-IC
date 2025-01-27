package control.invest.IC.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
@Table(name = "tb_dependente")
public class DependenteModel {

    @Id

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String cpf;

    private String nome;
    @ManyToOne
    @JoinColumn(name = "contribuinte_id", referencedColumnName = "id")
    @JsonBackReference
    private ContribuinteModel contribuinte;

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

    public ContribuinteModel getContribuinte() {
        return contribuinte;
    }

    public void setContribuinte(ContribuinteModel contribuinte) {
        this.contribuinte = contribuinte;
    }
}
