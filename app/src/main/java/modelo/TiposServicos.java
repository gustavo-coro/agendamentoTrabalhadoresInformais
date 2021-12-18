package modelo;

public class TiposServicos {

    private int id;
    private int idAdm;
    private String nomeTipoServico;

    public TiposServicos(int id, int idAdm, String nomeTipoServico) {
        this.id = id;
        this.idAdm = idAdm;
        this.nomeTipoServico = nomeTipoServico;
    }

    public TiposServicos() {
    }

    public TiposServicos(String nomeTipoServico) {
        this.nomeTipoServico = nomeTipoServico;
    }

    public TiposServicos(int idAdm, String nomeTipoServico) {
        this.idAdm = idAdm;
        this.nomeTipoServico = nomeTipoServico;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdAdm() {
        return idAdm;
    }

    public void setIdAdm(int idAdm) {
        this.idAdm = idAdm;
    }

    public String getNomeTipoServico() {
        return nomeTipoServico;
    }

    public void setNomeTipoServico(String nomeTipoServico) {
        this.nomeTipoServico = nomeTipoServico;
    }
}
