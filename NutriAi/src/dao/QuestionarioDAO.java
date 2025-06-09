package dao;

import model.QuestionarioRespostas;
import util.ConnectionFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types; // Importar java.sql.Types para usar Types.INTEGER

public class QuestionarioDAO {

    /**
     * Cadastra as respostas de um questionário no banco de dados.
     *
     * @param resposta O objeto QuestionarioResposta contendo as respostas.
     * @return true se as respostas foram cadastradas com sucesso, false caso contrário.
     */
    public boolean cadastrarRespostas(QuestionarioRespostas resposta) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        // As colunas são pergunta_1, pergunta_2, etc., conforme seu schema.
        String sql = "INSERT INTO questionario_resposta ("
                   + "paciente_id, " // Não incluir 'id' aqui, pois é SERIAL (auto-incremento)
                   + "pergunta_1, "
                   + "pergunta_2, "
                   + "pergunta_3, "
                   + "pergunta_4, " // Esta é a coluna VARCHAR
                   + "pergunta_5, "
                   + "pergunta_6, "
                   + "pergunta_7, "
                   + "pergunta_8"
                   + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)"; // 9 parâmetros

        try {
            conn = ConnectionFactory.getInstance().getConnection();
            // Retorna o ID gerado automaticamente
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            // Setando o ID do paciente (corrigido para getIdPaciente)
            pstmt.setInt(1, resposta.getIdPaciente());

            // Setando as respostas, convertendo String para Integer quando necessário
            // e usando os getters corretos do modelo QuestionarioRespostas

            // Pergunta 1 (INT) - Mapeia para diagnosticoPrincipal
            Integer p1_int = convertStringToInt(resposta.getDiagnosticoPrincipal());
            if (p1_int != null) pstmt.setInt(2, p1_int); else pstmt.setNull(2, Types.INTEGER);

            // Pergunta 2 (INT) - Mapeia para tempoDiagnostico
            Integer p2_int = convertStringToInt(resposta.getTempoDiagnostico());
            if (p2_int != null) pstmt.setInt(3, p2_int); else pstmt.setNull(3, Types.INTEGER);

            // Pergunta 3 (INT) - Mapeia para tratamentoAtual
            Integer p3_int = convertStringToInt(resposta.getTratamentoAtual());
            if (p3_int != null) pstmt.setInt(4, p3_int); else pstmt.setNull(4, Types.INTEGER);

            // Pergunta 4 (VARCHAR) - Mapeia para medicamentosContinuos
            // Esta é a exceção, é String no DB e no modelo
            pstmt.setString(5, resposta.getMedicamentosContinuos());

            // Pergunta 5 (INT) - Mapeia para sintomasApresentados
            Integer p5_int = convertStringToInt(resposta.getSintomasApresentados());
            if (p5_int != null) pstmt.setInt(6, p5_int); else pstmt.setNull(6, Types.INTEGER);

            // Pergunta 6 (INT) - Mapeia para alergiasIntolerancias
            Integer p6_int = convertStringToInt(resposta.getAlergiasIntolerancias());
            if (p6_int != null) pstmt.setInt(7, p6_int); else pstmt.setNull(7, Types.INTEGER);

            // Pergunta 7 (INT) - Mapeia para monitoramentoGlicose
            Integer p7_int = convertStringToInt(resposta.getMonitoramentoGlicose());
            if (p7_int != null) pstmt.setInt(8, p7_int); else pstmt.setNull(8, Types.INTEGER);

            // Pergunta 8 (INT) - Mapeia para objetivoPlanoAlimentar
            Integer p8_int = convertStringToInt(resposta.getObjetivoPlanoAlimentar());
            if (p8_int != null) pstmt.setInt(9, p8_int); else pstmt.setNull(9, Types.INTEGER);

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    resposta.setId(rs.getInt(1)); // Define o ID gerado no objeto
                    System.out.println("Respostas do questionário cadastradas com sucesso! ID: " + resposta.getId());
                    return true;
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao cadastrar respostas do questionário: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConnectionFactory.closeConnection(conn, pstmt, rs);
        }
        return false;
    }

    /**
     * Busca as respostas de um questionário pelo ID do paciente.
     * Assumimos que cada paciente tem apenas uma resposta (devido ao UNIQUE no paciente_id).
     *
     * @param pacienteId O ID do paciente.
     * @return O objeto QuestionarioResposta encontrado, ou null se não houver respostas para o paciente.
     */
    public QuestionarioRespostas buscarRespostasPorPacienteId(int pacienteId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = "SELECT id, paciente_id, pergunta_1, pergunta_2, pergunta_3, pergunta_4, pergunta_5, pergunta_6, pergunta_7, pergunta_8 " +
                     "FROM questionario_resposta WHERE paciente_id = ?";
        try {
            conn = ConnectionFactory.getInstance().getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, pacienteId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id");
                int pacienteIdFromDb = rs.getInt("paciente_id"); // Obter o paciente_id do ResultSet também

                // Lendo os Integers (usando getObject para lidar com NULLs do banco) e convertendo para String
                // para alimentar o construtor do QuestionarioRespostas, que espera Strings para as respostas.
                String p1 = (rs.getObject("pergunta_1") != null) ? String.valueOf(rs.getInt("pergunta_1")) : null;
                String p2 = (rs.getObject("pergunta_2") != null) ? String.valueOf(rs.getInt("pergunta_2")) : null;
                String p3 = (rs.getObject("pergunta_3") != null) ? String.valueOf(rs.getInt("pergunta_3")) : null;
                String p4 = rs.getString("pergunta_4"); // Esta é a String no DB
                String p5 = (rs.getObject("pergunta_5") != null) ? String.valueOf(rs.getInt("pergunta_5")) : null;
                String p6 = (rs.getObject("pergunta_6") != null) ? String.valueOf(rs.getInt("pergunta_6")) : null;
                String p7 = (rs.getObject("pergunta_7") != null) ? String.valueOf(rs.getInt("pergunta_7")) : null;
                String p8 = (rs.getObject("pergunta_8") != null) ? String.valueOf(rs.getInt("pergunta_8")) : null;

                // Cuidado: Seu construtor de QuestionarioRespostas no modelo pode ser um pouco diferente.
                // Estou assumindo: QuestionarioRespostas(int id, int pacienteId, String diagnosticoPrincipal, String tempoDiagnostico, ...)
                // Se seu construtor aceita Integer para as perguntas, teríamos que adaptar aqui ou no construtor do modelo.
                // Mas, como a view passa String, vamos manter o modelo QuestionarioRespostas com Strings para as perguntas.
                return new QuestionarioRespostas(id, pacienteIdFromDb, p1, p2, p3, p4, p5, p6, p7, p8);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar respostas do questionário: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConnectionFactory.closeConnection(conn, pstmt, rs);
        }
        return null;
    }

    // Método auxiliar para converter String para Integer (ou null se for inválido/vazio)
    private Integer convertStringToInt(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            System.err.println("Aviso: Não foi possível converter '" + value + "' para número INT. Inserindo NULL. Erro: " + e.getMessage());
            return null;
        }
    }
}