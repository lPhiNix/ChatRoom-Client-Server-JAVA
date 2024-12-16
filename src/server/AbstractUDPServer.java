package server;

import common.model.User;
import common.util.data.Users;
import common.util.logger.ChatLogger;
import common.util.socket.UDPSocketCommunication;
import common.util.socket.UDPUtil;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class AbstractUDPServer implements Server {
    private static final Logger logger = ChatLogger.getLogger(AbstractUDPServer.class.getName());
    protected DatagramSocket socket;
    protected int port;
    protected AtomicBoolean isRunning;
    private final Users connectedUsers;
    private final Map<String, Integer> userPorts;

    public AbstractUDPServer(int port, int maxUsers) {
        this.port = port;
        this.isRunning = new AtomicBoolean(false);
        this.connectedUsers = new Users(maxUsers);
        this.userPorts = new HashMap<>();
    }

    @Override
    public void start() {
        try {
            socket = UDPUtil.createSocket(port);
            isRunning.set(true);
            logger.info("Server started on port " + port);

            while (isRunning.get()) {
                byte[] buffer = new byte[1024];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

                try {
                    socket.receive(packet);
                    String message = new String(packet.getData(), 0, packet.getLength());
                    String clientAddress = packet.getAddress().getHostAddress();
                    int clientPort = packet.getPort();

                    handleClientMessage(message, clientAddress, clientPort);
                } catch (IOException e) {
                    if (isRunning.get()) {
                        logger.log(Level.SEVERE, "Error receiving message: " + e.getMessage(), e);
                    }
                }
            }
        } catch (SocketException e) {
            logger.log(Level.SEVERE, "Failed to start server: " + e.getMessage(), e);
        }
    }

    @Override
    public void stop() {
        isRunning.set(false);
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
        logger.info("Server stopped.");
    }

    public Users getConnectedUsers() {
        return connectedUsers;
    }

    public void sendMessage(String message, String clientAddress, int clientPort) {
        UDPSocketCommunication.sendMessage(socket, message, clientAddress, clientPort);
    }

    public void sendMessageToUser(User user, String message) {
        String clientAddress = user.getAddress();
        int clientPort = userPorts.get(user.getName());
        UDPSocketCommunication.sendMessage(socket, message, clientAddress, clientPort);
    }

    public void broadcastMessage(String message, String senderAddress, int senderPort) {
        for (Map.Entry<String, Integer> entry : userPorts.entrySet()) {
            String targetAddress = entry.getKey();
            int targetPort = entry.getValue();
            if (!targetAddress.equals(senderAddress) || targetPort != senderPort) {
                sendMessageToUser(getUserByName(targetAddress), message);
            }
        }
    }

    public void disconnectUser(User user) {
        connectedUsers.deleteUser(user);
        userPorts.remove(user.getName());
        broadcastMessage(user.getName() + " has left the chat.", null, -1);
    }

    public User getUserByName(String name) {
        return connectedUsers.getUserList().stream()
                .filter(user -> user.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    public User getUserByAddress(String address) {
        for (User user : connectedUsers.getUserList()) {
            if (user.getAddress().equals(address)) {
                return user;
            }
        }
        return null;
    }

    public void sendErrorMessage(String errorMessage, String clientAddress, int clientPort) {
        sendMessage("[Error] " + errorMessage, clientAddress, clientPort);
        logger.warning("Error sent to client (" + clientAddress + ":" + clientPort + "): " + errorMessage);
    }

    // Abstract method for handling client messages in concrete server
    public abstract void handleClientMessage(String message, String clientAddress, int clientPort);
}