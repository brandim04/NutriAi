package model;

public class Paciente extends Usuario {

    private int idPaciente;

    public Paciente() {
        super();
    }

    public Paciente(int idPaciente, int idUsuario, String nome, String cpf, String telefone, String email, String senha) {
        super(idUsuario, nome, cpf, telefone, email, senha);
        this.idPaciente = idPaciente;
    }

    public Paciente(int idUsuario, String nome, String cpf, String telefone, String email, String senha) {
        super(idUsuario, nome, cpf, telefone, email, senha);
    }

    public int getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(int idPaciente) {
        this.idPaciente = idPaciente;
    }
}