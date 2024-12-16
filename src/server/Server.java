package server;

public interface Server {
    void start();
    void stop();
    void handleClientMessage(String message, String clientAddress, int clientPort);
}