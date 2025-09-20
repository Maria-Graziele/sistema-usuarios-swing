package com.exemplo.sistemausuarios;

import java.util.List;
import java.util.Scanner;

public class SistemaUsuarios {
    
    private static final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private static final Scanner scanner = new Scanner(System.in);
    
    
    private static final String RESET = "\u001B[0m";
    private static final String GREEN = "\u001B[32m";
    private static final String RED = "\u001B[31m";
    private static final String BLUE = "\u001B[34m";
    private static final String YELLOW = "\u001B[33m";
    private static final String CYAN = "\u001B[36m";
    
    public static void main(String[] args) {
        exibirBoasVindas();
        
        
        if (!testarSistema()) {
            System.out.println(RED + "❌ Sistema não pode iniciar devido a problemas de conexão." + RESET);
            return;
        }
        
     
        executarMenuPrincipal();
        
        System.out.println(CYAN + "\n👋 Obrigado por usar o Sistema de Usuários!" + RESET);
        scanner.close();
    }
    
    private static void exibirBoasVindas() {
        System.out.println(CYAN + "╔══════════════════════════════════════╗");
        System.out.println("║        SISTEMA DE USUÁRIOS           ║");
        System.out.println("║          MySQL + Java               ║");
        System.out.println("╚══════════════════════════════════════╝" + RESET);
        System.out.println();
    }
    
   
    private static boolean testarSistema() {
        System.out.println(YELLOW + "🔍 Testando conexão com o banco de dados..." + RESET);
        
        boolean conexaoOk = usuarioDAO.testarConexao();
        
        if (conexaoOk) {
            int totalUsuarios = usuarioDAO.contarUsuarios();
            System.out.println(GREEN + "✅ Sistema pronto! Total de usuários: " + totalUsuarios + RESET);
            return true;
        }
        
        return false;
    }
    
    
    private static void executarMenuPrincipal() {
        while (true) {
            exibirMenu();
            
            int opcao = lerOpcaoMenu();
            
            if (opcao == 0) {
                break; 
            }
            
            processarOpcaoMenu(opcao);
            
            pausarExecucao();
        }
    }
   
    private static void exibirMenu() {
        System.out.println("\n" + BLUE + "═══════════════ MENU PRINCIPAL ═══════════════" + RESET);
        System.out.println("1️⃣  " + GREEN + "Inserir usuário" + RESET);
        System.out.println("2️⃣  " + CYAN + "Listar todos os usuários" + RESET);
        System.out.println("3️⃣  " + YELLOW + "Buscar usuário por ID" + RESET);
        System.out.println("4️⃣  " + BLUE + "Atualizar usuário" + RESET);
        System.out.println("5️⃣  " + RED + "Deletar usuário" + RESET);
        System.out.println("6️⃣  " + CYAN + "Buscar usuário por email" + RESET);
        System.out.println("7️⃣  " + YELLOW + "Estatísticas do sistema" + RESET);
        System.out.println("0️⃣  " + "Sair");
        System.out.println(BLUE + "════════════════════════════════════════════" + RESET);
        System.out.print("🎯 Escolha uma opção: ");
    }
    
    private static int lerOpcaoMenu() {
        try {
            int opcao = scanner.nextInt();
            scanner.nextLine(); 
            
            if (opcao < 0 || opcao > 7) {
                System.out.println(RED + "❌ Opção inválida! Digite um número entre 0 e 7." + RESET);
                return -1;
            }
            
            return opcao;
            
        } catch (Exception e) {
            System.out.println(RED + "❌ Entrada inválida! Digite apenas números." + RESET);
            scanner.nextLine(); 
            return -1;
        }
    }
    
    private static void processarOpcaoMenu(int opcao) {
        switch (opcao) {
            case 1:
                inserirUsuario();
                break;
            case 2:
                listarTodosUsuarios();
                break;
            case 3:
                buscarUsuarioPorId();
                break;
            case 4:
                atualizarUsuario();
                break;
            case 5:
                deletarUsuario();
                break;
            case 6:
                buscarUsuarioPorEmail();
                break;
            case 7:
                exibirEstatisticas();
                break;
            default:
                System.out.println(RED + "❌ Opção inválida!" + RESET);
        }
    }
    
    
    private static void inserirUsuario() {
        System.out.println(GREEN + "\n➕ ═══ INSERIR USUÁRIO ═══" + RESET);
        
        try {
            System.out.print("👤 Nome: ");
            String nome = scanner.nextLine().trim();
            
            if (nome.isEmpty()) {
                System.out.println(RED + "❌ Nome não pode estar vazio!" + RESET);
                return;
            }
            
            System.out.print("📧 Email: ");
            String email = scanner.nextLine().trim();
            
            if (email.isEmpty() || !email.contains("@")) {
                System.out.println(RED + "❌ Email inválido!" + RESET);
                return;
            }
            
            System.out.print("🎂 Idade: ");
            int idade = scanner.nextInt();
            scanner.nextLine(); // Consumir quebra de linha
            
            if (idade <= 0 || idade > 150) {
                System.out.println(RED + "❌ Idade deve estar entre 1 e 150 anos!" + RESET);
                return;
            }
            
            Usuario novoUsuario = new Usuario(nome, email, idade);
            
            if (usuarioDAO.inserir(novoUsuario)) {
                System.out.println(GREEN + "🎉 Usuário cadastrado com sucesso!" + RESET);
                System.out.println("📋 Dados: " + novoUsuario.toStringFormatado());
            } else {
                System.out.println(RED + "❌ Falha ao cadastrar usuário!" + RESET);
            }
            
        } catch (Exception e) {
            System.out.println(RED + "❌ Erro ao inserir usuário: " + e.getMessage() + RESET);
            scanner.nextLine(); 
        }
    }
    

