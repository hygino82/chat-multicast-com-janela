package br.dev.hygino;

import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;

public class JanelaChatUDP extends JFrame {

    private static BatePapoThread batePapoThread;
    private int porta;
    private String enderecoIp;
    private static String usuario;

    private JLabel lbPorta;
    private JTextField txtPorta;
    private JLabel lbEndereco;
    private JTextField txtEndereco;
    private JLabel lbUsuario;
    private JTextField txtUsuario;
    private JButton btnConectar;
    private JButton btnEnviar;
    private JLabel lbMensagem;
    private JTextField txtMensagem;
    private JPanel painelConfiguracoes;
    private JTextArea txtChat;
    private JPanel painelInferior;

    public JanelaChatUDP() {
        configurarJanela();
    }

    private void configurarJanela() {
        // Usando BorderLayout para organizar melhor os componentes
        setLayout(new BorderLayout());

        // Painel para configurações de conexão (superior)
        painelConfiguracoes = new JPanel(new GridLayout(5, 2, 10, 10)); // Grid com 5 linhas e 2 colunas

        lbPorta = new JLabel("Porta:");
        txtPorta = new JTextField("53000");
        lbEndereco = new JLabel("Endereço:");
        txtEndereco = new JTextField("224.1.1.20");
        lbUsuario = new JLabel("Usuário:");
        txtUsuario = new JTextField("Juvenal");
        btnConectar = new JButton("Conectar");

        painelConfiguracoes.add(lbPorta);
        painelConfiguracoes.add(txtPorta);
        painelConfiguracoes.add(lbEndereco);
        painelConfiguracoes.add(txtEndereco);
        painelConfiguracoes.add(lbUsuario);
        painelConfiguracoes.add(txtUsuario);
        painelConfiguracoes.add(btnConectar);

        // Adicionando o painel de configurações ao topo (NORTH)
        add(painelConfiguracoes, BorderLayout.NORTH);

        // Área de texto para o chat (centro)
        txtChat = new JTextArea(20, 50);
        txtChat.setEditable(false);  // Desativar a edição na área do chat
        JScrollPane scrollChat = new JScrollPane(txtChat); // Adicionando rolagem

        // Adicionando a área de chat ao centro
        add(scrollChat, BorderLayout.CENTER);

        // Painel inferior com o campo de mensagem e botão enviar (sul)
        painelInferior = new JPanel(new BorderLayout());

        lbMensagem = new JLabel("Digite a sua mensagem:");
        txtMensagem = new JTextField();
        btnEnviar = new JButton("Enviar");

        painelInferior.add(lbMensagem, BorderLayout.WEST);
        painelInferior.add(txtMensagem, BorderLayout.CENTER);
        painelInferior.add(btnEnviar, BorderLayout.EAST);

        btnConectar.addActionListener((ActionEvent e) -> {
            try {
                porta = Integer.parseInt(txtPorta.getText());
                enderecoIp = txtEndereco.getText();
                usuario = txtUsuario.getText();
                //SwingUtilities.invokeLater(() ->
                batePapoThread = new BatePapoThread(txtChat, usuario, porta, enderecoIp);
                batePapoThread.start(); //);
            } catch (NumberFormatException nfe) {
                System.out.println("erro");
            }
        });

        btnEnviar.addActionListener((ActionEvent e) -> {
            enviarMensagem();
        });

        txtMensagem.addActionListener((ActionEvent e) -> {
            enviarMensagem();
        });

        // Adicionando o painel inferior ao sul
        add(painelInferior, BorderLayout.SOUTH);

        // Configurações gerais da janela
        setTitle("Chat Multicast");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void enviarMensagem() {
        String mensagemEnviar = txtMensagem.getText();
        SwingUtilities.invokeLater(() -> batePapoThread.enviarMensagem(mensagemEnviar));
        txtMensagem.setText("");
    }

    public static void main(String[] args) {
        new JanelaChatUDP();
    }

}
