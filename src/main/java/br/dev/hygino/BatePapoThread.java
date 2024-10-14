package br.dev.hygino;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextArea;

public class BatePapoThread extends Thread {

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    private static MulticastSocket conexao;
    private JTextArea txtChat;
    private String usuario;
    private int porta;
    private String enderecoIp;

    public BatePapoThread(JTextArea txtChat, String usuario, int porta, String enderecoIp) {
        this.txtChat = txtChat;
        this.usuario = usuario;
        this.porta = porta;
        this.enderecoIp = enderecoIp;
    }

    @Override
    public void run() {
        try {
            txtChat.append(usuario);
            InetAddress endereco = InetAddress.getByName(enderecoIp);
            conexao = new MulticastSocket(porta);
            // LocalTime now = LocalTime.now();
            //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            // String horaFormatada = now.format(formatter);
            txtChat.append(" entrou na sala!\n");

            conexao.joinGroup(new InetSocketAddress(endereco, porta),
                    NetworkInterface.getByInetAddress(endereco));

            receberMensagens();
            conexao.close();
        } catch (SocketException ex) {
            Logger.getLogger(BatePapoThread.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(BatePapoThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void enviarMensagem(String mensagem) {
        try {
            String horaAtual = LocalTime.now().format(formatter);

            String resposta = String.format("[%s] %s diz: %s", horaAtual, usuario, mensagem);
            byte[] bufferEntrada = bufferEntrada = resposta.getBytes();

            DatagramPacket datagramaEnvio = new DatagramPacket(bufferEntrada, bufferEntrada.length, InetAddress.getByName(enderecoIp), porta);

            conexao.send(datagramaEnvio);
        } catch (IOException ex) {
            Logger.getLogger(BatePapoThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void receberMensagens() {
        while (true) {
            try {
                byte[] bufferEntrada = new byte[256];
                DatagramPacket datagramaEntrada = new DatagramPacket(bufferEntrada, bufferEntrada.length);

                conexao.receive(datagramaEntrada);

                String resposta = new String(datagramaEntrada.getData());

                if (!resposta.contains(usuario + " diz:")) {
                    txtChat.append(resposta + '\n');
                }
            } catch (IOException ex) {
                Logger.getLogger(BatePapoThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
