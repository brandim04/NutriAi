package model;

public class ModoDePreparo {
    private int id;
    private int receitaId; 
    private String passos; 

    public ModoDePreparo() {}

    public ModoDePreparo(int id, int receitaId, String passos) {
        this.id = id;
        this.receitaId = receitaId;
        this.passos = passos;
    }

    public ModoDePreparo(int receitaId, String passos) {
        this.receitaId = receitaId;
        this.passos = passos;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getReceitaId() { return receitaId; }
    public void setReceitaId(int receitaId) { this.receitaId = receitaId; }
    public String getPassos() { return passos; }
    public void setPassos(String passos) { this.passos = passos; }
}