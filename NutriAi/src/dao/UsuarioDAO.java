package dao;

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
public class UsuarioDAO {

    public int inserir(Usuario usuario) {
        String sql = "INSERT INTO usuario (nome, cpf, telefone, email, senha) VALUES (?, ?, ?, ?, ?) RETURNING id;";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int idGerado = -1; // Para armazenar o ID gerado pelo banco

        try {
            conn = ConnectionFactory.getInstance().getConnection(); // Obtém a conexão
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS); // Prepara a declaração para retornar o ID

            pstmt.setString(1, usuario.getNome());
            pstmt.setString(2, usuario.getCpf());
            pstmt.setString(3, usuario.getTelefone());
            pstmt.setString(4, usuario.getEmail());
            pstmt.setString(5, usuario.getSenha());

            int affectedRows = pstmt.executeUpdate(); // Executa a inserção

            if (affectedRows > 0) {
                rs = pstmt.getGeneratedKeys(); // Obtém o ResultSet com o ID gerado
                if (rs.next()) {
                    idGerado = rs.getInt(1); // Obtém o ID
                    usuario.setId(idGerado); // Define o ID gerado no objeto Usuario
                    System.out.println("Usuário inserido com sucesso! ID: " + idGerado);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao inserir usuário: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Fecha os recursos na ordem inversa de abertura
            ConnectionFactory.closeConnection(conn, pstmt, rs);
        }
        return idGerado; // Retorna o ID do usuário inserido
    }

    // Método para buscar um usuário pelo ID
    public Usuario buscarPorId(int id) {
        // CORRIGIDO: FROM usuario (singular)
        String sql = "SELECT id, nome, cpf, telefone, email, senha FROM usuario WHERE id = ?;";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Usuario usuario = null;

        try {
            conn = ConnectionFactory.getInstance().getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id); // Define o ID para a consulta

            rs = pstmt.executeQuery(); // Executa a consulta

            if (rs.next()) { // Se encontrar um resultado
                usuario = new Usuario(
                    rs.getInt("id"),
                    rs.getString("nome"),
                    rs.getString("cpf"),
                    rs.getString("telefone"),
                    rs.getString("email"),
                    rs.getString("senha")
                ); // Removido o {} pois Usuario não é mais abstrata
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar usuário por ID: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConnectionFactory.closeConnection(conn, pstmt, rs);
        }
        return usuario; // Retorna o usuário encontrado ou null
    }

    // Método para buscar um usuário pelo email (útil para login)
    public Usuario buscarPorEmail(String email) {
        // CORRIGIDO: FROM usuario (singular)
        String sql = "SELECT id, nome, cpf, telefone, email, senha FROM usuario WHERE email = ?;";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Usuario usuario = null;

        try {
            conn = ConnectionFactory.getInstance().getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, email);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                usuario = new Usuario(
                    rs.getInt("id"),
                    rs.getString("nome"),
                    rs.getString("cpf"),
                    rs.getString("telefone"),
                    rs.getString("email"),
                    rs.getString("senha")
                ); // Removido o {}
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar usuário por email: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConnectionFactory.closeConnection(conn, pstmt, rs);
        }
        return usuario;
    }

    // Método para atualizar um usuário existente
    public boolean atualizar(Usuario usuario) {
        // CORRIGIDO: UPDATE usuario (singular)
        String sql = "UPDATE usuario SET nome = ?, cpf = ?, telefone = ?, email = ?, senha = ? WHERE id = ?;";
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean sucesso = false;

        try {
            conn = ConnectionFactory.getInstance().getConnection();
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, usuario.getNome());
            pstmt.setString(2, usuario.getCpf());
            pstmt.setString(3, usuario.getTelefone());
            pstmt.setString(4, usuario.getEmail());
            pstmt.setString(5, usuario.getSenha());
            pstmt.setInt(6, usuario.getId()); // O ID é usado no WHERE para saber qual usuário atualizar

            int affectedRows = pstmt.executeUpdate(); // Retorna o número de linhas afetadas
            if (affectedRows > 0) {
                sucesso = true;
                System.out.println("Usuário atualizado com sucesso! ID: " + usuario.getId());
            } else {
                System.out.println("Nenhum usuário encontrado com o ID: " + usuario.getId() + " para atualização.");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar usuário: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConnectionFactory.closeConnection(conn, pstmt);
        }
        return sucesso;
    }

    // Método para deletar um usuário pelo ID
    public boolean deletar(int id) {
        // CORRIGIDO: DELETE FROM usuario (singular)
        String sql = "DELETE FROM usuario WHERE id = ?;";
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean sucesso = false;

        try {
            conn = ConnectionFactory.getInstance().getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                sucesso = true;
                System.out.println("Usuário deletado com sucesso! ID: " + id);
            } else {
                System.out.println("Nenhum usuário encontrado com o ID: " + id + " para exclusão.");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao deletar usuário: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConnectionFactory.closeConnection(conn, pstmt);
        }
        return sucesso;
    }
}