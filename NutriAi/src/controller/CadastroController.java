package controller;

import dao.UsuarioDAO;
import model.Usuario;
import view.Cadastro;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import javax.swing.JOptionPane;

public class CadastroController {

    private Cadastro telaCadastro;
    private UsuarioDAO usuarioDAO;

    public CadastroController(Cadastro telaCadastro) {
        this.telaCadastro = telaCadastro;
        this.usuarioDAO = new UsuarioDAO();
        initController();
    }

    private void initController() {
        telaCadastro.getCadastrarButton().addActionListener(e -> performCadastro());
        telaCadastro.getVoltarButton().addActionListener(e -> navigateToLogin());
    }

    private void performCadastro() {
        String nome = telaCadastro.getNome();
        String cpf = telaCadastro.getCpf();
        String dataNascimentoStr = telaCadastro.getDataNascimento();
        String telefone = telaCadastro.getTelefone();
        String email = telaCadastro.getEmail();
        String senha = telaCadastro.getSenha();

        if (nome.isEmpty() || cpf.isEmpty() || dataNascimentoStr.isEmpty() ||
            telefone.isEmpty() || email.isEmpty() || senha.isEmpty()) {
            JOptionPane.showMessageDialog(telaCadastro, "Todos os campos são obrigatórios!", "Erro de Cadastro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (cpf.length() != 11 || !cpf.matches("\\d+")) {
            JOptionPane.showMessageDialog(telaCadastro, "CPF inválido! Deve conter 11 dígitos numéricos.", "Erro de Cadastro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        LocalDate dataNascimento = null;
        try {
            dataNascimento = LocalDate.parse(dataNascimentoStr, java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(telaCadastro, "Formato de Data de Nascimento inválido! Use DD/MM/AAAA.", "Erro de Cadastro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!email.contains("@") || !email.contains(".")) {
             JOptionPane.showMessageDialog(telaCadastro, "Email inválido!", "Erro de Cadastro", JOptionPane.ERROR_MESSAGE);
             return;
        }
        
        if (senha.length() < 6) {
            JOptionPane.showMessageDialog(telaCadastro, "A senha deve ter no mínimo 6 caracteres.", "Erro de Cadastro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Usuario novoUsuario = new Usuario(nome, cpf, dataNascimento, telefone, email, senha);

            boolean sucesso = usuarioDAO.cadastrarUsuario(novoUsuario);

            if (sucesso) {
                JOptionPane.showMessageDialog(telaCadastro, "Usuário cadastrado com sucesso!", "Cadastro", JOptionPane.INFORMATION_MESSAGE);
                navigateToLogin();
            } else {
                JOptionPane.showMessageDialog(telaCadastro, "Erro ao cadastrar usuário. Tente novamente.", "Erro de Cadastro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(telaCadastro, "Erro no banco de dados: " + ex.getMessage(), "Erro de Sistema", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void navigateToLogin() {
        AppController.getInstance().showLoginScreen();
        telaCadastro.dispose();
    }
}