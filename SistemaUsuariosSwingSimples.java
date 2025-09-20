package com.exemplo.sistemausuarios;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class SistemaUsuariosSwingSimples extends JFrame {
    
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    

    private JTable tabelaUsuarios;
    private DefaultTableModel modeloTabela;
    private JTextField campoNome, campoEmail, campoIdade, campoBusca;
    private JButton btnInserir, btnAtualizar, btnDeletar, btnBuscar, btnLimpar, btnEstatisticas;
    private JLabel lblStatus;
    
    public SistemaUsuariosSwingSimples() {
        inicializarInterface();
        configurarEventos();
        atualizarTabela();
        testarConexao();
    }
    
    private void inicializarInterface() {
        setTitle("Sistema de Usuarios - MySQL");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setSize(900, 600);
        setLocationRelativeTo(null);
        
        
        JPanel painelPrincipal = new JPanel(new BorderLayout(10, 10));
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
   
        JPanel painelFormulario = criarPainelFormulario();
        painelPrincipal.add(painelFormulario, BorderLayout.NORTH);
        
       
        JPanel painelTabela = criarPainelTabela();
        painelPrincipal.add(painelTabela, BorderLayout.CENTER);
        
       
        JPanel painelStatus = criarPainelStatus();
        painelPrincipal.add(painelStatus, BorderLayout.SOUTH);
        
        add(painelPrincipal);
    }
    
    private JPanel criarPainelFormulario() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBorder(BorderFactory.createTitledBorder("Gerenciar Usuarios"));
        
        
        JPanel painelCampos = new JPanel(new GridLayout(3, 6, 5, 5));
        
       
        painelCampos.add(new JLabel("Nome:"));
        campoNome = new JTextField();
        painelCampos.add(campoNome);
        
        painelCampos.add(new JLabel("Email:"));
        campoEmail = new JTextField();
        painelCampos.add(campoEmail);
        
        painelCampos.add(new JLabel("Idade:"));
        campoIdade = new JTextField();
        painelCampos.add(campoIdade);
        
     
        painelCampos.add(new JLabel("Buscar (ID/Email):"));
        campoBusca = new JTextField();
        campoBusca.setToolTipText("Digite ID ou email para buscar");
        painelCampos.add(campoBusca);
        
 
        painelCampos.add(new JLabel(""));
        painelCampos.add(new JLabel(""));
        painelCampos.add(new JLabel(""));
        painelCampos.add(new JLabel(""));
        
        painel.add(painelCampos, BorderLayout.CENTER);
        
      
        JPanel painelBotoes = new JPanel(new FlowLayout());
        
        btnInserir = new JButton("Inserir");
        btnInserir.setBackground(Color.GREEN);
        btnInserir.setForeground(Color.WHITE);
        
        btnAtualizar = new JButton("Atualizar");
        btnAtualizar.setBackground(Color.BLUE);
        btnAtualizar.setForeground(Color.WHITE);
        btnAtualizar.setEnabled(false);
        
        btnDeletar = new JButton("Deletar");
        btnDeletar.setBackground(Color.RED);
        btnDeletar.setForeground(Color.WHITE);
        btnDeletar.setEnabled(false);
        
        btnBuscar = new JButton("Buscar");
        btnBuscar.setBackground(Color.ORANGE);
        btnBuscar.setForeground(Color.BLACK);
        
        btnLimpar = new JButton("Limpar");
        btnLimpar.setBackground(Color.GRAY);
        btnLimpar.setForeground(Color.WHITE);
        
        btnEstatisticas = new JButton("Estatisticas");
        btnEstatisticas.setBackground(Color.MAGENTA);
        btnEstatisticas.setForeground(Color.WHITE);
        
        painelBotoes.add(btnInserir);
        painelBotoes.add(btnAtualizar);
        painelBotoes.add(btnDeletar);
        painelBotoes.add(btnBuscar);
        painelBotoes.add(btnLimpar);
        painelBotoes.add(btnEstatisticas);
        
        painel.add(painelBotoes, BorderLayout.SOUTH);
        
        return painel;
    }
    
    private JPanel criarPainelTabela() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBorder(BorderFactory.createTitledBorder("Lista de Usuarios"));
        
        
        String[] colunas = {"ID", "Nome", "Email", "Idade"};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tabelaUsuarios = new JTable(modeloTabela);
        tabelaUsuarios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JScrollPane scrollPane = new JScrollPane(tabelaUsuarios);
        scrollPane.setPreferredSize(new Dimension(800, 300));
        
        painel.add(scrollPane, BorderLayout.CENTER);
        
        return painel;
    }
    
    private JPanel criarPainelStatus() {
        JPanel painel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        lblStatus = new JLabel("Carregando...");
        painel.add(lblStatus);
        return painel;
    }
    
    private void configurarEventos() {
     
        tabelaUsuarios.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int linhaSelecionada = tabelaUsuarios.getSelectedRow();
                if (linhaSelecionada >= 0) {
                    campoNome.setText(tabelaUsuarios.getValueAt(linhaSelecionada, 1).toString());
                    campoEmail.setText(tabelaUsuarios.getValueAt(linhaSelecionada, 2).toString());
                    campoIdade.setText(tabelaUsuarios.getValueAt(linhaSelecionada, 3).toString());
                    
                    btnAtualizar.setEnabled(true);
                    btnDeletar.setEnabled(true);
                } else {
                    limparCampos();
                    btnAtualizar.setEnabled(false);
                    btnDeletar.setEnabled(false);
                }
            }
        });
        
        btnInserir.addActionListener(e -> inserirUsuario());
        btnAtualizar.addActionListener(e -> atualizarUsuario());
        btnDeletar.addActionListener(e -> deletarUsuario());
        btnBuscar.addActionListener(e -> buscarUsuario());
        btnLimpar.addActionListener(e -> {
            limparCampos();
            atualizarTabela();
            tabelaUsuarios.clearSelection();
        });
        btnEstatisticas.addActionListener(e -> exibirEstatisticas());
    }
    
    private void inserirUsuario() {
        try {
            String nome = campoNome.getText().trim();
            String email = campoEmail.getText().trim();
            String idadeStr = campoIdade.getText().trim();
            
            if (nome.isEmpty()) {
                mostrarErro("Nome nao pode estar vazio!");
                return;
            }
            
            if (email.isEmpty() || !email.contains("@")) {
                mostrarErro("Email invalido!");
                return;
            }
            
            if (idadeStr.isEmpty()) {
                mostrarErro("Idade nao pode estar vazia!");
                return;
            }
            
            int idade = Integer.parseInt(idadeStr);
            if (idade <= 0 || idade > 150) {
                mostrarErro("Idade deve estar entre 1 e 150 anos!");
                return;
            }
            
            Usuario novoUsuario = new Usuario(nome, email, idade);
            
            if (usuarioDAO.inserir(novoUsuario)) {
                mostrarSucesso("Usuario cadastrado com sucesso!");
                limparCampos();
                atualizarTabela();
                atualizarStatus();
            } else {
                mostrarErro("Falha ao cadastrar usuario!");
            }
            
        } catch (NumberFormatException ex) {
            mostrarErro("Idade deve ser um numero valido!");
        } catch (Exception ex) {
            mostrarErro("Erro ao inserir usuario: " + ex.getMessage());
        }
    }
    
    private void atualizarUsuario() {
        int linhaSelecionada = tabelaUsuarios.getSelectedRow();
        if (linhaSelecionada < 0) {
            mostrarErro("Selecione um usuario para atualizar!");
            return;
        }
        
        try {
            int id = (Integer) tabelaUsuarios.getValueAt(linhaSelecionada, 0);
            String nome = campoNome.getText().trim();
            String email = campoEmail.getText().trim();
            String idadeStr = campoIdade.getText().trim();
            
            if (nome.isEmpty() || email.isEmpty() || idadeStr.isEmpty()) {
                mostrarErro("Todos os campos devem ser preenchidos!");
                return;
            }
            
            if (!email.contains("@")) {
                mostrarErro("Email invalido!");
                return;
            }
            
            int idade = Integer.parseInt(idadeStr);
            if (idade <= 0 || idade > 150) {
                mostrarErro("Idade deve estar entre 1 e 150 anos!");
                return;
            }
            
            Usuario usuario = new Usuario(id, nome, email, idade);
            
            if (usuarioDAO.atualizar(usuario)) {
                mostrarSucesso("Usuario atualizado com sucesso!");
                atualizarTabela();
                atualizarStatus();
            } else {
                mostrarErro("Falha ao atualizar usuario!");
            }
            
        } catch (NumberFormatException ex) {
            mostrarErro("Idade deve ser um numero valido!");
        } catch (Exception ex) {
            mostrarErro("Erro ao atualizar usuario: " + ex.getMessage());
        }
    }
    
    private void deletarUsuario() {
        int linhaSelecionada = tabelaUsuarios.getSelectedRow();
        if (linhaSelecionada < 0) {
            mostrarErro("Selecione um usuario para deletar!");
            return;
        }
        
        int id = (Integer) tabelaUsuarios.getValueAt(linhaSelecionada, 0);
        String nome = tabelaUsuarios.getValueAt(linhaSelecionada, 1).toString();
        
        int opcao = JOptionPane.showConfirmDialog(
            this,
            "Tem certeza que deseja deletar o usuario '" + nome + "'?",
            "Confirmar Exclusao",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );
        
        if (opcao == JOptionPane.YES_OPTION) {
            if (usuarioDAO.deletar(id)) {
                mostrarSucesso("Usuario deletado com sucesso!");
                limparCampos();
                atualizarTabela();
                atualizarStatus();
                tabelaUsuarios.clearSelection();
            } else {
                mostrarErro("Falha ao deletar usuario!");
            }
        }
    }
    
    private void buscarUsuario() {
        String termo = campoBusca.getText().trim();
        
        if (termo.isEmpty()) {
            atualizarTabela();
            return;
        }
        
        modeloTabela.setRowCount(0);
        Usuario usuario = null;
        
        try {
            int id = Integer.parseInt(termo);
            usuario = usuarioDAO.buscarPorId(id);
        } catch (NumberFormatException e) {
            usuario = usuarioDAO.buscarPorEmail(termo);
        }
        
        if (usuario != null) {
            Object[] linha = {usuario.getId(), usuario.getNome(), usuario.getEmail(), usuario.getIdade()};
            modeloTabela.addRow(linha);
            lblStatus.setText("1 usuario encontrado");
        } else {
            lblStatus.setText("Nenhum usuario encontrado");
        }
    }
    
    private void exibirEstatisticas() {
        List<Usuario> usuarios = usuarioDAO.buscarTodos();
        int total = usuarios.size();
        
        if (total == 0) {
            JOptionPane.showMessageDialog(
                this,
                "Nao ha usuarios cadastrados no sistema.",
                "Estatisticas",
                JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }
        
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
        
        Usuario maisJovem = usuarios.stream()
                .min((u1, u2) -> Integer.compare(u1.getIdade(), u2.getIdade()))
                .orElse(null);
        
        Usuario maisVelho = usuarios.stream()
                .max((u1, u2) -> Integer.compare(u1.getIdade(), u2.getIdade()))
                .orElse(null);
        
        StringBuilder mensagem = new StringBuilder();
        mensagem.append("ESTATISTICAS DO SISTEMA\n\n");
        mensagem.append("Total de usuarios: ").append(total).append("\n\n");
        mensagem.append("Idade media: ").append(String.format("%.1f", idadeMedia)).append(" anos\n");
        mensagem.append("Idade minima: ").append(idadeMinima).append(" anos\n");
        mensagem.append("Idade maxima: ").append(idadeMaxima).append(" anos\n\n");
        
        if (maisJovem != null) {
            mensagem.append("Mais jovem: ").append(maisJovem.getNome())
                    .append(" (").append(maisJovem.getIdade()).append(" anos)\n");
        }
        
        if (maisVelho != null) {
            mensagem.append("Mais velho: ").append(maisVelho.getNome())
                    .append(" (").append(maisVelho.getIdade()).append(" anos)\n");
        }
        
        mensagem.append("\nStatus da conexao: ");
        if (usuarioDAO.testarConexao()) {
            mensagem.append("Conectado");
        } else {
            mensagem.append("Desconectado");
        }
        
        JOptionPane.showMessageDialog(
            this,
            mensagem.toString(),
            "Estatisticas do Sistema",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
    
    private void atualizarTabela() {
        modeloTabela.setRowCount(0);
        
        List<Usuario> usuarios = usuarioDAO.buscarTodos();
        
        for (Usuario usuario : usuarios) {
            Object[] linha = {usuario.getId(), usuario.getNome(), usuario.getEmail(), usuario.getIdade()};
            modeloTabela.addRow(linha);
        }
        
        atualizarStatus();
    }
    
    private void atualizarStatus() {
        int total = usuarioDAO.contarUsuarios();
        boolean conectado = usuarioDAO.testarConexao();
        
        if (conectado) {
            lblStatus.setText("Conectado | Total: " + total + " usuario(s)");
        } else {
            lblStatus.setText("Desconectado | Sem acesso ao banco de dados");
        }
    }
    
    private void testarConexao() {
        if (usuarioDAO.testarConexao()) {
            atualizarStatus();
        } else {
            lblStatus.setText("Erro de conexao com o banco de dados");
            mostrarErro("Nao foi possivel conectar ao banco de dados!\nVerifique as configuracoes de conexao.");
        }
    }
    
    private void limparCampos() {
        campoNome.setText("");
        campoEmail.setText("");
        campoIdade.setText("");
        campoBusca.setText("");
    }
    
    private void mostrarSucesso(String mensagem) {
        JOptionPane.showMessageDialog(this, mensagem, "Sucesso", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void mostrarErro(String mensagem) {
        JOptionPane.showMessageDialog(this, mensagem, "Erro", JOptionPane.ERROR_MESSAGE);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new SistemaUsuariosSwingSimples().setVisible(true);
        });
    }
}
