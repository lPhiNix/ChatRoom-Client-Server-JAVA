package common.socket;

import common.logger.ChatLogger;

import java.io.IOException;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * La clase {@code UDPSocketCommunication} proporciona métodos para enviar y recibir mensajes
 * a través de un socket UDP en una red.
 *
 * Esta clase se encarga de gestionar la comunicación mediante el protocolo UDP,
 * permitiendo enviar mensajes a una dirección específica y recibir mensajes de otros nodos.
 */
public class UDPSocketCommunication {

    /** Logger utilizado para registrar los eventos y errores en la clase. */
    private static final Logger logger = ChatLogger.getLogger(UDPSocketCommunication.class.getName());

    /**
     * Envía un mensaje a través de un socket UDP.
     *
     * @param socket El socket UDP utilizado para enviar el mensaje.
     * @param message El mensaje que se desea enviar.
     * @param address La dirección a la que se enviará el mensaje (dirección IP y puerto).
     */
    public static void sendMessage(DatagramSocket socket, String message, InetSocketAddress address) {
        try {
            // Convierte el mensaje a un array de bytes
            byte[] buffer = message.getBytes();

            // Crea un paquete UDP con el mensaje, dirección IP y puerto del destinatario
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, InetAddress.getByName(address.getHostName()), address.getPort());

            // Envía el paquete
            socket.send(packet);
        } catch (IOException e) {
            // En caso de error, se registra el problema
            logger.log(Level.SEVERE, "Failed to send message: " + e.getMessage());
        }
    }

    /**
     * Recibe un mensaje a través de un socket UDP.
     *
     * @param socket El socket UDP desde el que se recibirá el mensaje.
     * @return El paquete UDP que contiene el mensaje recibido.
     * @throws IOException Si ocurre un error al recibir el mensaje.
     */
    public static DatagramPacket receiveMessage(DatagramSocket socket) throws IOException {
        // Se crea un buffer de 1024 bytes para recibir el mensaje
        byte[] buffer = new byte[1024];

        // Se crea un paquete UDP para almacenar el mensaje recibido
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

        // Recibe el paquete del socket
        socket.receive(packet);

        return packet;  // Retorna el paquete recibido
    }
}
