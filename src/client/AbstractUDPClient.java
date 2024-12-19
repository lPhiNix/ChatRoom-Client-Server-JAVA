package client;

import common.logger.ChatLogger;
import common.socket.UDPSocketCommunication;
import common.socket.UDPUtil;

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
            clientSocket = UDPUtil.createSocket();
            System.out.println("Connected to server at " + serverAddress);
        } catch (SocketException e) {
            logger.log(Level.SEVERE, "Failed to connect: " + e.getMessage());
        }
    }

    @Override
    public void disconnect() {
        if (clientSocket != null && !clientSocket.isClosed()) {
            clientSocket.close();
            System.out.println("Disconnected from server.");
        }
    }

    protected abstract void start();
    protected abstract void listen();

    @Override
    public void sendMessage(String message) throws IOException {
        UDPSocketCommunication.sendMessage(clientSocket, message, serverAddress);
    }
}
