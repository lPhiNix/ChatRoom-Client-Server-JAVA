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

public class UDPServer extends AbstractUDPServer {
    private static final Logger logger = Logger.getLogger(UDPServer.class.getName());
    private final CommandFactory commandFactory;

    public UDPServer(int port) {
        super(port);
        this.commandFactory = new CommandFactory();
    }

    @Override
    public void listen() {
        try {
            while (isRunning.get()) {
                DatagramPacket packet = UDPSocketCommunication.receiveMessage(serverSocket);
                logger.log(Level.INFO, "Packet received");

                String message = extractMessage(packet);
                InetSocketAddress clientAddress = extractClientAddress(packet);

                logger.log(Level.INFO, "Received message from {0}: {1}", new Object[]{clientAddress, message});
                handleMessage(message, clientAddress);
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error receiving message: {0}", e.getMessage());
        } finally {
            logger.log(Level.INFO, "Server stopped listening.");
        }
    }

    private String extractMessage(DatagramPacket packet) {
        String message = new String(packet.getData(), 0, packet.getLength()).trim();
        logger.log(Level.INFO, "Extracted message: {0}", message);
        return message;
    }

    private InetSocketAddress extractClientAddress(DatagramPacket packet) {
        InetSocketAddress address = new InetSocketAddress(packet.getAddress(), packet.getPort());
        logger.log(Level.INFO, "Extracted client address: {0}", address);
        return address;
    }

    private void handleMessage(String message, InetSocketAddress clientAddress) throws IOException {
        if (isCommand(message)) {
            logger.log(Level.INFO, "Message identified as command: {0}", message);
            processCommand(message, clientAddress);
        } else {
            logger.log(Level.INFO, "Message identified as regular message: {0}", message);
            handleRegularMessage(message, clientAddress);
        }
    }

    private void handleRegularMessage(String message, InetSocketAddress clientAddress) throws IOException {
        logger.log(Level.INFO, "Handling regular message from {0}: {1}", new Object[]{clientAddress, message});
        User user = getUserManager().getUserByAddress(clientAddress);
        if (user == null) {
            logger.log(Level.WARNING, "No user found for address: {0}", clientAddress);
            return;
        }

        Message newMessage = new Message(user, message);
        logger.log(Level.INFO, "Broadcasting message from user {0}: {1}", new Object[]{user.getUsername(), message});
        broadcastMessage(newMessage, clientAddress);
    }

    private void processCommand(String message, InetSocketAddress clientAddress) throws IOException {
        String[] parts = parseCommand(message);
        String commandName = parts[0];
        String content = parts[1];

        logger.log(Level.CONFIG, "Processing command: {0} with content: {1}", new Object[]{commandName, content});
        try {
            CommandContext context = createCommandContext(content, clientAddress);
            Command command = commandFactory.createCommand(commandName, context);

            if (command != null) {
                logger.log(Level.INFO, "Executing command: {0}", commandName);
                executeCommand(command, commandName);
            } else {
                logger.log(Level.WARNING, "Unknown command received: {0}", commandName);
                handleUnknownCommand(commandName, clientAddress);
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error processing command: {0}", e.getMessage());
            handleCommandError(e, clientAddress);
        }
    }

    private String[] parseCommand(String message) {
        String[] parts = message.split(" ", 2);
        String commandName = parts[0].substring(1);
        String content = parts.length > 1 ? parts[1] : "";
        return new String[]{commandName, content};
    }

    private CommandContext createCommandContext(String content, InetSocketAddress clientAddress) {
        User user = new User(content, clientAddress);
        return new CommandContext(this, user, clientAddress);
    }

    private void executeCommand(Command command, String commandName) throws IOException {
        command.execute(serverSocket);
        logger.log(Level.CONFIG, "Command executed successfully: {0}", commandName);
    }

    private void handleUnknownCommand(String commandName, InetSocketAddress clientAddress) throws IOException {
        logger.log(Level.WARNING, "Unknown command received: {0}", commandName);
        sendMessage("Unknown command: " + commandName, clientAddress);
    }

    private void handleCommandError(Exception e, InetSocketAddress clientAddress) throws IOException {
        logger.log(Level.SEVERE, "Error executing command: {0}", e.getMessage());
        sendMessage("Error executing command: " + e.getMessage(), clientAddress);
    }
}
