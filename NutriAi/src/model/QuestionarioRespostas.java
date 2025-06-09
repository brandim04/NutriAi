package model;

public class QuestionarioRespostas {
    private int id; // ID da resposta (geralmente autoincrementado no banco de dados)
    private int idPaciente;
    private String diagnosticoPrincipal;
    private String tempoDiagnostico;
    private String tratamentoAtual;
    private String medicamentosContinuos;
    private String sintomasApresentados;
    private String alergiasIntolerancias;
    private String monitoramentoGlicose;
    private String objetivoPlanoAlimentar;

    /**
     * Construtor padrão (vazio) é necessário para a linha:
     * QuestionarioRespostas respostas = new QuestionarioRespostas();
     */
    public QuestionarioRespostas() {
        // Inicializações padrão podem ser feitas aqui, se necessário.
    }

    // Construtor completo (opcional, mas útil para carregar do DB)
    public QuestionarioRespostas(int id, int idPaciente, String diagnosticoPrincipal, String tempoDiagnostico,
                                 String tratamentoAtual, String medicamentosContinuos, String sintomasApresentados,
                                 String alergiasIntolerancias, String monitoramentoGlicose, String objetivoPlanoAlimentar) {
        this.id = id;
        this.idPaciente = idPaciente;
        this.diagnosticoPrincipal = diagnosticoPrincipal;
        this.tempoDiagnostico = tempoDiagnostico;
        this.tratamentoAtual = tratamentoAtual;
        this.medicamentosContinuos = medicamentosContinuos;
        this.sintomasApresentados = sintomasApresentados; // Corrigido erro de digitação de 'sintomasAentados'
        this.alergiasIntolerancias = alergiasIntolerancias;
        this.monitoramentoGlicose = monitoramentoGlicose;
        this.objetivoPlanoAlimentar = objetivoPlanoAlimentar;
    }

    // --- Getters e Setters ---

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(int idPaciente) {
        this.idPaciente = idPaciente;
    }

    public String getDiagnosticoPrincipal() {
        return diagnosticoPrincipal;
    }

    public void setDiagnosticoPrincipal(String diagnosticoPrincipal) {
        this.diagnosticoPrincipal = diagnosticoPrincipal;
    }

    public String getTempoDiagnostico() {
        return tempoDiagnostico;
    }

    public void setTempoDiagnostico(String tempoDiagnostico) {
        this.tempoDiagnostico = tempoDiagnostico;
    }

    public String getTratamentoAtual() {
        return tratamentoAtual;
    }

    public void setTratamentoAtual(String tratamentoAtual) {
        this.tratamentoAtual = tratamentoAtual;
    }

    public String getMedicamentosContinuos() {
        return medicamentosContinuos;
    }

    public void setMedicamentosContinuos(String medicamentosContinuos) {
        this.medicamentosContinuos = medicamentosContinuos;
    }

    public String getSintomasApresentados() {
        return sintomasApresentados;
    }

    public void setSintomasApresentados(String sintomasApresentados) {
        this.sintomasApresentados = sintomasApresentados;
    }

    public String getAlergiasIntolerancias() {
        return alergiasIntolerancias;
    }

    public void setAlergiasIntolerancias(String alergiasIntolerancias) {
        this.alergiasIntolerancias = alergiasIntolerancias;
    }

    public String getMonitoramentoGlicose() {
        return monitoramentoGlicose;
    }

    public void setMonitoramentoGlicose(String monitoramentoGlicose) {
        this.monitoramentoGlicose = monitoramentoGlicose;
    }

    public String getObjetivoPlanoAlimentar() {
        return objetivoPlanoAlimentar;
    }

    public void setObjetivoPlanoAlimentar(String objetivoPlanoAlimentar) {
        this.objetivoPlanoAlimentar = objetivoPlanoAlimentar;
    }
}