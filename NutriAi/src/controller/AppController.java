// src/controller/AppController.java
package controller;

import dao.NutricionistaDAO;
import dao.PacienteDAO;
import dao.UsuarioDAO;
import model.Nutricionista;
import model.Paciente;
import view.TelaLogin;
import view.PerfilNutri; // Certifique-se de que esta classe existe
import view.Cadastro;
import javax.swing.JOptionPane;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import view.Perfil; // Para a tela de perfil do paciente (Corrigido para 'Perfil')
import model.Usuario; // Necessário para o método autenticarUsuario
import view.Questionario; // Adicionado para chamar o Questionario

public class AppController {

    // --- Início do padrão Singleton ---
    private static AppController instance; // A única instância da classe

    // Construtor privado para evitar instanciacao externa
    private AppController() {
        this.telaLogin = new TelaLogin(); // Cria a tela de login aqui
        this.usuarioDAO = new UsuarioDAO();
        this.nutricionistaDAO = new NutricionistaDAO();
        this.pacienteDAO = new PacienteDAO();
        initController(); // Inicializa os listeners
    }

    // Método estático para obter a instância única
    public static AppController getInstance() {
        if (instance == null) {
            instance = new AppController();
        }
        return instance;
    }
    // --- Fim do padrão Singleton ---

    private TelaLogin telaLogin;
    private UsuarioDAO usuarioDAO;
    private NutricionistaDAO nutricionistaDAO;
    private PacienteDAO pacienteDAO;

    // Inicializa os listeners dos botões da tela de login
    private void initController() {
        // Listener para o botão de login
        telaLogin.getLoginButton().addActionListener(e -> performLogin());

        // Listener para o botão de cadastro na TelaLogin
        telaLogin.getRegisterButton().addActionListener(e -> openCadastroScreen());

        // Exibe a tela de login inicial
        telaLogin.setVisible(true);
    }

    // Lógica para realizar o login
    private void performLogin() {
        System.out.println(">>> Botão 'Login' clicado na Tela de Login!"); // Adicione para depuração

        String email = telaLogin.getEmail();
        String senha = telaLogin.getSenha(); // Assumindo que TelaLogin tem um getSenha()

        // Validação básica dos campos
        if (email.isEmpty() || senha.isEmpty()) {
            JOptionPane.showMessageDialog(telaLogin, "Por favor, preencha email e senha.", "Erro de Login", JOptionPane.ERROR_MESSAGE);
            System.out.println(">>> ERRO: Campos de login vazios.");
            return; // Sai do método se os campos estiverem vazios
        }

        // --- Lógica de autenticação ---
        Usuario usuarioAutenticado = usuarioDAO.autenticarUsuario(email, senha);

        if (usuarioAutenticado != null) {
            JOptionPane.showMessageDialog(telaLogin, "Login realizado com sucesso!", "Login", JOptionPane.INFORMATION_MESSAGE);
            telaLogin.dispose(); // Fecha a tela de login após o sucesso

            // Redirecionamento baseado no tipo de usuário autenticado
            if (usuarioAutenticado instanceof Paciente) {
                // Chama o método centralizado para mostrar o perfil do paciente
                mostrarTelaPerfil((Paciente) usuarioAutenticado);
                System.out.println(">>> Login de Paciente: " + usuarioAutenticado.getNome());
            } else if (usuarioAutenticado instanceof Nutricionista) {
                // Chama o método centralizado para mostrar o perfil do nutricionista
                mostrarPerfilNutricionista((Nutricionista) usuarioAutenticado);
                System.out.println(">>> Login de Nutricionista: " + usuarioAutenticado.getNome());
            } else {
                JOptionPane.showMessageDialog(telaLogin, "Tipo de usuário desconhecido.", "Erro", JOptionPane.ERROR_MESSAGE);
                System.err.println(">>> ERRO: Tipo de usuário autenticado desconhecido.");
                telaLogin.setVisible(true); // Mantém a tela de login aberta
            }

        } else {
            // Credenciais inválidas
            JOptionPane.showMessageDialog(telaLogin, "Email ou senha inválidos.", "Erro de Login", JOptionPane.ERROR_MESSAGE);
            System.out.println(">>> ERRO: Email ou senha inválidos.");
        }
    }

