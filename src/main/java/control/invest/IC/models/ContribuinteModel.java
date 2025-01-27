package control.invest.IC.models;

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
