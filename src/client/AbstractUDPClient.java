package client;

import common.logger.ChatLogger;
import common.socket.UDPSocketCommunication;
import common.socket.UDPUtil;

import java.io.IOException;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clase abstracta que proporciona una implementación base para un cliente UDP.
 * Gestiona la conexión al servidor, la desconexión y el envío de mensajes.
 */
public abstract class AbstractUDPClient implements Client {
    private static final Logger logger = ChatLogger.getLogger(AbstractUDPClient.class.getName());
    protected final InetSocketAddress serverAddress;  // Dirección del servidor
    protected final int userPort;  // Puerto del cliente
    protected DatagramSocket clientSocket;  // Socket UDP del cliente

    /**
     * Constructor para inicializar la dirección del servidor y el puerto del cliente.
     *
     * @param serverAddress Dirección del servidor al que se conectará el cliente.
     * @param userPort Puerto local del cliente.
     */
    public AbstractUDPClient(InetSocketAddress serverAddress, int userPort) {
        this.userPort = userPort;
        this.serverAddress = serverAddress;
    }

    /**
     * Establece la conexión con el servidor creando un socket UDP.
     */
    @Override
    public void connect() {
        try {
            clientSocket = UDPUtil.createSocket();  // Crear socket UDP
            System.out.println("Connected to server at " + serverAddress);
        } catch (SocketException e) {
            logger.log(Level.SEVERE, "Failed to connect: " + e.getMessage());
        }
    }

    /**
     * Cierra la conexión del cliente al servidor cerrando el socket UDP.
     */
    @Override
    public void disconnect() {
        if (clientSocket != null && !clientSocket.isClosed()) {
            clientSocket.close();  // Cerrar el socket UDP
            System.out.println("Disconnected from server.");
        }
    }

    /**
     * Método abstracto que debe ser implementado por las clases derivadas para manejar la lógica
     * específica de comenzar el cliente (por ejemplo, iniciar la interacción con el servidor).
     */
    protected abstract void start();

    /**
     * Método abstracto que debe ser implementado por las clases derivadas para manejar la recepción
     * de mensajes del servidor (por ejemplo, escuchando y procesando mensajes de otros clientes o el servidor).
     */
    protected abstract void listen();

    /**
     * Envía un mensaje al servidor a través de UDP.
     *
     * @param message El mensaje a enviar.
     * @throws IOException Si ocurre un error durante el envío del mensaje.
     */
    @Override
    public void sendMessage(String message) throws IOException {
        UDPSocketCommunication.sendMessage(clientSocket, message, serverAddress);  // Enviar el mensaje
    }
}
