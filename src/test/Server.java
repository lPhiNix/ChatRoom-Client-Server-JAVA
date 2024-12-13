package test;

import common.model.User;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

public class Server {
    private static final Map<String, User> usuariosConectados = new HashMap<>();
    private static final int PUERTO = 9876;

    public static void main(String[] args) {
        try (DatagramSocket socket = new DatagramSocket(PUERTO)) {
            System.out.println("Servidor UDP esperando en puerto " + PUERTO);
            byte[] receiveData = new byte[1024];

            while (true) {
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                socket.receive(receivePacket);

                InetAddress clientAddress = receivePacket.getAddress();
                int clientPort = receivePacket.getPort();
                String receivedMessage = new String(receivePacket.getData(), 0, receivePacket.getLength());

                System.out.println("Mensaje recibido: " + receivedMessage);

                // Delegamos la lógica de manejo al ConnectionHandler
                String response = ConnectionHandler.handleConnection(receivedMessage, clientAddress, clientPort);

                // Enviar respuesta al cliente
                socket.send(new DatagramPacket(response.getBytes(), response.length(), clientAddress, clientPort));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
