package dao;

import model.Paciente;
import model.Usuario; 
import util.ConnectionFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
/**
 *
 * @author brand
 */
public class PacienteDAO {
   
    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    public int inserir(Paciente paciente) {
        String sql = "INSERT INTO paciente (usuario_id) VALUES (?) RETURNING id;";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int idPacienteGerado = -1;

        try {
            conn = ConnectionFactory.getInstance().getConnection();
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            pstmt.setInt(1, paciente.getId());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    idPacienteGerado = rs.getInt(1);
                    paciente.setIdPaciente(idPacienteGerado);
                    System.out.println("Paciente inserido com sucesso! ID_Paciente: " + idPacienteGerado);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao inserir paciente: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConnectionFactory.closeConnection(conn, pstmt, rs);
        }
        return idPacienteGerado;
    }

    public Paciente buscarPorId(int idPaciente) {
        String sql = "SELECT id, usuario_id FROM paciente WHERE id = ?;";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Paciente paciente = null;

        try {
            conn = ConnectionFactory.getInstance().getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, idPaciente);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                int idUsuario = rs.getInt("usuario_id");

                Usuario usuario = usuarioDAO.buscarPorId(idUsuario);

                if (usuario != null) {
                    paciente = new Paciente(
                        rs.getInt("id"),
                        usuario.getId(),
                        usuario.getNome(),
                        usuario.getCpf(),
                        usuario.getTelefone(),
                        usuario.getEmail(),
                        usuario.getSenha()
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar paciente por ID: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConnectionFactory.closeConnection(conn, pstmt, rs);
        }
        return paciente;
    }

    public boolean atualizar(Paciente paciente) {
        String sql = "UPDATE paciente SET usuario_id = ? WHERE id = ?;";
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean sucesso = false;

        try {
            conn = ConnectionFactory.getInstance().getConnection();
            pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, paciente.getId());
            pstmt.setInt(2, paciente.getIdPaciente());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                sucesso = true;
                System.out.println("Paciente (entry) atualizado com sucesso! ID_Paciente: " + paciente.getIdPaciente());
            } else {
                System.out.println("Nenhum paciente encontrado com o ID_Paciente: " + paciente.getIdPaciente() + " para atualização.");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar paciente: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConnectionFactory.closeConnection(conn, pstmt);
        }
        return sucesso;
    }

    public boolean deletar(int idPaciente) {
        String sql = "DELETE FROM paciente WHERE id = ?;";
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean sucesso = false;

        try {
            conn = ConnectionFactory.getInstance().getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, idPaciente);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                sucesso = true;
                System.out.println("Paciente deletado com sucesso! ID_Paciente: " + idPaciente);
            } else {
                System.out.println("Nenhum paciente encontrado com o ID_Paciente: " + idPaciente + " para exclusão.");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao deletar paciente: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConnectionFactory.closeConnection(conn, pstmt);
        }
        return sucesso;
    }
}