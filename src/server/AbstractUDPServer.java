package server;

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

public abstract class AbstractUDPServer implements Server {
    private static final Logger logger = Logger.getLogger(AbstractUDPServer.class.getName());
    protected final int port;
    protected final UserManager userManager;
    protected final MessageHistoryManager messageHistoryManager;
    protected DatagramSocket serverSocket;
    protected AtomicBoolean isRunning;

    public AbstractUDPServer(int port) {
        this.port = port;
        this.userManager = new UserManager();
        this.messageHistoryManager = new MessageHistoryManager();
        this.isRunning = new AtomicBoolean();
    }

    @Override
    public void start() {
        try {
            serverSocket = UDPUtil.createSocket(port);
            logger.log(Level.INFO, "Server started on port: " + port);
            isRunning.set(true);
            listen();
        } catch (SocketException e) {
            logger.log(Level.SEVERE, "Failed to start server: " + e.getMessage());
        }
    }

    @Override
    public void stop() {
        if (serverSocket != null && !serverSocket.isClosed()) {
            isRunning.set(false);
            serverSocket.close();
            logger.log(Level.INFO, "Server stopped.");
        }
    }

    public abstract void listen();

    protected boolean isCommand(String message) {
        return message.startsWith(String.valueOf(CommandFactory.COMMAND_SYMBOL));
    }

    @Override
    public void broadcastMessage(Message message, InetSocketAddress sender) throws IOException {
        if (message != null) {
            logger.log(Level.INFO, "Broadcasting message: " + message);
            messageHistoryManager.addMessage(message);

            for (User client : userManager.getClients().values()) {
                if (!client.getAddress().equals(sender)) {
                    sendMessage(message.toString(), client.getAddress());
                }
            }
        }
    }

    @Override
    public void sendMessage(String message, InetSocketAddress clientAddress) throws IOException {
        byte[] data = message.getBytes();
        DatagramPacket packet = new DatagramPacket(data, data.length, clientAddress.getAddress(), clientAddress.getPort());
        logger.log(Level.INFO, "Sending message to " + clientAddress + ": " + message);
        serverSocket.send(packet);
    }

    public void sendHistory(InetSocketAddress clientAddress) throws IOException {
        List<Message> history = messageHistoryManager.getHistory();
        for (Message message : history) {
            sendMessage(message.toString(), clientAddress);
        }
    }

    public UserManager getUserManager() {
        return userManager;
    }
}
