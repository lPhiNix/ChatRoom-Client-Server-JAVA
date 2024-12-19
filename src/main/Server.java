package main;

import server.UDPServer;

public class Server {
    public static void main(String[] args) {
        UDPServer server = new UDPServer(12345);
        server.start();
    }
}
