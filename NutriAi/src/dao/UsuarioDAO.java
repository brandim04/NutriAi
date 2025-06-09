package dao;

import model.Usuario;
import model.Paciente;     // Importa a classe Paciente
import model.Nutricionista; // Importa a classe Nutricionista
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import util.ConnectionFactory;
import java.sql.Date; // Importa java.sql.Date para o método setDate()

public class UsuarioDAO {

    // A conexão não será mais mantida como um atributo da classe,
    // será obtida e fechada por método para melhor gerenciamento de recursos.
    // private Connection connection; 

    public UsuarioDAO() {
        // O construtor não precisa mais obter a conexão aqui,
        // pois cada método obterá a sua própria.
    }

    /**
     * Cadastra um novo usuário na tabela "usuario" e define o ID gerado no objeto.
     *
     * @param usuario O objeto Usuario a ser cadastrado.
     * @return true se o cadastro foi bem-sucedido, false caso contrário.
     */
    public boolean cadastrarUsuario(Usuario usuario) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet generatedKeys = null;
        String sql = "INSERT INTO \"usuario\" (nome, cpf, data_nascimento, telefone, email, senha) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            conn = ConnectionFactory.getInstance().getConnection(); // Obtém a conexão
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getCpf());
            stmt.setDate(3, java.sql.Date.valueOf(usuario.getDataNascimento())); // Usa setDate para LocalDate
            stmt.setString(4, usuario.getTelefone());
            stmt.setString(5, usuario.getEmail());
            stmt.setString(6, usuario.getSenha());
            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    usuario.setId(generatedKeys.getInt(1)); // Define o ID gerado no objeto Usuario
                    return true;
                }
            }
            return false;
        } catch (SQLException e) {
            System.err.println("Erro ao cadastrar usuário: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            ConnectionFactory.closeConnection(conn, stmt, generatedKeys); // Garante o fechamento da conexão
        }
    }

    /**
     * Autentica um usuário no sistema e retorna o objeto específico (Paciente ou Nutricionista).
     * Este método substitui o antigo `loginUsuario`.
     *
     * @param email O email do usuário.
     * @param senha A senha do usuário.
     * @return Um objeto Paciente, Nutricionista, ou null se a autenticação falhar.
     */
    public Usuario autenticarUsuario(String email, String senha) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        // Query para buscar o usuário e seus dados específicos, usando LEFT JOIN para ambos os tipos.
        // O 'crn' do nutricionista é incluído na seleção.
        String sql = "SELECT u.id AS id_usuario, u.nome, u.cpf, u.data_nascimento, u.telefone, u.email, u.senha, " +
                     "p.id AS id_paciente, " + // ID da tabela pacientes (se for paciente)
                     "n.id AS id_nutricionista, n.crn " + // ID e CRN da tabela nutricionistas (se for nutricionista)
                     "FROM \"usuario\" u " + // Use aspas duplas se o nome da tabela for "usuario" em minúsculas
                     "LEFT JOIN \"paciente\" p ON u.id = p.usuario_id " + // Faz join com pacientes
                     "LEFT JOIN \"nutricionista\" n ON u.id = n.usuario_id " + // Faz join com nutricionistas
                     "WHERE u.email = ? AND u.senha = ?"; // Condição de autenticação

        try {
            conn = ConnectionFactory.getInstance().getConnection(); // Obtém a conexão
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
            stmt.setString(2, senha); // ATENÇÃO: Em produção, use hash de senhas!
            rs = stmt.executeQuery();

            if (rs.next()) {
                // Extrai dados comuns do usuário (base)
                int idUsuario = rs.getInt("id_usuario");
                String nome = rs.getString("nome");
                String cpfOuCrn = rs.getString("cpf"); // Dependendo do tipo, será CPF ou CRN
                LocalDate dataNascimento = rs.getDate("data_nascimento").toLocalDate();
                String telefone = rs.getString("telefone");
                // Email e senha já verificados na query

                // Tenta extrair IDs específicos
                int idPaciente = rs.getInt("id_paciente");
                int idNutricionista = rs.getInt("id_nutricionista");
                String crnNutricionista = rs.getString("crn");

                // Verifica qual tipo de usuário foi encontrado e retorna o objeto correspondente
                if (idPaciente > 0) { // Se um id_paciente válido foi encontrado
                    return new Paciente(idUsuario, nome, cpfOuCrn, dataNascimento, telefone, email, senha, idPaciente); 
                } else if (idNutricionista > 0) { // Se um id_nutricionista válido foi encontrado
                    return new Nutricionista(idUsuario, nome, cpfOuCrn, dataNascimento, telefone, email, senha, idNutricionista, crnNutricionista);
                } else {
                    // Usuário autenticado, mas não é nem Paciente nem Nutricionista nas tabelas específicas
                    System.err.println("Usuário autenticado não é paciente nem nutricionista: " + email);
                    // Pode retornar um objeto Usuario base se for o caso do seu sistema
                    return new Usuario(idUsuario, nome, cpfOuCrn, dataNascimento, telefone, email, senha);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao autenticar usuário: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConnectionFactory.closeConnection(conn, stmt, rs); // Garante o fechamento da conexão
        }
        return null; // Credenciais inválidas ou erro na consulta
    }

    /**
     * Verifica se um email já está cadastrado na tabela "usuario".
     *
     * @param email O email a ser verificado.
     * @return true se o email já existe, false caso contrário.
     */
    public boolean emailJaExiste(String email) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        boolean existe = false;

        try {
            conn = ConnectionFactory.getInstance().getConnection(); // Obtém a conexão
            String sql = "SELECT COUNT(*) FROM \"usuario\" WHERE email = ?"; // Verifica na tabela "usuario"
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, email);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                existe = rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Erro ao verificar email existente: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConnectionFactory.closeConnection(conn, pstmt, rs); // Garante o fechamento da conexão
        }
        return existe;
    }

    /**
     * Busca um usuário pela email na tabela "usuario".
     *
     * @param email O email do usuário.
     * @return O objeto Usuario encontrado, ou null se não for encontrado.
     */
    public Usuario buscarUsuarioPorEmail(String email) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "SELECT id, nome, cpf, data_nascimento, telefone, email, senha FROM \"usuario\" WHERE email = ?";
        try {
            conn = ConnectionFactory.getInstance().getConnection(); // Obtém a conexão
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
            rs = stmt.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                String cpf = rs.getString("cpf");
                LocalDate dataNascimento = LocalDate.parse(rs.getString("data_nascimento"));
                String telefone = rs.getString("telefone");
                String senha = rs.getString("senha");
                return new Usuario(id, nome, cpf, dataNascimento, telefone, email, senha);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar usuário por email: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConnectionFactory.closeConnection(conn, stmt, rs); // Garante o fechamento da conexão
        }
        return null;
    }

    /**
     * Busca um usuário pelo ID na tabela "usuario".
     *
     * @param id O ID do usuário.
     * @return O objeto Usuario encontrado, ou null se não for encontrado.
     */
    public Usuario buscarPorId(int id) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "SELECT id, nome, cpf, data_nascimento, telefone, email, senha FROM \"usuario\" WHERE id = ?";
        Usuario usuario = null;
        try {
            conn = ConnectionFactory.getInstance().getConnection(); // Obtém a conexão
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            if (rs.next()) {
                String nome = rs.getString("nome");
                String cpf = rs.getString("cpf");
                LocalDate dataNascimento = LocalDate.parse(rs.getString("data_nascimento"));
                String telefone = rs.getString("telefone");
                String email = rs.getString("email");
                String senha = rs.getString("senha");
                
                usuario = new Usuario(id, nome, cpf, dataNascimento, telefone, email, senha);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar usuário por ID: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConnectionFactory.closeConnection(conn, stmt, rs); // Garante o fechamento da conexão
        }
        return usuario;
    }

    /**
     * Atualiza os dados de um usuário na tabela "usuario".
     *
     * @param usuario O objeto Usuario com os dados atualizados.
     * @return true se a atualização foi bem-sucedida, false caso contrário.
     */
    public boolean atualizarUsuario(Usuario usuario) {
        Connection conn = null;
        PreparedStatement stmt = null;
        String sql = "UPDATE \"usuario\" SET nome = ?, cpf = ?, data_nascimento = ?, telefone = ?, email = ?, senha = ? WHERE id = ?";
        try {
            conn = ConnectionFactory.getInstance().getConnection(); // Obtém a conexão
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getCpf());
            stmt.setDate(3, java.sql.Date.valueOf(usuario.getDataNascimento())); // Usa setDate para LocalDate
            stmt.setString(4, usuario.getTelefone());
            stmt.setString(5, usuario.getEmail());
            stmt.setString(6, usuario.getSenha());
            stmt.setInt(7, usuario.getId()); // Usa o ID para WHERE clause
            
            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar usuário: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            ConnectionFactory.closeConnection(conn, stmt); // Garante o fechamento da conexão
        }
    }
}
