package test2.client;

import test2.common.command.CommandFactory;
import test2.common.command.commands.ExitCommand;
import test2.common.logger.ChatLogger;

import java.io.IOException;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class AbstractUDPClient implements Client {
    private static final Logger logger = ChatLogger.getLogger(AbstractUDPClient.class.getName());
    protected final InetSocketAddress serverAddress;
    protected final int userPort;
    protected DatagramSocket clientSocket;

    public AbstractUDPClient(InetSocketAddress serverAddress, int userPort) {
        this.userPort = userPort;
        this.serverAddress = serverAddress;
    }

    @Override
    public void connect() {
        try {
            clientSocket = new DatagramSocket();
            logger.log(Level.INFO, "Connected to server at " + serverAddress + ":" + serverAddress.getPort());
        } catch (SocketException e) {
            logger.log(Level.SEVERE, "Failed to connect: " + e.getMessage());
        }
    }

    @Override
    public void disconnect() {
        if (clientSocket != null && !clientSocket.isClosed()) {
            clientSocket.close();
            logger.log(Level.INFO, "Disconnected from server.");
        }
    }

    protected abstract void start();
    protected abstract void listen();

    protected boolean isExitCommand(String input) {
        return input.equalsIgnoreCase(CommandFactory.COMMAND_SYMBOL + ExitCommand.getCommandName());
    }

    protected void processUserInput(String input) {
        logger.info("Sending command: " + input);
        try {
            sendMessage(input);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error sending command: " + e.getMessage(), e);
        }
    }

    @Override
    public void sendMessage(String message) throws IOException {
        byte[] data = message.getBytes();
        DatagramPacket packet = new DatagramPacket(data, data.length, serverAddress.getAddress(), userPort);
        logger.info("Sending packet to server: " + message);
        clientSocket.send(packet);
    }
}
