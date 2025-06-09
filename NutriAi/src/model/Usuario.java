package model;

import java.time.LocalDate;

public class Usuario {
    private int id; // Adicionado id
    private String email;
    private String senha;
    private String nome;
    private String cpf;
    private String telefone;
    private LocalDate dataNascimento;

    // Construtor para login (sem id e outros detalhes completos)
    public Usuario(String email, String senha) {
        this.email = email;
        this.senha = senha;
    }

    // Construtor completo para cadastro (sem id, pois o banco gerará)
    public Usuario(String nome, String cpf, LocalDate dataNascimento, String telefone, String email, String senha) {
        this.nome = nome;
        this.cpf = cpf;
        this.dataNascimento = dataNascimento;
        this.telefone = telefone;
        this.email = email;
        this.senha = senha;
    }

    // Construtor completo com ID (para quando buscar do banco)
    public Usuario(int id, String nome, String cpf, LocalDate dataNascimento, String telefone, String email, String senha) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.dataNascimento = dataNascimento;
        this.telefone = telefone;
        this.email = email;
        this.senha = senha;
    }
    
    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public LocalDate getDataNascimento() { return dataNascimento; }
    public void setDataNascimento(LocalDate dataNascimento) { this.dataNascimento = dataNascimento; }
}