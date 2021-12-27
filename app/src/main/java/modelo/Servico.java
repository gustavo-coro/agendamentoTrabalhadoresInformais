package modelo;

public class Servico {

    private int id;
    private String nome;
    private Float preco;
    private int idTiposServicos;
    private int idAdm;

    public Servico(int id, String nome, Float preco, int idTiposServicos, int idAdm) {
        this.id = id;
        this.nome = nome;
        this.preco = preco;
        this.idTiposServicos = idTiposServicos;
        this.idAdm = idAdm;
    }

    public Servico(String nome, Float preco, int idTiposServicos, int idAdm) {
        this.nome = nome;
        this.preco = preco;
        this.idTiposServicos = idTiposServicos;
        this.idAdm = idAdm;
    }

    public Servico() {
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

    public Float getPreco() {
        return preco;
    }

    public void setPreco(Float preco) {
        this.preco = preco;
    }

    public int getIdTiposServicos() {
        return idTiposServicos;
    }

    public void setIdTiposServicos(int idTiposServicos) {
        this.idTiposServicos = idTiposServicos;
    }

    public int getIdAdm() {
        return idAdm;
    }

    public void setIdAdm(int idAdm) {
        this.idAdm = idAdm;
    }


}
