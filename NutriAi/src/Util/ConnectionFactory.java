package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

    private static ConnectionFactory instance;

    private static final String URL = "jdbc:postgresql://localhost:5432/NutriAi";
    private static final String USER = "postgres";
    private static final String PASSWORD = "root"; // <-- AQUI! Sua senha é "root"

    private ConnectionFactory() {
        try {
            Class.forName("org.postgresql.Driver");
            System.out.println("Driver PostgreSQL carregado com sucesso!");
        } catch (ClassNotFoundException e) {
            System.err.println("Erro ao carregar o driver JDBC do PostgreSQL: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Falha ao carregar o driver JDBC.", e);
        }
    }

    public static synchronized ConnectionFactory getInstance() {
        if (instance == null) {
            instance = new ConnectionFactory();
        }
        return instance;
    }

    public Connection getConnection() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Conexão com o banco de dados estabelecida!");
        } catch (SQLException e) {
            System.err.println("Erro ao conectar ao banco de dados: " + e.getMessage());
            e.printStackTrace();
        }
        return conn;
    }

    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar a conexão com o banco de dados: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public static void closeConnection(Connection conn, java.sql.Statement stmt) {
        closeConnection(conn);
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar o Statement: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public static void closeConnection(Connection conn, java.sql.PreparedStatement pstmt, java.sql.ResultSet rs) {
        closeConnection(conn);
        if (pstmt != null) {
            try {
                pstmt.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar o PreparedStatement: " + e.getMessage());
                e.printStackTrace();
            }
        }
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar o ResultSet: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}