package dao;

import model.Paciente;
import util.ConnectionFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class NutricionistaPacienteDAO {

    private Connection connection;

    public NutricionistaPacienteDAO() {
        this.connection = ConnectionFactory.getInstance().getConnection();
    }

    public List<Paciente> buscarPacientesDoNutricionista(int idNutricionista) {
        List<Paciente> pacientes = new ArrayList<>();
        
        String sql = "SELECT u.id AS usuario_id, u.nome, p.id AS paciente_id " + 
                     "FROM \"nutricionista_paciente\" np " +
                     "JOIN \"paciente\" p ON np.paciente_id = p.id " +
                     "JOIN \"usuario\" u ON p.usuario_id = u.id " +
                     "WHERE np.nutricionista_id = ?"; 
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, idNutricionista);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    // Dados essenciais para um objeto Paciente mínimo
                    int idUsuario = rs.getInt("usuario_id"); 
                    String nome = rs.getString("nome");
                    int idPaciente = rs.getInt("paciente_id"); 
                    
                    // Cria um objeto Paciente com os dados mínimos necessários.
                    // Se o construtor de Paciente exigir mais parâmetros (CPF, dataNascimento, etc.),
                    // você precisará passá-los como null ou valores padrão, ou criar um construtor
                    // mais simples em Paciente.java para este cenário.
                    // Por enquanto, vamos usar um construtor completo e passar nulo para os outros campos.
                    Paciente paciente = new Paciente(idUsuario, nome, null, null, null, null, null, idPaciente); 
                    pacientes.add(paciente);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar pacientes do nutricionista: " + e.getMessage());
            e.printStackTrace();
        }
        return pacientes;
    }

    // Opcional: Adicione métodos para adicionar/remover associações
    public boolean adicionarAssociacao(int idNutricionista, int idPaciente) {
        String sql = "INSERT INTO \"nutricionista_paciente\" (nutricionista_id, paciente_id) VALUES (?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, idNutricionista);
            pstmt.setInt(2, idPaciente);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao adicionar associação nutricionista-paciente: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean removerAssociacao(int idNutricionista, int idPaciente) {
        String sql = "DELETE FROM \"nutricionista_paciente\" WHERE nutricionista_id = ? AND paciente_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, idNutricionista);
            pstmt.setInt(2, idPaciente);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao remover associação nutricionista-paciente: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