    // Abre a tela de cadastro
    private void openCadastroScreen() {
        Cadastro telaCadastro = new Cadastro();
        telaCadastro.setVisible(true);
        telaLogin.setVisible(false); // Esconde a tela de login

        // Listener para o botão "Cadastrar" da Tela de Cadastro
        telaCadastro.getCadastrarButton().addActionListener(e -> performRegistration(telaCadastro));
        // Listener para o botão "Voltar" da Tela de Cadastro
        telaCadastro.getVoltarButton().addActionListener(e -> {
            telaCadastro.dispose(); // Fecha a tela de cadastro
            telaLogin.setVisible(true); // Mostra a tela de login novamente
        });
    }

    // Lógica para realizar o registro (cadastro) de um novo usuário
    private void performRegistration(Cadastro telaCadastro) {
        System.out.println(">>> Botão 'Cadastrar' clicado na Tela de Cadastro!");

        String nome = telaCadastro.getNome();
        String cpfCnpjOuCrn = telaCadastro.getCpf(); // Campo usado tanto para CPF quanto para CRN
        String dataNascimentoStr = telaCadastro.getDataNascimento();
        String telefone = telaCadastro.getTelefone();
        String email = telaCadastro.getEmail();
        String senha = telaCadastro.getSenha();
        String tipoUsuario = (String) telaCadastro.getComboBoxTipoUsuario().getSelectedItem();

        System.out.println(">>> Dados do Cadastro:");
        System.out.println("Nome: " + nome);
        System.out.println("CPF/CRN: " + cpfCnpjOuCrn);
        System.out.println("Data Nasc.: " + dataNascimentoStr);
        System.out.println("Telefone: " + telefone);
        System.out.println("Email: " + email);
        System.out.println("Tipo Usuário: " + tipoUsuario);

        // Validação de campos vazios
        if (nome.isEmpty() || cpfCnpjOuCrn.isEmpty() || dataNascimentoStr.isEmpty() || telefone.isEmpty() || email.isEmpty() || senha.isEmpty()) {
            JOptionPane.showMessageDialog(telaCadastro, "Todos os campos devem ser preenchidos.", "Erro de Cadastro", JOptionPane.ERROR_MESSAGE);
            System.out.println(">>> ERRO: Campos vazios.");
            return;
        }

        // Validação e parse da data de nascimento
        LocalDate dataNascimento;
        try {
            dataNascimento = LocalDate.parse(dataNascimentoStr); // Assume o formato AAAA-MM-DD
            System.out.println(">>> Data de nascimento parseada: " + dataNascimento);
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(telaCadastro, "Formato de data de nascimento inválido. Use AAAA-MM-DD.", "Erro de Cadastro", JOptionPane.ERROR_MESSAGE);
            System.err.println(">>> ERRO: Formato de data inválido: " + ex.getMessage());
            return;
        }

        // Verifica se o email já existe no banco de dados
        if (usuarioDAO.emailJaExiste(email)) {
            JOptionPane.showMessageDialog(telaCadastro, "Este email já está cadastrado.", "Erro de Cadastro", JOptionPane.ERROR_MESSAGE);
            System.out.println(">>> ERRO: Email já cadastrado.");
            return;
        }
        System.out.println(">>> Email não cadastrado, prosseguindo...");

        // Lógica de cadastro baseada no tipo de usuário selecionado
        if ("Nutricionista".equals(tipoUsuario)) {
            System.out.println(">>> Tentando cadastrar como Nutricionista...");
            String crn = cpfCnpjOuCrn; // Para nutricionista, 'cpfCnpjOuCrn' é o CRN

            Nutricionista novoNutricionista = new Nutricionista(nome, crn, dataNascimento, telefone, email, senha, crn);

            if (nutricionistaDAO.cadastrarNutricionista(novoNutricionista)) {
                JOptionPane.showMessageDialog(telaCadastro, "Nutricionista cadastrado com sucesso!");
                System.out.println(">>> Nutricionista cadastrado com sucesso!");

                telaCadastro.dispose(); // Fecha a tela de cadastro
                mostrarPerfilNutricionista(novoNutricionista); // Usa o método centralizado

            } else {
                JOptionPane.showMessageDialog(telaCadastro, "Erro ao cadastrar nutricionista.", "Erro de Cadastro", JOptionPane.ERROR_MESSAGE);
                System.err.println(">>> ERRO: Falha ao cadastrar nutricionista no DAO.");
            }

        } else if ("Paciente".equals(tipoUsuario)) {
            System.out.println(">>> Tentando cadastrar como Paciente...");
            String cpf = cpfCnpjOuCrn; // Para paciente, 'cpfCnpjOuCrn' é o CPF

            // O construtor Paciente(int idUsuario, String nome, String cpf, LocalDate dataNascimento, String telefone, String email, String senha, int idPaciente, String objetivo)
            // O 'idUsuario' e 'idPaciente' devem ser preenchidos pelo DAO após a inserção.
            // Aqui estamos passando 0 e null, e o DAO deve lidar com a atribuição de IDs reais.
            Paciente novoPaciente = new Paciente(0, nome, cpf, dataNascimento, telefone, email, senha, 0);

            if (pacienteDAO.cadastrarPaciente(novoPaciente)) {
                JOptionPane.showMessageDialog(telaCadastro, "Paciente cadastrado com sucesso!");
                System.out.println(">>> Paciente cadastrado com sucesso!");

                telaCadastro.dispose(); // Fecha a tela de cadastro
                mostrarTelaPerfil(novoPaciente); // Usa o método centralizado

            } else {
                JOptionPane.showMessageDialog(telaCadastro, "Erro ao cadastrar paciente.", "Erro de Cadastro", JOptionPane.ERROR_MESSAGE);
                System.err.println(">>> ERRO: Falha ao cadastrar paciente no DAO.");
            }
        } else {
            JOptionPane.showMessageDialog(telaCadastro, "Tipo de usuário inválido selecionado.", "Erro de Cadastro", JOptionPane.ERROR_MESSAGE);
            System.err.println(">>> ERRO: Tipo de usuário inválido.");
        }
    }

