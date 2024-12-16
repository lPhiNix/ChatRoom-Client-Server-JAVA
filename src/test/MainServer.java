package test;

import client.UDPClient;
import common.model.Message;
import common.model.User;
import server.UDPServer;

import java.net.UnknownHostException;
import java.util.Scanner;

public class MainServer {
    public static void main(String[] args) {
        int port = 4445;
        int maxUsers = 10;

        UDPServer server = new UDPServer(port, maxUsers);
        server.start();

        try {
            System.out.println("Servidor en funcionamiento. Presiona Enter para detener.");
            System.in.read();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            server.stop();
        }
    }
}
