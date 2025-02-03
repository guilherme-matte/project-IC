package control.invest.IC.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;

@Entity
@Table(name = "tb_irpf")
public class IrpfModel extends RepresentationModel<IrpfModel> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idIrpf;

    @ManyToOne
    @JoinColumn(name = "contribuinte_id", referencedColumnName = "id")
    @JsonBackReference
    private ContribuinteModel contribuinteModel;

    private String fontePagadoraCnpj;
    private String fontePagadoraNomeEmpresa;

    //rendimentos tributaveis
    private double rendimentosTotais;
    private double prevSocial;
    private double fapi;
    private double pensao;
    private double impostoRetido;

    // rendimentos isentos
    private double parcelaIsentaApos;
    private double parcelaIsentaDecTerc;
    private double ajudaCusto;
    private double acidenteTrabalho;
    private double lucroDividendo;
    private double pagamentosRecebidos;//Pagamento realizado ao titular ou socio de micro empresa
    private double rescisao;
    private double jurosMora;
    private double outrosRendimentosIsentos;


    // rendimentos exclusivos
    private double decTercSal;
    private double impRetDecTerc;
    private double outrosRendExclusivo;


    //rendimentos acumulados
    private double totalRendTributavel;
    private double despesaAcaoJud;
    private double contribPrevSocial;
    private double pensaoRecebida;
    private double impostoRetidoRendRec;
    private double rendIsentos;

    //pagamento dedutiveis
    private String cnpjEmpresaPagDedutivel;
    private String nomeEmpresaPagDedutivel;
    private double valorEmpresaPagDedutivel;


    public String getFontePagadoraCnpj() {
        return fontePagadoraCnpj;
    }

    public void setFontePagadoraCnpj(String fontePagadoraCnpj) {
        this.fontePagadoraCnpj = fontePagadoraCnpj;
    }

    public String getFontePagadoraNomeEmpresa() {
        return fontePagadoraNomeEmpresa;
    }

    public void setFontePagadoraNomeEmpresa(String fontePagadoraNomeEmpresa) {
        this.fontePagadoraNomeEmpresa = fontePagadoraNomeEmpresa;
    }

    public double getRendimentosTotais() {
        return rendimentosTotais;
    }

    public void setRendimentosTotais(double rendimentosTotais) {
        this.rendimentosTotais = rendimentosTotais;
    }

    public double getPrevSocial() {
        return prevSocial;
    }

    public void setPrevSocial(double prevSocial) {
        this.prevSocial = prevSocial;
    }

    public double getFapi() {
        return fapi;
    }

    public void setFapi(double fapi) {
        this.fapi = fapi;
    }

    public double getPensao() {
        return pensao;
    }

    public void setPensao(double pensao) {
        this.pensao = pensao;
    }

    public double getImpostoRetido() {
        return impostoRetido;
    }

    public void setImpostoRetido(double impostoRetido) {
        this.impostoRetido = impostoRetido;
    }

    public double getParcelaIsentaApos() {
        return parcelaIsentaApos;
    }

    public void setParcelaIsentaApos(double parcelaIsentaApos) {
        this.parcelaIsentaApos = parcelaIsentaApos;
    }

    public double getParcelaIsentaDecTerc() {
        return parcelaIsentaDecTerc;
    }

    public void setParcelaIsentaDecTerc(double parcelaIsentaDecTerc) {
        this.parcelaIsentaDecTerc = parcelaIsentaDecTerc;
    }

    public double getAjudaCusto() {
        return ajudaCusto;
    }

    public void setAjudaCusto(double ajudaCusto) {
        this.ajudaCusto = ajudaCusto;
    }

    public double getAcidenteTrabalho() {
        return acidenteTrabalho;
    }

    public void setAcidenteTrabalho(double acidenteTrabalho) {
        this.acidenteTrabalho = acidenteTrabalho;
    }

    public double getLucroDividendo() {
        return lucroDividendo;
    }

    public void setLucroDividendo(double lucroDividendo) {
        this.lucroDividendo = lucroDividendo;
    }

    public double getPagamentosRecebidos() {
        return pagamentosRecebidos;
    }

    public void setPagamentosRecebidos(double pagamentosRecebidos) {
        this.pagamentosRecebidos = pagamentosRecebidos;
    }

    public double getRescisao() {
        return rescisao;
    }

    public void setRescisao(double rescisao) {
        this.rescisao = rescisao;
    }

    public double getJurosMora() {
        return jurosMora;
    }

    public void setJurosMora(double jurosMora) {
        this.jurosMora = jurosMora;
    }

    public double getOutrosRendimentosIsentos() {
        return outrosRendimentosIsentos;
    }

    public void setOutrosRendimentosIsentos(double outrosRendimentosIsentos) {
        this.outrosRendimentosIsentos = outrosRendimentosIsentos;
    }

    public double getDecTercSal() {
        return decTercSal;
    }

    public void setDecTercSal(double decTercSal) {
        this.decTercSal = decTercSal;
    }

    public double getImpRetDecTerc() {
        return impRetDecTerc;
    }

    public void setImpRetDecTerc(double impRetDecTerc) {
        this.impRetDecTerc = impRetDecTerc;
    }

    public double getOutrosRendExclusivo() {
        return outrosRendExclusivo;
    }

    public void setOutrosRendExclusivo(double outrosRendExclusivo) {
        this.outrosRendExclusivo = outrosRendExclusivo;
    }

    public double getTotalRendTributavel() {
        return totalRendTributavel;
    }

    public void setTotalRendTributavel(double totalRendTributavel) {
        this.totalRendTributavel = totalRendTributavel;
    }

    public double getDespesaAcaoJud() {
        return despesaAcaoJud;
    }

    public void setDespesaAcaoJud(double despesaAcaoJud) {
        this.despesaAcaoJud = despesaAcaoJud;
    }

    public double getContribPrevSocial() {
        return contribPrevSocial;
    }

    public void setContribPrevSocial(double contribPrevSocial) {
        this.contribPrevSocial = contribPrevSocial;
    }

    public double getPensaoRecebida() {
        return pensaoRecebida;
    }

    public void setPensaoRecebida(double pensaoRecebida) {
        this.pensaoRecebida = pensaoRecebida;
    }

    public double getImpostoRetidoRendRec() {
        return impostoRetidoRendRec;
    }

    public void setImpostoRetidoRendRec(double impostoRetidoRendRec) {
        this.impostoRetidoRendRec = impostoRetidoRendRec;
    }

    public double getRendIsentos() {
        return rendIsentos;
    }

    public void setRendIsentos(double rendIsentos) {
        this.rendIsentos = rendIsentos;
    }

    public String getCnpjEmpresaPagDedutivel() {
        return cnpjEmpresaPagDedutivel;
    }

    public void setCnpjEmpresaPagDedutivel(String cnpjEmpresaPagDedutivel) {
        this.cnpjEmpresaPagDedutivel = cnpjEmpresaPagDedutivel;
    }

    public String getNomeEmpresaPagDedutivel() {
        return nomeEmpresaPagDedutivel;
    }

    public void setNomeEmpresaPagDedutivel(String nomeEmpresaPagDedutivel) {
        this.nomeEmpresaPagDedutivel = nomeEmpresaPagDedutivel;
    }

    public double getValorEmpresaPagDedutivel() {
        return valorEmpresaPagDedutivel;
    }

    public void setValorEmpresaPagDedutivel(double valorEmpresaPagDedutivel) {
        this.valorEmpresaPagDedutivel = valorEmpresaPagDedutivel;
    }

    public Long getIdIrpf() {
        return idIrpf;
    }

    public void setIdIrpf(Long idIrpf) {
        this.idIrpf = idIrpf;
    }

    public ContribuinteModel getContribuinteModel() {
        return contribuinteModel;
    }

    public void setContribuinteModel(ContribuinteModel contribuinteModel) {
        this.contribuinteModel = contribuinteModel;
    }
}
