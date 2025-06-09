package model;

import java.time.LocalDate;

public class Nutricionista extends Usuario {
    private int idNutricionista; // ID próprio do nutricionista na tabela 'nutricionistas'
    private String crn; // Campo CRN continua

    // Construtor completo (para quando o Nutricionista já tem um ID do banco)
    // Este construtor terá 9 parâmetros no total: 7 do Usuario + 2 próprios do Nutricionista (idNutricionista, crn)
    public Nutricionista(int idUsuario, String nome, String cpf, LocalDate dataNascimento, String telefone, String email, String senha, int idNutricionista, String crn) {
        super(idUsuario, nome, cpf, dataNascimento, telefone, email, senha); // Chama o construtor de Usuario com ID
        this.idNutricionista = idNutricionista;
        this.crn = crn;
    }

    // Construtor para criar um NOVO Nutricionista (sem ID inicial do banco para Nutricionista)
    // Este construtor terá 8 parâmetros no total: 6 do Usuario + 2 próprios do Nutricionista (crn)
    public Nutricionista(String nome, String cpf, LocalDate dataNascimento, String telefone, String email, String senha, String crn) {
        super(nome, cpf, dataNascimento, telefone, email, senha); // Chama o construtor de Usuario sem ID
        this.crn = crn;
    }

    // --- GETTERS E SETTERS ESPECÍFICOS DE NUTRICIONISTA ---
    public int getIdNutricionista() { return idNutricionista; }
    public void setIdNutricionista(int idNutricionista) { this.idNutricionista = idNutricionista; }

    public String getCrn() { return crn; }
    public void setCrn(String crn) { this.crn = crn; }

    // Os métodos getEspecialidade() e setEspecialidade() devem ser REMOVIDOS daqui
}