package model;


public class Receitas {
    private int id;
    private int nutricionistaId; // Referencia Nutricionista.id (o PK da tabela nutricionistas)
    private String titulo; // Atualizado de 'nome' para 'titulo'

    public Receitas() {}

    public Receitas(int id, int nutricionistaId, String titulo) {
        this.id = id;
        this.nutricionistaId = nutricionistaId;
        this.titulo = titulo;
    }

    public Receitas(int nutricionistaId, String titulo) {
        this.nutricionistaId = nutricionistaId;
        this.titulo = titulo;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getNutricionistaId() { return nutricionistaId; }
    public void setNutricionistaId(int nutricionistaId) { this.nutricionistaId = nutricionistaId; }
    public String getTitulo() { return titulo; } 
    public void setTitulo(String titulo) { this.titulo = titulo; } 
}