package controller;

import view.Questionario; // Certifique-se que esta classe está no pacote 'view'
import javax.swing.SwingUtilities; // Adicionar este import
// import view.ReceitasView; // Comentar por enquanto, pois ReceitasView ainda não existe

public class QuestionarioController {

    private Questionario questionarioView;
    // Adicione outras DAOs ou modelos que este controller precisar

    public QuestionarioController() {
        // questionarioView = new Questionario(this); // Comentar: o construtor padrão de Questionario é sem argumentos
        questionarioView = new Questionario(); // Use o construtor sem argumentos por enquanto
    }

    public void iniciar() {
        if (questionarioView != null) {
            questionarioView.setVisible(true);
        } else {
            System.err.println("Erro: QuestionarioView não inicializada.");
        }
    }

    // Método para lidar com a submissão do questionário
    public void submitQuestionario() {
        System.out.println("Questionário submetido!");
        // Lógica para coletar as respostas do questionárioView
        // E salvá-las no banco de dados usando QuestionarioRespostasDAO
        // Exemplo:
        // QuestionarioRespostasDAO dao = new QuestionarioRespostasDAO();
        // QuestionarioRespostas respostas = new QuestionarioRespostas(...);
        // dao.inserir(respostas);

        // Após a submissão, talvez redirecionar o usuário para outra tela
        // Por enquanto, apenas fechar a tela do questionário
        if (questionarioView != null) {
            questionarioView.dispose(); // Fecha a tela do questionário
        }

        // Redirecionar para a tela de receitas ou outra tela após a submissão
        // SwingUtilities.invokeLater(() -> { // Comentar por enquanto
        //     new ReceitasView().setVisible(true); // Comentar por enquanto
        // });
    }

    // Você pode adicionar outros métodos para interagir com a view ou modelos aqui
}