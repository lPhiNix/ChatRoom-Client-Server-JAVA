package test;

import common.model.User;
import java.net.*;
import java.util.Scanner;

public class PabloClient {
    private static final int PUERTO_SERVIDOR = 9876;
    private static final String IP_SERVIDOR = "localhost";

    public static void main(String[] args) {
        try (DatagramSocket socket = new DatagramSocket()) {
            Scanner scanner = new Scanner(System.in);

            // Iniciar sesión
            System.out.println("Introduce tu nombre de usuario:");
            String nombreUsuario = scanner.nextLine();
            System.out.println("Introduce tu contraseña:");
            String contraseñaUsuario = scanner.nextLine();
            String ipUsuario = InetAddress.getLocalHost().getHostAddress();  // Obtener la IP local del cliente

            // Enviar el mensaje de autenticación al servidor
            String mensaje = nombreUsuario + "," + contraseñaUsuario + "," + ipUsuario;
            byte[] sendData = mensaje.getBytes();
            InetAddress servidorAddress = InetAddress.getByName(IP_SERVIDOR);
            DatagramPacket packet = new DatagramPacket(sendData, sendData.length, servidorAddress, PUERTO_SERVIDOR);
            socket.send(packet);
            System.out.println("Enviando solicitud de conexión al servidor...");

            // Esperar respuesta del servidor
            byte[] receiveData = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            socket.receive(receivePacket);
            String response = new String(receivePacket.getData(), 0, receivePacket.getLength());
            System.out.println("Respuesta del servidor: " + response);

            if (response.startsWith("Bienvenido")) {
                // Si la conexión es exitosa, permitir al usuario enviar mensajes
                while (true) {
                    System.out.println("Escribe un mensaje (escribe 'salir' para desconectarte):");
                    String mensajeChat = scanner.nextLine();

                    if (mensajeChat.equalsIgnoreCase("salir")) {
                        // Salir del chat
                        System.out.println("Saliendo del chat...");
                        break;
                    }

                    // Enviar mensaje al servidor
                    sendData = mensajeChat.getBytes();
                    packet = new DatagramPacket(sendData, sendData.length, servidorAddress, PUERTO_SERVIDOR);
                    socket.send(packet);
                }
            }

            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
