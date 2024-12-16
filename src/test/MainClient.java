package test;

import client.UDPClient;
import common.model.User;
import common.util.socket.UDPUtil;

import java.net.UnknownHostException;
import java.util.Scanner;

public class MainClient {
    public static void main(String[] args) throws UnknownHostException {
        String serverIp = UDPUtil.getLocalIP();
        int serverPort = 4445;
        Scanner scanner = new Scanner(System.in);

        System.out.print("Introduce tu nombre de usuario: ");
        String username = scanner.nextLine();

        try {
            User user = new User(username, serverIp);
            UDPClient client = new UDPClient(serverIp, serverPort, user);

            client.connect();
            System.out.println("Conectado al servidor en " + serverIp + ":" + serverPort);

            client.receiveMessages();

            client.startChat();
        } catch (UnknownHostException e) {
            System.err.println("Error al conectar con el servidor: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