    private static void listarTodosUsuarios() {
        System.out.println(CYAN + "\n📋 ═══ LISTA DE USUÁRIOS ═══" + RESET);
        
        List<Usuario> usuarios = usuarioDAO.buscarTodos();
        
        if (usuarios.isEmpty()) {
            System.out.println(YELLOW + "📭 Nenhum usuário cadastrado no sistema." + RESET);
        } else {
            System.out.println(GREEN + "Total: " + usuarios.size() + " usuário(s)" + RESET);
            System.out.println();
            
            for (Usuario usuario : usuarios) {
                System.out.println("🔹 " + usuario.toStringFormatado());
            }
        }
    }
    
    private static void buscarUsuarioPorId() {
        System.out.println(YELLOW + "\n🔍 ═══ BUSCAR POR ID ═══" + RESET);
        
        try {
            System.out.print("🆔 Digite o ID: ");
            int id = scanner.nextInt();
            scanner.nextLine(); 
            
            Usuario usuario = usuarioDAO.buscarPorId(id);
            
            if (usuario != null) {
                System.out.println(GREEN + "✅ Usuário encontrado:" + RESET);
                System.out.println("📋 " + usuario.toStringFormatado());
            } else {
                System.out.println(RED + "❌ Usuário com ID " + id + " não encontrado!" + RESET);
            }
            
        } catch (Exception e) {
            System.out.println(RED + "❌ Erro ao buscar usuário: " + e.getMessage() + RESET);
            scanner.nextLine(); // Limpar buffer
        }
    }
    
    private static void buscarUsuarioPorEmail() {
        System.out.println(YELLOW + "\n🔍 ═══ BUSCAR POR EMAIL ═══" + RESET);
        
        System.out.print("📧 Digite o email: ");
        String email = scanner.nextLine().trim();
        
        if (email.isEmpty()) {
            System.out.println(RED + "❌ Email não pode estar vazio!" + RESET);
            return;
        }
        
        Usuario usuario = usuarioDAO.buscarPorEmail(email);
        
        if (usuario != null) {
            System.out.println(GREEN + "✅ Usuário encontrado:" + RESET);
            System.out.println("📋 " + usuario.toStringFormatado());
        } else {
            System.out.println(RED + "❌ Usuário com email '" + email + "' não encontrado!" + RESET);
        }
    }
    
   
    private static void atualizarUsuario() {
        System.out.println(BLUE + "\n✏️ ═══ ATUALIZAR USUÁRIO ═══" + RESET);
        
        try {
            System.out.print("🆔 Digite o ID do usuário: ");
            int id = scanner.nextInt();
            scanner.nextLine(); 
            
            Usuario usuario = usuarioDAO.buscarPorId(id);
            if (usuario == null) {
                System.out.println(RED + "❌ Usuário não encontrado!" + RESET);
                return;
            }
            
            System.out.println(YELLOW + "📋 Usuário atual: " + usuario.toStringFormatado() + RESET);
            System.out.println(CYAN + "💡 Deixe em branco para manter o valor atual" + RESET);
            
           
            System.out.print("👤 Novo nome (atual: " + usuario.getNome() + "): ");
            String novoNome = scanner.nextLine().trim();
            if (!novoNome.isEmpty()) {
                usuario.setNome(novoNome);
            }
            
          
            System.out.print("📧 Novo email (atual: " + usuario.getEmail() + "): ");
            String novoEmail = scanner.nextLine().trim();
            if (!novoEmail.isEmpty()) {
                if (!novoEmail.contains("@")) {
                    System.out.println(RED + "❌ Email inválido!" + RESET);
                    return;
                }
                usuario.setEmail(novoEmail);
            }
            
            System.out.print("🎂 Nova idade (atual: " + usuario.getIdade() + "): ");
            String idadeStr = scanner.nextLine().trim();
            if (!idadeStr.isEmpty()) {
                try {
                    int novaIdade = Integer.parseInt(idadeStr);
                    if (novaIdade <= 0 || novaIdade > 150) {
                        System.out.println(RED + "❌ Idade deve estar entre 1 e 150 anos!" + RESET);
                        return;
                    }
                    usuario.setIdade(novaIdade);
                } catch (NumberFormatException e) {
                    System.out.println(RED + "❌ Idade inválida!" + RESET);
                    return;
                }
            }
            
            if (usuarioDAO.atualizar(usuario)) {
                System.out.println(GREEN + "🎉 Usuário atualizado com sucesso!" + RESET);
                System.out.println("📋 Novos dados: " + usuario.toStringFormatado());
            } else {
                System.out.println(RED + "❌ Falha ao atualizar usuário!" + RESET);
            }
            
        } catch (Exception e) {
            System.out.println(RED + "❌ Erro ao atualizar usuário: " + e.getMessage() + RESET);
            scanner.nextLine(); 
        }
    }
    
   
    private static void deletarUsuario() {
        System.out.println(RED + "\n🗑️ ═══ DELETAR USUÁRIO ═══" + RESET);
        
        try {
            System.out.print("🆔 Digite o ID do usuário: ");
            int id = scanner.nextInt();
            scanner.nextLine(); // Consumir quebra de linha
            
            Usuario usuario = usuarioDAO.buscarPorId(id);
            if (usuario == null) {
                System.out.println(RED + "❌ Usuário não encontrado!" + RESET);
                return;
            }
            
            System.out.println(YELLOW + "⚠️ Usuário a ser deletado: " + usuario.toStringFormatado() + RESET);
            System.out.print(RED + "⚠️ Tem certeza que deseja deletar? (s/N): " + RESET);
            String confirmacao = scanner.nextLine().trim();
            
            if (confirmacao.equalsIgnoreCase("s") || confirmacao.equalsIgnoreCase("sim")) {
                if (usuarioDAO.deletar(id)) {
                    System.out.println(GREEN + "🗑️ Usuário deletado com sucesso!" + RESET);
                } else {
                    System.out.println(RED + "❌ Falha ao deletar usuário!" + RESET);
                }
            } else {
                System.out.println(YELLOW + "❌ Operação cancelada." + RESET);
            }
            
        } catch (Exception e) {
            System.out.println(RED + "❌ Erro ao deletar usuário: " + e.getMessage() + RESET);
            scanner.nextLine(); // Limpar buffer
        }
    }
    
