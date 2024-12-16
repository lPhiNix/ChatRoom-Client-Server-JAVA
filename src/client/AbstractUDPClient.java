package client;

import common.model.Message;
import common.model.User;
import common.util.logger.ChatLogger;
import common.util.socket.UDPSocketCommunication;
import common.util.socket.UDPUtil;

import java.io.IOException;
import java.net.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class AbstractUDPClient implements Client {
    private static final Logger logger = ChatLogger.getLogger(AbstractUDPClient.class.getName());
    protected DatagramSocket socket;
    protected InetAddress serverAddress;
    protected int serverPort;
    protected User user;
    protected AtomicBoolean isConnected;

    public AbstractUDPClient(String serverIp, int serverPort, User user) throws UnknownHostException {
        this.serverAddress = InetAddress.getByName(serverIp);
        this.serverPort = serverPort;
        this.user = user;
        this.isConnected = new AtomicBoolean(false);
    }

    @Override
    public void connect() {
        try {
            socket = UDPUtil.createSocket();
            isConnected.set(true);
            logger.info("Connected to server at " + serverAddress + ":" + serverPort);
            receiveMessages();  // Start listening for incoming messages
        } catch (SocketException e) {
            logger.log(Level.SEVERE, "Failed to connect: " + e.getMessage(), e);
        }
    }

    @Override
    public void disconnect() {
        isConnected.set(false);
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
        logger.info("Disconnected from server.");
    }

    @Override
    public void sendMessage(Message message) {
        if (!isConnected.get()) {
            logger.warning("Not connected to the server!");
            return;
        }

        String formattedMessage = message.toString();
        UDPSocketCommunication.sendMessage(socket, formattedMessage, serverAddress.getHostAddress(), serverPort);
        logger.info("Sent message: " + formattedMessage);
    }

    public void receiveMessages() {
        new Thread(() -> {
            while (isConnected.get()) {
                try {
                    String message = UDPSocketCommunication.receiveMessage(socket);
                    handleMessage(message);
                } catch (IOException e) {
                    if (isConnected.get()) {
                        logger.log(Level.SEVERE, "Error receiving message: " + e.getMessage(), e);
                    }
                }
            }
        }).start();
    }

    // Abstract method for handling messages, to be implemented by the concrete class
    protected abstract void handleMessage(String message);
}