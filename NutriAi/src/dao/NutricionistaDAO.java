package dao;

import model.Nutricionista; 
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
public class NutricionistaDAO {
    
    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    public int inserir(Nutricionista nutricionista) {
        String sql = "INSERT INTO nutricionista (usuario_id, crn, especialidade) VALUES (?, ?, ?) RETURNING id;";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int idNutricionistaGerado = -1;

        try {
            conn = ConnectionFactory.getInstance().getConnection();
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            pstmt.setInt(1, nutricionista.getId()); // ID do Usuario (FK)
            pstmt.setString(2, nutricionista.getCrn());
            pstmt.setString(3, nutricionista.getEspecialidade());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    idNutricionistaGerado = rs.getInt(1);
                    nutricionista.setIdNutricionista(idNutricionistaGerado);
                    System.out.println("Nutricionista inserido com sucesso! ID_Nutricionista: " + idNutricionistaGerado);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao inserir nutricionista: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConnectionFactory.closeConnection(conn, pstmt, rs);
        }
        return idNutricionistaGerado;
    }

    public Nutricionista buscarPorId(int idNutricionista) {
        String sql = "SELECT id, usuario_id, crn, especialidade FROM nutricionista WHERE id = ?;";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Nutricionista nutricionista = null;

        try {
            conn = ConnectionFactory.getInstance().getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, idNutricionista);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                int idUsuario = rs.getInt("usuario_id");

                Usuario usuario = usuarioDAO.buscarPorId(idUsuario);

                if (usuario != null) {
                    nutricionista = new Nutricionista(
                        rs.getInt("id"), // id da tabela nutricionista
                        rs.getString("crn"),
                        rs.getString("especialidade"),
                        usuario.getId(), // id da tabela usuario
                        usuario.getNome(),
                        usuario.getCpf(),
                        usuario.getTelefone(),
                        usuario.getEmail(),
                        usuario.getSenha()
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar nutricionista por ID: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConnectionFactory.closeConnection(conn, pstmt, rs);
        }
        return nutricionista;
    }

    public boolean atualizar(Nutricionista nutricionista) {
        String sql = "UPDATE nutricionista SET usuario_id = ?, crn = ?, especialidade = ? WHERE id = ?;";
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean sucesso = false;

        try {
            conn = ConnectionFactory.getInstance().getConnection();
            pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, nutricionista.getId()); // ID do Usuario (FK)
            pstmt.setString(2, nutricionista.getCrn());
            pstmt.setString(3, nutricionista.getEspecialidade());
            pstmt.setInt(4, nutricionista.getIdNutricionista()); // ID próprio da tabela nutricionista

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                sucesso = true;
                System.out.println("Nutricionista (entry) atualizado com sucesso! ID_Nutricionista: " + nutricionista.getIdNutricionista());
            } else {
                System.out.println("Nenhum nutricionista encontrado com o ID_Nutricionista: " + nutricionista.getIdNutricionista() + " para atualização.");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar nutricionista: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConnectionFactory.closeConnection(conn, pstmt);
        }
        return sucesso;
    }

    public boolean deletar(int idNutricionista) {
        String sql = "DELETE FROM nutricionista WHERE id = ?;";
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean sucesso = false;

        try {
            conn = ConnectionFactory.getInstance().getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, idNutricionista);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                sucesso = true;
                System.out.println("Nutricionista deletado com sucesso! ID_Nutricionista: " + idNutricionista);
            } else {
                System.out.println("Nenhum nutricionista encontrado com o ID_Nutricionista: " + idNutricionista + " para exclusão.");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao deletar nutricionista: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConnectionFactory.closeConnection(conn, pstmt);
        }
        return sucesso;
    }
}
