package model;

public class QuestionarioRespostas {
    private int id; 
    private int pacienteId; //

    private int pergunta1; // Seu diagnostico principal?
    private int pergunta2; // Há quanto tempo tem esse diagnostico?
    private int pergunta3; // Qual tratamento vc ta fazendo ultimamente?
    private String pergunta4; // Medicamentos de uso continuo? (Resposta escrita)
    private int pergunta5; // Principais sintomas que você apresenta?
    private int pergunta6; // Você possui alergias ou intolerâncias alimentares?
    private int pergunta7; // Você faz monitoramento de glicose?
    private int pergunta8; // Principal objetivo com o plano alimentar?

    public QuestionarioRespostas() {
        
    }

    public QuestionarioRespostas(int id, int pacienteId,
                                int pergunta1, int pergunta2, int pergunta3, String pergunta4,
                                int pergunta5, int pergunta6, int pergunta7, int pergunta8) {
        this.id = id;
        this.pacienteId = pacienteId;
        this.pergunta1 = pergunta1;
        this.pergunta2 = pergunta2;
        this.pergunta3 = pergunta3;
        this.pergunta4 = pergunta4;
        this.pergunta5 = pergunta5;
        this.pergunta6 = pergunta6;
        this.pergunta7 = pergunta7;
        this.pergunta8 = pergunta8;
    }

    public QuestionarioRespostas(int pacienteId,
                                int pergunta1, int pergunta2, int pergunta3, String pergunta4,
                                int pergunta5, int pergunta6, int pergunta7, int pergunta8) {
        this.pacienteId = pacienteId;
        this.pergunta1 = pergunta1;
        this.pergunta2 = pergunta2;
        this.pergunta3 = pergunta3;
        this.pergunta4 = pergunta4;
        this.pergunta5 = pergunta5;
        this.pergunta6 = pergunta6;
        this.pergunta7 = pergunta7;
        this.pergunta8 = pergunta8;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPacienteId() {
        return pacienteId;
    }

    public void setPacienteId(int pacienteId) {
        this.pacienteId = pacienteId;
    }

    public int getPergunta1() {
        return pergunta1;
    }

    public void setPergunta1(int pergunta1) {
        this.pergunta1 = pergunta1;
    }

    public int getPergunta2() {
        return pergunta2;
    }

    public void setPergunta2(int pergunta2) {
        this.pergunta2 = pergunta2;
    }

    public int getPergunta3() {
        return pergunta3;
    }

    public void setPergunta3(int pergunta3) {
        this.pergunta3 = pergunta3;
    }

    public String getPergunta4() {
        return pergunta4;
    }

    public void setPergunta4(String pergunta4) {
        this.pergunta4 = pergunta4;
    }

    public int getPergunta5() {
        return pergunta5;
    }

    public void setPergunta5(int pergunta5) {
        this.pergunta5 = pergunta5;
    }

    public int getPergunta6() {
        return pergunta6;
    }

    public void setPergunta6(int pergunta6) {
        this.pergunta6 = pergunta6;
    }

    public int getPergunta7() {
        return pergunta7;
    }

    public void setPergunta7(int pergunta7) {
        this.pergunta7 = pergunta7;
    }

    public int getPergunta8() {
        return pergunta8;
    }

    public void setPergunta8(int pergunta8) {
        this.pergunta8 = pergunta8;
    }
}