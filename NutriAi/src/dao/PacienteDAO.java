// No seu PacienteDAO.java

package dao;

import model.Paciente;
import model.Usuario;
import util.ConnectionFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

public class PacienteDAO {

    private Connection connection;
    private UsuarioDAO usuarioDAO; 

    public PacienteDAO() {
        this.connection = ConnectionFactory.getInstance().getConnection();
        this.usuarioDAO = new UsuarioDAO(); 
    }

    public boolean cadastrarPaciente(Paciente paciente) {
        boolean usuarioCadastrado = usuarioDAO.cadastrarUsuario(paciente);
        if (!usuarioCadastrado) {
            return false;
        }

        int idUsuario = paciente.getId();

        String sql = "INSERT INTO \"paciente\" (usuario_id) VALUES (?)"; 
        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, idUsuario);

            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    int idPacienteGerado = rs.getInt(1);
                    paciente.setIdPaciente(idPacienteGerado);
                    System.out.println("Paciente cadastrado com sucesso! ID_Paciente: " + idPacienteGerado);
                    return true;
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao cadastrar paciente: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public Paciente buscarPacientePorId(int idUsuario) {
        // ATENÇÃO AQUI: Removemos "p.objetivo" da seleção.
        String sql = "SELECT p.id AS paciente_id, u.id, u.nome, u.cpf, u.data_nascimento, u.telefone, u.email, u.senha " +
                     "FROM \"paciente\" p JOIN \"usuario\" u ON p.usuario_id = u.id " +
                     "WHERE u.id = ?"; 
        Paciente paciente = null;
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, idUsuario);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int idPacienteDb = rs.getInt("paciente_id");

                    int idUsuarioDb = rs.getInt("id"); 
                    String nome = rs.getString("nome");
                    String cpf = rs.getString("cpf");
                    LocalDate dataNascimento = null;
                    if (rs.getString("data_nascimento") != null) {
                         dataNascimento = LocalDate.parse(rs.getString("data_nascimento"));
                    }
                    
                    String telefone = rs.getString("telefone");
                    String email = rs.getString("email");
                    String senha = rs.getString("senha");

                    paciente = new Paciente(idUsuarioDb, nome, cpf, dataNascimento, telefone, email, senha, idPacienteDb);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar paciente: " + e.getMessage());
            e.printStackTrace();
        }
        return paciente;
    }

    public boolean atualizarPaciente(Paciente paciente) {
        boolean usuarioAtualizado = usuarioDAO.atualizarUsuario(paciente);
        if (!usuarioAtualizado) {
            return false;
        }

        String sql = "UPDATE \"paciente\" WHERE usuario_id = ?"; 
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(2, paciente.getId()); 
            
            int linhasAfetadas = pstmt.executeUpdate();
            if (linhasAfetadas > 0) {
                System.out.println("Paciente (entry) atualizado com sucesso! ID_Usuario: " + paciente.getId());
                return true;
            } else {
                System.out.println("Nenhum paciente encontrado com o ID_Usuario: " + paciente.getId() + " para atualização.");
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar paciente: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
}