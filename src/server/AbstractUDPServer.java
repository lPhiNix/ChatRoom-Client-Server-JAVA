package server;

import common.socket.UDPSocketCommunication;
import common.socket.UDPUtil;
import common.command.CommandFactory;
import common.data.UserManager;
import common.data.MessageHistoryManager;
import common.model.Message;
import common.model.User;

import java.io.IOException;
import java.net.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clase abstracta que implementa la interfaz {@link Server} y proporciona la funcionalidad básica para un servidor de chat UDP.
 * Los servidores de chat que extiendan esta clase deberán implementar el método {@link #listen()} para manejar la recepción de mensajes.
 */
public abstract class AbstractUDPServer implements Server {
    private static final Logger logger = Logger.getLogger(AbstractUDPServer.class.getName());
    protected final int port;  // Puerto en el que escucha el servidor
    protected final UserManager userManager;  // Gestor de usuarios conectados
    protected final MessageHistoryManager messageHistoryManager;  // Gestor del historial de mensajes
    protected DatagramSocket serverSocket;  // Socket del servidor UDP
    protected AtomicBoolean isRunning;  // Estado de ejecución del servidor

    /**
     * Constructor de la clase AbstractUDPServer.
     * Inicializa el puerto, el gestor de usuarios y el historial de mensajes.
     *
     * @param port El puerto en el que el servidor escuchará las conexiones.
     */
    public AbstractUDPServer(int port) {
        this.port = port;
        this.userManager = new UserManager();
        this.messageHistoryManager = new MessageHistoryManager();
        this.isRunning = new AtomicBoolean();
    }

    /**
     * Inicia el servidor UDP.
     * Crea un socket UDP y comienza a escuchar en el puerto especificado.
     */
    @Override
    public void start() {
        try {
            serverSocket = UDPUtil.createSocket(port);
            logger.log(Level.INFO, "Server started on port: " + port);
            isRunning.set(true);
            listen();  // Método abstracto para escuchar los mensajes (debe ser implementado por las subclases)
        } catch (SocketException e) {
            logger.log(Level.SEVERE, "Failed to start server: " + e.getMessage());
        }
    }

    /**
     * Detiene el servidor UDP.
     * Cierra el socket del servidor y cambia el estado de ejecución a falso.
     */
    @Override
    public void stop() {
        if (serverSocket != null && !serverSocket.isClosed()) {
            isRunning.set(false);
            serverSocket.close();
            logger.log(Level.CONFIG, "Server stopped.");
        }
    }

    /**
     * Método abstracto para escuchar y procesar mensajes entrantes.
     * Este método debe ser implementado por las clases que extienden {@link AbstractUDPServer}.
     */
    public abstract void listen();

    /**
     * Verifica si un mensaje es un comando, basándose en el símbolo de comando definido en {@link CommandFactory#COMMAND_SYMBOL}.
     *
     * @param message El mensaje a verificar.
     * @return {@code true} si el mensaje es un comando, {@code false} en caso contrario.
     */
    protected boolean isCommand(String message) {
        return message.startsWith(String.valueOf(CommandFactory.COMMAND_SYMBOL));
    }

    /**
     * Difunde un mensaje a todos los usuarios conectados, excluyendo al remitente.
     *
     * @param message El mensaje a difundir.
     * @param sender La dirección del remitente.
     * @throws IOException Si ocurre un error al enviar el mensaje.
     */
    @Override
    public void broadcastMessage(Message message, InetSocketAddress sender) throws IOException {
        if (message != null) {
            logger.log(Level.INFO, "Broadcasting message: " + message);
            messageHistoryManager.addMessage(message);  // Agregar el mensaje al historial

            // Enviar el mensaje a todos los clientes, excepto al remitente
            for (User client : userManager.getClients().values()) {
                if (!client.getAddress().equals(sender)) {
                    sendMessage(message.toString(), client.getAddress());
                }
            }
        }
    }

    /**
     * Envía un mensaje a un cliente específico.
     *
     * @param message El mensaje que se enviará.
     * @param clientAddress La dirección del cliente al que se enviará el mensaje.
     * @throws IOException Si ocurre un error al enviar el mensaje.
     */
    @Override
    public void sendMessage(String message, InetSocketAddress clientAddress) throws IOException {
        UDPSocketCommunication.sendMessage(serverSocket, message, clientAddress);
        logger.log(Level.INFO, "Sending message to " + clientAddress + ": " + message);
    }

    /**
     * Envía el historial de mensajes a un cliente específico.
     *
     * @param clientAddress La dirección del cliente al que se enviará el historial.
     * @throws IOException Si ocurre un error al enviar el historial.
     */
    public void sendHistory(InetSocketAddress clientAddress) throws IOException {
        List<Message> history = messageHistoryManager.getHistory();
        logger.log(Level.INFO, "Sending history to new user from " + clientAddress);
        for (Message message : history) {
            sendMessage(message.toString(), clientAddress);
        }
    }

    /**
     * Obtiene el gestor de usuarios del servidor.
     *
     * @return El gestor de usuarios.
     */
    public UserManager getUserManager() {
        return userManager;
    }
}
