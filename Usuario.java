
package com.exemplo.sistemausuarios;

public class Usuario {

    private int id;
    private String nome;
    private String email;
    private int idade;
    
    
    public Usuario() {
    }
    
    public Usuario(String nome, String email, int idade) {
        this.nome = nome;
        this.email = email;
        this.idade = idade;
    }
    
   
    public Usuario(int id, String nome, String email, int idade) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.idade = idade;
    }

    public int getId() {
        return id;
    }
    
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getNome() {
        return nome;
    }
    
  
    public void setNome(String nome) {
        this.nome = nome;
    }
    
  
    public String getEmail() {
        return email;
    }
    
   
    public void setEmail(String email) {
        this.email = email;
    }
    
    public int getIdade() {
        return idade;
    }
   
    public void setIdade(int idade) {
        this.idade = idade;
    }
    
    public boolean isValido() {
        return nome != null && !nome.trim().isEmpty() &&
               email != null && !email.trim().isEmpty() && email.contains("@") &&
               idade > 0 && idade < 150;
    }
    
   
    @Override
    public String toString() {
        return String.format("Usuario{id=%d, nome='%s', email='%s', idade=%d}", 
                           id, nome, email, idade);
    }
    
  
    public String toStringFormatado() {
        return String.format("ID: %d | Nome: %s | Email: %s | Idade: %d anos", 
                           id, nome, email, idade);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Usuario usuario = (Usuario) obj;
        return id == usuario.id;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}