package server;

import common.command.Command;
import common.command.CommandContext;
import common.command.CommandFactory;
import common.model.Message;
import common.model.User;
import common.socket.UDPSocketCommunication;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implementación de un servidor UDP para la gestión de un chat.
 * Esta clase extiende {@link AbstractUDPServer} y maneja el procesamiento de mensajes y comandos recibidos
 * a través de sockets UDP.
 */
public class UDPServer extends AbstractUDPServer {
    private static final Logger logger = Logger.getLogger(UDPServer.class.getName());
    private final CommandFactory commandFactory;  // Fábrica de comandos para crear comandos específicos

    /**
     * Constructor del servidor UDP.
     *
     * @param port El puerto en el que el servidor escuchará las conexiones.
     */
    public UDPServer(int port) {
        super(port);
        this.commandFactory = new CommandFactory();
    }

    /**
     * Escucha los paquetes UDP entrantes y procesa los mensajes o comandos.
     * El servidor se ejecuta en un bucle mientras esté en estado de ejecución.
     */
    @Override
    public void listen() {
        try {
            while (isRunning.get()) {
                DatagramPacket packet = UDPSocketCommunication.receiveMessage(serverSocket);  // Recibe el paquete
                logger.log(Level.INFO, "Packet received");

                String message = extractMessage(packet);  // Extrae el mensaje del paquete
                InetSocketAddress clientAddress = extractClientAddress(packet);  // Extrae la dirección del cliente

                logger.log(Level.INFO, "Received message from {0}: {1}", new Object[]{clientAddress, message});
                handleMessage(message, clientAddress);  // Maneja el mensaje recibido
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error receiving message: {0}", e.getMessage());
        } finally {
            logger.log(Level.INFO, "Server stopped listening.");
        }
    }

    /**
     * Extrae el mensaje de un paquete UDP.
     *
     * @param packet El paquete recibido.
     * @return El mensaje extraído del paquete.
     */
    private String extractMessage(DatagramPacket packet) {
        String message = new String(packet.getData(), 0, packet.getLength()).trim();
        logger.log(Level.INFO, "Extracted message: {0}", message);
        return message;
    }

    /**
     * Extrae la dirección del cliente de un paquete UDP.
     *
     * @param packet El paquete recibido.
     * @return La dirección del cliente desde la que se recibió el paquete.
     */
    private InetSocketAddress extractClientAddress(DatagramPacket packet) {
        InetSocketAddress address = new InetSocketAddress(packet.getAddress(), packet.getPort());
        logger.log(Level.INFO, "Extracted client address: {0}", address);
        return address;
    }

    /**
     * Maneja el mensaje recibido, determinando si es un comando o un mensaje regular.
     *
     * @param message El mensaje recibido.
     * @param clientAddress La dirección del cliente que envió el mensaje.
     * @throws IOException Si ocurre un error al manejar el mensaje.
     */
    private void handleMessage(String message, InetSocketAddress clientAddress) throws IOException {
        if (isCommand(message)) {
            logger.log(Level.INFO, "Message identified as command: {0}", message);
            processCommand(message, clientAddress);  // Procesa el comando si es identificado como tal
        } else {
            logger.log(Level.INFO, "Message identified as regular message: {0}", message);
            handleRegularMessage(message, clientAddress);  // Maneja el mensaje regular
        }
    }

    /**
     * Maneja un mensaje regular, enviándolo a todos los usuarios conectados.
     *
     * @param message El mensaje recibido.
     * @param clientAddress La dirección del cliente que envió el mensaje.
     * @throws IOException Si ocurre un error al enviar el mensaje.
     */
    private void handleRegularMessage(String message, InetSocketAddress clientAddress) throws IOException {
        logger.log(Level.INFO, "Handling regular message from {0}: {1}", new Object[]{clientAddress, message});
        User user = getUserManager().getUserByAddress(clientAddress);  // Obtiene el usuario que envió el mensaje
        if (user == null) {
            logger.log(Level.WARNING, "No user found for address: {0}", clientAddress);
            return;
        }

        Message newMessage = new Message(user, message);  // Crea un objeto Message con el usuario y el mensaje
        logger.log(Level.INFO, "Broadcasting message from user {0}: {1}", new Object[]{user.getUsername(), message});
        broadcastMessage(newMessage, clientAddress);  // Difunde el mensaje a todos los clientes conectados
    }

    /**
     * Procesa un comando recibido en el mensaje.
     *
     * @param message El mensaje recibido que contiene el comando.
     * @param clientAddress La dirección del cliente que envió el comando.
     * @throws IOException Si ocurre un error al procesar el comando.
     */
    private void processCommand(String message, InetSocketAddress clientAddress) throws IOException {
        String[] parts = parseCommand(message);  // Analiza el comando y su contenido
        String commandName = parts[0];
        String content = parts[1];

        logger.log(Level.CONFIG, "Processing command: {0} with content: {1}", new Object[]{commandName, content});
        try {
            CommandContext context = createCommandContext(content, clientAddress);  // Crea el contexto del comando
            Command command = commandFactory.createCommand(commandName, context);  // Crea el comando a partir del nombre

            if (command != null) {
                logger.log(Level.INFO, "Executing command: {0}", commandName);
                executeCommand(command, commandName);  // Ejecuta el comando
            } else {
                logger.log(Level.WARNING, "Unknown command received: {0}", commandName);
                handleUnknownCommand(commandName, clientAddress);  // Maneja comandos desconocidos
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error processing command: {0}", e.getMessage());
            handleCommandError(e, clientAddress);  // Maneja errores al procesar el comando
        }
    }

    /**
     * Analiza el comando recibido y separa el nombre del comando y su contenido.
     *
     * @param message El mensaje que contiene el comando.
     * @return Un array con el nombre del comando y su contenido.
     */
    private String[] parseCommand(String message) {
        String[] parts = message.split(" ", 2);
        String commandName = parts[0].substring(1);  // Elimina el símbolo de comando "/"
        String content = parts.length > 1 ? parts[1] : "";  // Obtiene el contenido del comando
        return new String[]{commandName, content};
    }

    /**
     * Crea el contexto necesario para ejecutar un comando.
     *
     * @param content El contenido del comando.
     * @param clientAddress La dirección del cliente que envió el comando.
     * @return El contexto que contiene información sobre el comando.
     */
    private CommandContext createCommandContext(String content, InetSocketAddress clientAddress) {
        User user = new User(content, clientAddress);  // Crea un nuevo usuario basado en el contenido del comando
        return new CommandContext(this, user, clientAddress);  // Crea un nuevo contexto con el usuario y la dirección
    }

    /**
     * Ejecuta el comando recibido.
     *
     * @param command El comando a ejecutar.
     * @param commandName El nombre del comando.
     * @throws IOException Si ocurre un error al ejecutar el comando.
     */
    private void executeCommand(Command command, String commandName) throws IOException {
        command.execute(serverSocket);  // Ejecuta el comando en el socket
        logger.log(Level.CONFIG, "Command executed successfully: {0}", commandName);
    }

    /**
     * Maneja un comando desconocido.
     *
     * @param commandName El nombre del comando desconocido.
     * @param clientAddress La dirección del cliente que envió el comando.
     * @throws IOException Si ocurre un error al manejar el comando desconocido.
     */
    private void handleUnknownCommand(String commandName, InetSocketAddress clientAddress) throws IOException {
        logger.log(Level.WARNING, "Unknown command received: {0}", commandName);
        sendMessage("Unknown command: " + commandName, clientAddress);  // Informa al cliente que el comando no es reconocido
    }

    /**
     * Maneja errores al procesar un comando.
     *
     * @param e La excepción que se ha lanzado durante el procesamiento del comando.
     * @param clientAddress La dirección del cliente que envió el comando.
     * @throws IOException Si ocurre un error al manejar el error del comando.
     */
    private void handleCommandError(Exception e, InetSocketAddress clientAddress) throws IOException {
        logger.log(Level.SEVERE, "Error executing command: {0}", e.getMessage());
        sendMessage("Error executing command: " + e.getMessage(), clientAddress);  // Informa al cliente sobre el error del comando
    }
}
