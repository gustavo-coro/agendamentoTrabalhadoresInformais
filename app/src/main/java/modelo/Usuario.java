package modelo;

import java.util.Date;

public class Usuario {

    private int id;
    private String nome;
    private String endereco;
    private Date dataNasc;
    private String email;
    private String telefone;
    private String cpf;
    private String descricao;
    private String senha;

    public Usuario(String nome, String endereco, Date dataNasc, String email, String telefone, String cpf, String descricao, String senha) {
        this.nome = nome;
        this.endereco = endereco;
        this.dataNasc = dataNasc;
        this.email = email;
        this.telefone = telefone;
        this.cpf = cpf;
        this.descricao = descricao;
        this.senha = senha;
    }

    public Usuario(int id, String nome, String endereco, Date dataNasc, String email, String telefone, String cpf, String descricao, String senha) {
        this.id = id;
        this.nome = nome;
        this.endereco = endereco;
        this.dataNasc = dataNasc;
        this.email = email;
        this.telefone = telefone;
        this.cpf = cpf;
        this.descricao = descricao;
        this.senha = senha;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public Date getDataNasc() {
        return dataNasc;
    }

    public void setDataNasc(Date dataNasc) {
        this.dataNasc = dataNasc;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
