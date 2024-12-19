package common.socket;

import common.logger.ChatLogger;

import java.io.IOException;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UDPSocketCommunication {
    private static final Logger logger = ChatLogger.getLogger(UDPSocketCommunication.class.getName());
    public static void sendMessage(DatagramSocket socket, String message, InetSocketAddress address) {
        try {
            byte[] buffer = message.getBytes();
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, InetAddress.getByName(address.getHostName()), address.getPort());
            socket.send(packet);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to send message: " + e.getMessage());
        }
    }

    public static DatagramPacket receiveMessage(DatagramSocket socket) throws IOException {
        byte[] buffer = new byte[1024];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        socket.receive(packet);
        return packet;
    }
}
