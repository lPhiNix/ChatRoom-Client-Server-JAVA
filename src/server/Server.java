package server;

import common.model.Message;

import java.io.IOException;
import java.net.InetSocketAddress;

public interface Server {
    void start();
    void stop();
    void broadcastMessage(Message message, InetSocketAddress sender) throws IOException;
    void sendMessage(String message, InetSocketAddress clientAddress) throws IOException;
}
