package server;

import common.command.Command;
import common.command.CommandContext;
import common.command.CommandFactory;
import common.model.Message;
import common.model.User;

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
            byte[] buffer = new byte[1024];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

            while (isRunning.get()) {
                serverSocket.receive(packet);
                String message = extractMessage(packet);
                InetSocketAddress clientAddress = extractClientAddress(packet);

                logger.log(Level.INFO, "Received message from " + clientAddress + ": " + message);
                handleMessage(message, clientAddress);
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error receiving message: " + e.getMessage(), e);
        }
    }

    private String extractMessage(DatagramPacket packet) {
        return new String(packet.getData(), 0, packet.getLength()).trim();
    }

    private InetSocketAddress extractClientAddress(DatagramPacket packet) {
        return new InetSocketAddress(packet.getAddress(), packet.getPort());
    }

    private void handleMessage(String message, InetSocketAddress clientAddress) throws IOException {
        if (isCommand(message)) {
            processCommand(message, clientAddress);
        } else {
            handleRegularMessage(message, clientAddress);
        }
    }

    private void handleRegularMessage(String message, InetSocketAddress clientAddress) throws IOException {
        User user = getUserManager().getUserByAddress(clientAddress);
        Message newMessage = new Message(user, message);
        broadcastMessage(newMessage, clientAddress);
    }

    private void processCommand(String message, InetSocketAddress clientAddress) throws IOException {
        String[] parts = parseCommand(message);
        String commandName = parts[0];
        String content = parts[1];

        try {
            CommandContext context = createCommandContext(content, clientAddress);
            Command command = commandFactory.createCommand(commandName, context);

            if (command != null) {
                executeCommand(command, commandName);
            } else {
                handleUnknownCommand(commandName, clientAddress);
            }
        } catch (Exception e) {
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
        logger.log(Level.INFO, "Executing command: " + commandName);
        command.execute(serverSocket);
    }

    private void handleUnknownCommand(String commandName, InetSocketAddress clientAddress) throws IOException {
        logger.log(Level.WARNING, "Unknown command received: " + commandName);
        sendMessage("Unknown command: " + commandName, clientAddress);
    }

    private void handleCommandError(Exception e, InetSocketAddress clientAddress) throws IOException {
        logger.log(Level.SEVERE, "Error executing command: " + e.getMessage(), e);
        sendMessage("Error executing command: " + e.getMessage(), clientAddress);
    }
}