    private static void exibirEstatisticas() {
        System.out.println(CYAN + "\n📊 ═══ ESTATÍSTICAS DO SISTEMA ═══" + RESET);
        
        int totalUsuarios = usuarioDAO.contarUsuarios();
        List<Usuario> usuarios = usuarioDAO.buscarTodos();
        
        System.out.println("👥 Total de usuários: " + GREEN + totalUsuarios + RESET);
        
        if (totalUsuarios > 0) {

            double idadeMedia = usuarios.stream()
                    .mapToInt(Usuario::getIdade)
                    .average()
                    .orElse(0);
            
            int idadeMinima = usuarios.stream()
                    .mapToInt(Usuario::getIdade)
                    .min()
                    .orElse(0);
            
            int idadeMaxima = usuarios.stream()
                    .mapToInt(Usuario::getIdade)
                    .max()
                    .orElse(0);
            
            System.out.printf("🎂 Idade média: " + YELLOW + "%.1f anos" + RESET + "\n", idadeMedia);
            System.out.println("🔽 Idade mínima: " + BLUE + idadeMinima + " anos" + RESET);
            System.out.println("🔼 Idade máxima: " + RED + idadeMaxima + " anos" + RESET);
            
            Usuario maisJovem = usuarios.stream()
                    .min((u1, u2) -> Integer.compare(u1.getIdade(), u2.getIdade()))
                    .orElse(null);
            
            Usuario maisVelho = usuarios.stream()
                    .max((u1, u2) -> Integer.compare(u1.getIdade(), u2.getIdade()))
                    .orElse(null);
            
            if (maisJovem != null) {
                System.out.println("👶 Mais jovem: " + GREEN + maisJovem.getNome() + 
                                 " (" + maisJovem.getIdade() + " anos)" + RESET);
            }
            
            if (maisVelho != null) {
                System.out.println("👴 Mais velho: " + BLUE + maisVelho.getNome() + 
                                 " (" + maisVelho.getIdade() + " anos)" + RESET);
            }
        }
        
        if (usuarioDAO.testarConexao()) {
            System.out.println("🔗 Status da conexão: " + GREEN + "✅ Conectado" + RESET);
        } else {
            System.out.println("🔗 Status da conexão: " + RED + "❌ Desconectado" + RESET);
        }
    }
    
    private static void pausarExecucao() {
        System.out.println(YELLOW + "\n⏸️ Pressione Enter para continuar..." + RESET);
        scanner.nextLine();
    }
}
