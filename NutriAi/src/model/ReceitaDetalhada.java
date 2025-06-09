package model;

/**
 *
 * @author brand
 */
public class ReceitaDetalhada {
    private int id;
    private String titulo;
    private String tipoRefeicao; // Adicionado para exibir na tela, se tiver no banco ou for fixo
    private String ingredientes; // String que conterá todos os ingredientes formatados
    private String modoPreparo;  // String que conterá todos os passos do preparo

    public ReceitaDetalhada(int id, String titulo, String tipoRefeicao, String ingredientes, String modoPreparo) {
        this.id = id;
        this.titulo = titulo;
        this.tipoRefeicao = tipoRefeicao;
        this.ingredientes = ingredientes;
        this.modoPreparo = modoPreparo;
    }

    // Getters
    public int getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getTipoRefeicao() { return tipoRefeicao; }
    public String getIngredientes() { return ingredientes; }
    public String getModoPreparo() { return modoPreparo; }

    // Setters (se você precisar alterar os dados após a criação do objeto)
    public void setId(int id) { this.id = id; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public void setTipoRefeicao(String tipoRefeicao) { this.tipoRefeicao = tipoRefeicao; }
    public void setIngredientes(String ingredientes) { this.ingredientes = ingredientes; }
    public void setModoPreparo(String modoPreparo) { this.modoPreparo = modoPreparo; }
}
