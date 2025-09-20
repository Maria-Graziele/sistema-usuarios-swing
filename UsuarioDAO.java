package com.exemplo.sistemausuarios;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {
    
    
    private static final String INSERT_SQL = "INSERT INTO usuarios (nome, email, idade) VALUES (?, ?, ?)";
    private static final String SELECT_ALL_SQL = "SELECT * FROM usuarios ORDER BY nome";
    private static final String SELECT_BY_ID_SQL = "SELECT * FROM usuarios WHERE id = ?";
    private static final String UPDATE_SQL = "UPDATE usuarios SET nome = ?, email = ?, idade = ? WHERE id = ?";
    private static final String DELETE_SQL = "DELETE FROM usuarios WHERE id = ?";
    private static final String SELECT_BY_EMAIL_SQL = "SELECT * FROM usuarios WHERE email = ?";
    private static final String COUNT_SQL = "SELECT COUNT(*) as total FROM usuarios";
    
    
    public boolean inserir(Usuario usuario) {
       
        if (usuario == null || !usuario.isValido()) {
            System.err.println("❌ Erro: Dados do usuário inválidos para inserção");
            return false;
        }
        
        
        if (emailJaExiste(usuario.getEmail())) {
            System.err.println("❌ Erro: Email já cadastrado no sistema");
            return false;
        }
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getEmail());
            stmt.setInt(3, usuario.getIdade());
            
            int linhasAfetadas = stmt.executeUpdate();
            
            if (linhasAfetadas > 0) {
                // Obter o ID gerado
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        usuario.setId(generatedKeys.getInt(1));
                        System.out.println("✅ Usuário inserido com sucesso! ID: " + usuario.getId());
                        return true;
                    }
                }
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Erro ao inserir usuário:");
            System.err.println("Usuário: " + usuario);
            System.err.println("Erro SQL: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    public List<Usuario> buscarTodos() {
        List<Usuario> usuarios = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_ALL_SQL)) {
            
            while (rs.next()) {
                Usuario usuario = criarUsuarioDoResultSet(rs);
                usuarios.add(usuario);
            }
            
            System.out.println("📋 " + usuarios.size() + " usuário(s) encontrado(s)");
            
        } catch (SQLException e) {
            System.err.println("❌ Erro ao buscar usuários:");
            System.err.println("Erro SQL: " + e.getMessage());
            e.printStackTrace();
        }
        
        return usuarios;
    }
    
    
    public Usuario buscarPorId(int id) {
        if (id <= 0) {
            System.err.println("❌ Erro: ID inválido para busca");
            return null;
        }
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_ID_SQL)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Usuario usuario = criarUsuarioDoResultSet(rs);
                    System.out.println("✅ Usuário encontrado: " + usuario.getNome());
                    return usuario;
                } else {
                    System.out.println("❌ Usuário com ID " + id + " não encontrado");
                }
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Erro ao buscar usuário por ID:");
            System.err.println("ID: " + id);
            System.err.println("Erro SQL: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    public boolean atualizar(Usuario usuario) {
        if (usuario == null || !usuario.isValido() || usuario.getId() <= 0) {
            System.err.println("❌ Erro: Dados do usuário inválidos para atualização");
            return false;
        }
        
      
        Usuario usuarioComMesmoEmail = buscarPorEmail(usuario.getEmail());
        if (usuarioComMesmoEmail != null && usuarioComMesmoEmail.getId() != usuario.getId()) {
            System.err.println("❌ Erro: Email já está sendo usado por outro usuário");
            return false;
        }
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_SQL)) {
            
            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getEmail());
            stmt.setInt(3, usuario.getIdade());
            stmt.setInt(4, usuario.getId());
            
            int linhasAfetadas = stmt.executeUpdate();
            
            if (linhasAfetadas > 0) {
                System.out.println("✅ Usuário atualizado com sucesso!");
                return true;
            } else {
                System.out.println("❌ Nenhum usuário foi atualizado. Verifique se o ID existe.");
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Erro ao atualizar usuário:");
            System.err.println("Usuário: " + usuario);
            System.err.println("Erro SQL: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
   
    public boolean deletar(int id) {
        if (id <= 0) {
            System.err.println("❌ Erro: ID inválido para exclusão");
            return false;
        }
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_SQL)) {
            
            stmt.setInt(1, id);
            
            int linhasAfetadas = stmt.executeUpdate();
            
            if (linhasAfetadas > 0) {
                System.out.println("✅ Usuário deletado com sucesso!");
                return true;
            } else {
                System.out.println("❌ Nenhum usuário foi deletado. Verifique se o ID existe.");
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Erro ao deletar usuário:");
            System.err.println("ID: " + id);
            System.err.println("Erro SQL: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    
    public Usuario buscarPorEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return null;
        }
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_EMAIL_SQL)) {
            
            stmt.setString(1, email.trim());
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return criarUsuarioDoResultSet(rs);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Erro ao buscar usuário por email:");
            System.err.println("Email: " + email);
            System.err.println("Erro SQL: " + e.getMessage());
        }
        
        return null;
    }
    
   
    public boolean emailJaExiste(String email) {
        return buscarPorEmail(email) != null;
    }
    
   
    public int contarUsuarios() {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(COUNT_SQL)) {
            
            if (rs.next()) {
                return rs.getInt("total");
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Erro ao contar usuários:");
            System.err.println("Erro SQL: " + e.getMessage());
        }
        
        return 0;
    }
   
    private Usuario criarUsuarioDoResultSet(ResultSet rs) throws SQLException {
        return new Usuario(
            rs.getInt("id"),
            rs.getString("nome"),
            rs.getString("email"),
            rs.getInt("idade")
        );
    }
    
    
    public boolean testarConexao() {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            
         
            stmt.executeQuery("SELECT COUNT(*) FROM usuarios LIMIT 1");
            System.out.println("✅ Conexão com a tabela 'usuarios' funcionando!");
            return true;
            
        } catch (SQLException e) {
            System.err.println("❌ Erro na conexão com a tabela:");
            System.err.println("Erro SQL: " + e.getMessage());
            
            if (e.getMessage().contains("doesn't exist")) {
                System.err.println("\n💡 DICA: Execute este SQL no MySQL para criar a tabela:");
                System.err.println("CREATE TABLE usuarios (");
                System.err.println("    id INT AUTO_INCREMENT PRIMARY KEY,");
                System.err.println("    nome VARCHAR(100) NOT NULL,");
                System.err.println("    email VARCHAR(100) UNIQUE NOT NULL,");
                System.err.println("    idade INT");
                System.err.println(");");
            }
            
            return false;
        }
    }
}
