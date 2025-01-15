package control.invest.IC.models;

import jakarta.persistence.*;

@Entity
public class ContribuinteModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column (unique = true, nullable = false)
    private String cpf;
    private String nome;
}
