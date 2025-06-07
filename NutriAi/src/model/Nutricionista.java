package model;


/**
 *
 * @author brand
 */

public class Nutricionista extends Usuario {

    private int idNutricionista;
    private String crn;
    private String especialidade;

    public Nutricionista() {
        super();
    }

    public Nutricionista(int idNutricionista, String crn, String especialidade,
                         int idUsuario, String nome, String cpf, String telefone, String email, String senha) {
        super(idUsuario, nome, cpf, telefone, email, senha);
        this.idNutricionista = idNutricionista;
        this.crn = crn;
        this.especialidade = especialidade;
    }

    public Nutricionista(String crn, String especialidade,
                         int idUsuario, String nome, String cpf, String telefone, String email, String senha) {
        super(idUsuario, nome, cpf, telefone, email, senha);
        this.crn = crn;
        this.especialidade = especialidade;
    }

    public int getIdNutricionista() {
        return idNutricionista;
    }

    public void setIdNutricionista(int idNutricionista) {
        this.idNutricionista = idNutricionista;
    }

    public String getCrn() {
        return crn;
    }

    public void setCrn(String crn) {
        this.crn = crn;
    }

    public String getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(String especialidade) {
        this.especialidade = especialidade;
    }
}