# Sistema de Usuários - Java Swing e MySQL

Projeto acadêmico desenvolvido em Java, com interface gráfica usando *Java Swing* e banco de dados no *MySQL*. Permite cadastro, consulta, atualização e exclusão de usuários.

## Funcionalidades

- Cadastro de usuários (nome, e-mail, idade)
- Consulta de usuários cadastrados
- Atualização de dados do usuário
- Exclusão de usuários

## Tecnologias Utilizadas

- Java 11+
- Java Swing
- MySQL
- JDBC

## Estrutura do Projeto
src/
└── br/
└── com/
└── usuario/
├── SistemaUsuariosSwingSimples.java  # Classe principal com interface
├── Usuario.java                     # Modelo do usuário
└── UsuarioDAO.java                  # Classe de acesso ao banco

## Requisitos

- Java JDK 11 ou superior
- MySQL Server
- IDE recomendada: NetBeans ou IntelliJ IDEA

## Configuração do Banco de Dados

1.Crie o banco de dados no MySQL:

CREATE DATABASE sistema_usuarios;

2.Crie a tabela de usuários:

CREATE TABLE usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    senha VARCHAR(50) NOT NULL
);

## Como Rodar o Projeto

- Clone o repositório
- Abra o projeto na IDE NetBeans ou IntelliJ IDEA.
- Execute a classe principal (SistemaUsuariosSwingSimples.java).
- A interface gráfica será exibida, permitindo usar o sistema normalmente.