    // --- Métodos de Navegação Centralizados ---

    /**
     * Exibe a tela de perfil para um Paciente.
     * @param paciente O objeto Paciente logado ou recém-cadastrado.
     */
    public void mostrarTelaPerfil(Paciente paciente) {
        // Assume que 'Perfil' é a classe da sua tela de perfil para pacientes
        Perfil perfilPaciente = new Perfil(paciente); // O construtor de Perfil deve aceitar um Paciente
        perfilPaciente.setVisible(true);
    }

    /**
     * Exibe a tela de perfil para um Nutricionista.
     * @param nutricionista O objeto Nutricionista logado ou recém-cadastrado.
     */
    public void mostrarPerfilNutricionista(Nutricionista nutricionista) {
        // Assume que 'PerfilNutri' é a classe da sua tela de perfil para nutricionistas
        PerfilNutri perfilNutri = new PerfilNutri(nutricionista); // O construtor de PerfilNutri deve aceitar um Nutricionista
        perfilNutri.setVisible(true);
    }

    /**
     * Exibe a tela do Questionário.
     * @param paciente O paciente para o qual o questionário será preenchido.
     */
    public void mostrarQuestionario(Paciente paciente) {
        Questionario questionario = new Questionario(paciente); // O construtor de Questionario deve aceitar um Paciente
        questionario.setVisible(true);
    }

    // Método auxiliar para exibir a tela de login (se necessário voltar a ela)
    public void showLoginScreen() {
        telaLogin.setVisible(true);
    }
}