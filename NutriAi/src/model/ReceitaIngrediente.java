package model;

public class ReceitaIngrediente {
    private int receitaId;
    private int ingredienteId;
    private String quantidade;

    public ReceitaIngrediente() {}

    public ReceitaIngrediente(int receitaId, int ingredienteId, String quantidade) {
        this.receitaId = receitaId;
        this.ingredienteId = ingredienteId;
        this.quantidade = quantidade;
    }

    public int getReceitaId() { return receitaId; }
    public void setReceitaId(int receitaId) { this.receitaId = receitaId; }
    public int getIngredienteId() { return ingredienteId; }
    public void setIngredienteId(int ingredienteId) { this.ingredienteId = ingredienteId; }
    public String getQuantidade() { return quantidade; }
    public void setQuantidade(String quantidade) { this.quantidade = quantidade; }
}