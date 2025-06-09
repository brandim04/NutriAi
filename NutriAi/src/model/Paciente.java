package model;

import java.time.LocalDate;

public class Paciente extends Usuario {
    private int idPaciente; // ID próprio do paciente na tabela 'pacientes'

    // Construtor completo para quando busca do banco
    // Observar a ordem e os tipos: idUsuario (int), nome, cpf, dataNascimento (LocalDate), etc.
    public Paciente(int idUsuario, String nome, String cpf, LocalDate dataNascimento, String telefone, String email, String senha, int idPaciente) {
        super(idUsuario, nome, cpf, dataNascimento, telefone, email, senha); // Passa para o construtor de Usuario
        this.idPaciente = idPaciente;
    }

    // Construtor para criar um NOVO Paciente (sem ID do banco, mas com ID do Usuario será gerado)
    public Paciente(String nome, String cpf, LocalDate dataNascimento, String telefone, String email, String senha) {
        super(nome, cpf, dataNascimento, telefone, email, senha); // Passa para o construtor de Usuario sem ID
    }

    public int getIdPaciente() { return idPaciente; }
    public void setIdPaciente(int idPaciente) { this.idPaciente = idPaciente; }

}