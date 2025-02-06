package control.invest.IC.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
@Table(name = "tb_pagamentos")
public class PagamentoModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String identificacao;//recebe cpf e cnpj
    private String nome;
    @ManyToOne
    @JoinColumn(name = "contribuinte_id", referencedColumnName = "id")
    @JsonBackReference
    private ContribuinteModel contribuinte;

    private double valor;
}
