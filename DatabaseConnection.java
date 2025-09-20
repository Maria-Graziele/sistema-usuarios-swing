package com.exemplo.sistemausuarios;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DatabaseConnection {
    
    private static final String URL = "jdbc:mysql://localhost:3306/meu_projeto";
    private static final String USERNAME = "root"; 
    private static final String PASSWORD = "1234";   
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";

    public static Connection getConnection() {
        Connection connection = null;
        
        try {
           
            Class.forName(DRIVER);
            
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            
            System.out.println("Conexão com MySQL estabelecida com sucesso!");
            
        } catch (ClassNotFoundException e) {
            System.err.println("Erro: Driver MySQL não encontrado!");
            System.err.println("Verifique se o mysql-connector-j está no classpath");
            e.printStackTrace();
            
        } catch (SQLException e) {
            System.err.println("Erro ao conectar com o banco de dados:");
            System.err.println("URL: " + URL);
            System.err.println("Usuário: " + USERNAME);
            System.err.println("Erro: " + e.getMessage());
            
            // Dicas de troubleshooting
            if (e.getMessage().contains("Access denied")) {
                System.err.println("\n💡 DICA: Verifique usuário e senha do MySQL");
            } else if (e.getMessage().contains("Connection refused")) {
                System.err.println("\n💡 DICA: Verifique se o MySQL está rodando na porta 3306");
            } else if (e.getMessage().contains("Unknown database")) {
                System.err.println("\n💡 DICA: Crie o banco 'meu_projeto' no MySQL primeiro");
            }
            
            e.printStackTrace();
        }
        
        return connection;
    }
    
   
    public static boolean testarConexao() {
        Connection conn = getConnection();
        if (conn != null) {
            try {
                conn.close();
                return true;
            } catch (SQLException e) {
                System.err.println("Erro ao fechar conexão: " + e.getMessage());
            }
        }
        return false;
    }
    

    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Conexão fechada com sucesso.");
            } catch (SQLException e) {
                System.err.println("Erro ao fechar conexão: " + e.getMessage());
            }
        }
    }
}