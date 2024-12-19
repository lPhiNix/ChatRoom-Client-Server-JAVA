package common.socket;

import common.logger.ChatLogger;

import java.io.IOException;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UDPSocketCommunication {
    private static final Logger logger = ChatLogger.getLogger(UDPSocketCommunication.class.getName());
    public static void sendMessage(DatagramSocket socket, String message, String address, int port) {
        try {
            byte[] buffer = message.getBytes();
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, InetAddress.getByName(address), port);
            socket.send(packet);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to send message: " + e.getMessage());
        }
    }

    public static String receiveMessage(DatagramSocket socket) throws IOException {
        byte[] buffer = new byte[1024];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        socket.receive(packet);
        return new String(packet.getData(), 0, packet.getLength());
    }
}
