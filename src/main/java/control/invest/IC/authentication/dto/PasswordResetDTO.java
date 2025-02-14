package control.invest.IC.authentication.dto;

public class PasswordResetDTO {
    private String senhaAtual;
    private String senhaNova;
    private String senhaNovaConfirmacao;

    public String getSenhaAtual() {
        return senhaAtual;
    }

    public void setSenhaAtual(String senhaAtual) {
        this.senhaAtual = senhaAtual;
    }

    public String getSenhaNova() {
        return senhaNova;
    }

    public void setSenhaNova(String senhaNova) {
        this.senhaNova = senhaNova;
    }

    public String getSenhaNovaConfirmacao() {
        return senhaNovaConfirmacao;
    }

    public void setSenhaNovaConfirmacao(String senhaNovaConfirmacao) {
        this.senhaNovaConfirmacao = senhaNovaConfirmacao;
    }
}
