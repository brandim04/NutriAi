package dao;

import model.Nutricionista;
import model.Usuario;
import model.Paciente;
import util.ConnectionFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class NutricionistaDAO {

    // A conexão não será mais mantida como atributo da classe.
    private UsuarioDAO usuarioDAO;

    public NutricionistaDAO() {
        this.usuarioDAO = new UsuarioDAO();
    }

    /**
     * Cadastra um novo nutricionista, incluindo o registro base do usuário.
     *
     * @param nutricionista O objeto Nutricionista a ser cadastrado.
     * @return true se o cadastro foi bem-sucedido, false caso contrário.
     */
    public boolean cadastrarNutricionista(Nutricionista nutricionista) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        boolean usuarioCadastrado = usuarioDAO.cadastrarUsuario(nutricionista);
        if (!usuarioCadastrado) {
            return false;
        }

        int idUsuario = nutricionista.getId();

        String sql = "INSERT INTO \"nutricionista\" (usuario_id, crn) VALUES (?, ?)";
        try {
            conn = ConnectionFactory.getInstance().getConnection();
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, idUsuario);
            pstmt.setString(2, nutricionista.getCrn());
            
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    int idNutricionistaGerado = rs.getInt(1);
                    nutricionista.setIdNutricionista(idNutricionistaGerado);
                    System.out.println("Nutricionista cadastrado com sucesso! ID_Nutricionista: " + idNutricionistaGerado);
                    return true;
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao cadastrar nutricionista: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConnectionFactory.closeConnection(conn, pstmt, rs);
        }
        return false;
    }

    /**
     * Busca um nutricionista pelo seu ID específico.
     *
     * @param idNutricionista O ID do nutricionista na tabela "nutricionista".
     * @return O objeto Nutricionista encontrado, ou null se não for encontrado.
     */
    public Nutricionista buscarNutricionistaPorId(int idNutricionista) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT n.id, n.crn, u.id AS usuario_id_fk, u.nome, u.cpf, u.data_nascimento, u.telefone, u.email, u.senha " +
                     "FROM \"nutricionista\" n JOIN \"usuario\" u ON n.usuario_id = u.id WHERE n.id = ?";
        Nutricionista nutricionista = null;
        try {
            conn = ConnectionFactory.getInstance().getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, idNutricionista);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                int idNutriDb = rs.getInt("id");
                String crn = rs.getString("crn");

                int idUsuario = rs.getInt("usuario_id_fk");
                String nome = rs.getString("nome");
                String cpf = rs.getString("cpf");
                LocalDate dataNascimento = LocalDate.parse(rs.getString("data_nascimento"));
                String telefone = rs.getString("telefone");
                String email = rs.getString("email");
                String senha = rs.getString("senha");

                nutricionista = new Nutricionista(idUsuario, nome, cpf, dataNascimento, telefone, email, senha,
                                                  idNutriDb, crn);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar nutricionista: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConnectionFactory.closeConnection(conn, pstmt, rs);
        }
        return nutricionista;
    }
    
    /**
     * Atualiza os dados de um nutricionista.
     *
     * @param nutricionista O objeto Nutricionista com os dados atualizados.
     * @return true se a atualização foi bem-sucedida, false caso contrário.
     */
    public boolean atualizarNutricionista(Nutricionista nutricionista) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        boolean usuarioAtualizado = usuarioDAO.atualizarUsuario(nutricionista);
        if (!usuarioAtualizado) {
            return false;
        }

        String sql = "UPDATE \"nutricionista\" SET crn = ? WHERE id = ?";
        try {
            conn = ConnectionFactory.getInstance().getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, nutricionista.getCrn());
            pstmt.setInt(2, nutricionista.getIdNutricionista());
            
            int linhasAfetadas = pstmt.executeUpdate();
            if (linhasAfetadas > 0) {
                System.out.println("Nutricionista (entry) atualizado com sucesso! ID_Nutricionista: " + nutricionista.getIdNutricionista());
                return true;
            } else {
                System.out.println("Nenhum nutricionista encontrado com o ID_Nutricionista: " + nutricionista.getIdNutricionista() + " para atualização.");
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar nutricionista: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConnectionFactory.closeConnection(conn, pstmt);
        }
        return false;
    }

    /**
     * Busca todos os pacientes associados a um nutricionista específico através da tabela de junção.
     *
     * @param idNutricionista O ID do nutricionista para buscar os pacientes.
     * @return Uma lista de objetos Paciente, ou uma lista vazia se nenhum paciente for encontrado.
     */
    public List<Paciente> buscarPacientesDoNutricionista(int idNutricionista) {
        List<Paciente> pacientes = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        // A query agora faz JOIN com a tabela de junção 'nutricionista_paciente'
        String sql = "SELECT p.id AS paciente_id, " +
                     "u.id AS usuario_id_fk, u.nome, u.cpf, u.data_nascimento, u.telefone, u.email, u.senha " +
                     "FROM \"paciente\" p " +
                     "JOIN \"usuario\" u ON p.usuario_id = u.id " +
                     "JOIN nutricionista_paciente np ON p.id = np.paciente_id " + // Junta com a tabela de junção
                     "WHERE np.nutricionista_id = ?"; // Filtra pelo ID do nutricionista na tabela de junção

        try {
            conn = ConnectionFactory.getInstance().getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, idNutricionista);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                // Dados do paciente (específicos)
                int idPacienteDb = rs.getInt("paciente_id");

                // Dados do usuário (base)
                int idUsuarioDb = rs.getInt("usuario_id_fk");
                String nome = rs.getString("nome");
                String cpf = rs.getString("cpf");
                LocalDate dataNascimento = LocalDate.parse(rs.getString("data_nascimento"));
                String telefone = rs.getString("telefone");
                String email = rs.getString("email");
                String senha = rs.getString("senha");

                // Cria o objeto Paciente
                Paciente paciente = new Paciente(idUsuarioDb, nome, cpf, dataNascimento, telefone, email, senha, idPacienteDb); 
                pacientes.add(paciente);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar pacientes para nutricionista: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConnectionFactory.closeConnection(conn, pstmt, rs);
        }
        return pacientes;
    }
}
