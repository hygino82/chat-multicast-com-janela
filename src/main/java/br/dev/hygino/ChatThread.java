package br.dev.hygino;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ChatThread extends Thread {

    private int porta;
    private String enderecoIp;
    private String nomeUsuario;
    private JTextField txtmensagem;
    private JTextArea janelaMensagens;

    public ChatThread(int porta, String enderecoIp, String nomeUsuario, JTextField txtmensagem, JTextArea janelamensagens) {
        this.porta = porta;
        this.enderecoIp = enderecoIp;
        this.nomeUsuario = nomeUsuario;
        this.txtmensagem = txtmensagem;
        this.janelaMensagens = janelamensagens;
    }

    @Override
    public void run() {
        LocalTime now = LocalTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        String horaFormatada = now.format(formatter);
        janelaMensagens.append(horaFormatada + ": Usuário " + nomeUsuario + " entrou na sala!\n");
    }

    private void outro() {
        //receber mensagens dos demais usuários
        try {
            byte[] msg = new byte[128];

            MulticastSocket conexao = new MulticastSocket(porta);
            conexao.joinGroup(new InetSocketAddress(InetAddress.getByName(enderecoIp), porta),
                    NetworkInterface.getByInetAddress(InetAddress.getByName(enderecoIp)));
            while (true) {
                DatagramPacket datagrama = new DatagramPacket(msg, msg.length);
                conexao.receive(datagrama);

                String mensagem = new String(datagrama.getData());

                if (!mensagem.contains(nomeUsuario + " diz: ")) {
                    System.out.println('\n' + mensagem);
                    System.out.print("Digite a mensagem: ");
                }
                msg = new byte[128];
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

}
