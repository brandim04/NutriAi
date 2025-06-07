package dao;

import model.QuestionarioRespostas;
import util.ConnectionFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author brand
 */
public class QuestionarioRespostasDAO {
    
    public int inserir(QuestionarioRespostas respostas) {
        String sql = "INSERT INTO questionario_respostas (paciente_id, pergunta1, pergunta2, pergunta3, pergunta4, pergunta5, pergunta6, pergunta7, pergunta8) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING id;";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int idRespostasGerado = -1;

        try {
            conn = ConnectionFactory.getInstance().getConnection();
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            pstmt.setInt(1, respostas.getPacienteId());
            pstmt.setInt(2, respostas.getPergunta1());
            pstmt.setInt(3, respostas.getPergunta2());
            pstmt.setInt(4, respostas.getPergunta3());
            pstmt.setString(5, respostas.getPergunta4());
            pstmt.setInt(6, respostas.getPergunta5());
            pstmt.setInt(7, respostas.getPergunta6());
            pstmt.setInt(8, respostas.getPergunta7());
            pstmt.setInt(9, respostas.getPergunta8());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    idRespostasGerado = rs.getInt(1);
                    respostas.setId(idRespostasGerado); // Define o ID gerado no objeto
                    System.out.println("Respostas do questionário inseridas com sucesso! ID: " + idRespostasGerado);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao inserir respostas do questionário: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConnectionFactory.closeConnection(conn, pstmt, rs);
        }
        return idRespostasGerado;
    }

    public QuestionarioRespostas buscarPorId(int idRespostas) {
        String sql = "SELECT * FROM questionario_respostas WHERE id = ?;";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        QuestionarioRespostas respostas = null;

        try {
            conn = ConnectionFactory.getInstance().getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, idRespostas);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                respostas = new QuestionarioRespostas(
                    rs.getInt("id"),
                    rs.getInt("paciente_id"),
                    rs.getInt("pergunta1"),
                    rs.getInt("pergunta2"),
                    rs.getInt("pergunta3"),
                    rs.getString("pergunta4"),
                    rs.getInt("pergunta5"),
                    rs.getInt("pergunta6"),
                    rs.getInt("pergunta7"),
                    rs.getInt("pergunta8")
                );
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar respostas do questionário por ID: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConnectionFactory.closeConnection(conn, pstmt, rs);
        }
        return respostas;
    }
    
    public List<QuestionarioRespostas> buscarPorPacienteId(int pacienteId) {
        String sql = "SELECT * FROM questionario_respostas WHERE paciente_id = ? ORDER BY id DESC;"; // Busca as mais recentes primeiro
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<QuestionarioRespostas> listaRespostas = new ArrayList<>();

        try {
            conn = ConnectionFactory.getInstance().getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, pacienteId);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                QuestionarioRespostas respostas = new QuestionarioRespostas(
                    rs.getInt("id"),
                    rs.getInt("paciente_id"),
                    rs.getInt("pergunta1"),
                    rs.getInt("pergunta2"),
                    rs.getInt("pergunta3"),
                    rs.getString("pergunta4"),
                    rs.getInt("pergunta5"),
                    rs.getInt("pergunta6"),
                    rs.getInt("pergunta7"),
                    rs.getInt("pergunta8")
                );
                listaRespostas.add(respostas);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar respostas do questionário por Paciente ID: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConnectionFactory.closeConnection(conn, pstmt, rs);
        }
        return listaRespostas;
    }

    public boolean atualizar(QuestionarioRespostas respostas) {
        String sql = "UPDATE questionario_respostas SET paciente_id = ?, pergunta1 = ?, pergunta2 = ?, pergunta3 = ?, pergunta4 = ?, pergunta5 = ?, pergunta6 = ?, pergunta7 = ?, pergunta8 = ? WHERE id = ?;";
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean sucesso = false;

        try {
            conn = ConnectionFactory.getInstance().getConnection();
            pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, respostas.getPacienteId());
            pstmt.setInt(2, respostas.getPergunta1());
            pstmt.setInt(3, respostas.getPergunta2());
            pstmt.setInt(4, respostas.getPergunta3());
            pstmt.setString(5, respostas.getPergunta4());
            pstmt.setInt(6, respostas.getPergunta5());
            pstmt.setInt(7, respostas.getPergunta6());
            pstmt.setInt(8, respostas.getPergunta7());
            pstmt.setInt(9, respostas.getPergunta8());
            pstmt.setInt(10, respostas.getId()); // ID para a cláusula WHERE

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                sucesso = true;
                System.out.println("Respostas do questionário atualizadas com sucesso! ID: " + respostas.getId());
            } else {
                System.out.println("Nenhuma resposta do questionário encontrada com o ID: " + respostas.getId() + " para atualização.");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar respostas do questionário: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConnectionFactory.closeConnection(conn, pstmt);
        }
        return sucesso;
    }

    public boolean deletar(int idRespostas) {
        String sql = "DELETE FROM questionario_respostas WHERE id = ?;";
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean sucesso = false;

        try {
            conn = ConnectionFactory.getInstance().getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, idRespostas);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                sucesso = true;
                System.out.println("Respostas do questionário deletadas com sucesso! ID: " + idRespostas);
            } else {
                System.out.println("Nenhuma resposta do questionário encontrada com o ID: " + idRespostas + " para exclusão.");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao deletar respostas do questionário: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConnectionFactory.closeConnection(conn, pstmt);
        }
        return sucesso;
    }
}
