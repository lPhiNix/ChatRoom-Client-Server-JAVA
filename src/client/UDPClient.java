package client;

import common.model.Message;
import common.model.User;
import java.net.UnknownHostException;
import java.util.Scanner;

public class UDPClient extends AbstractUDPClient {
    public UDPClient(String serverIp, int serverPort, User user) throws UnknownHostException {
        super(serverIp, serverPort, user);
    }

    @Override
    protected void handleMessage(String message) {
        if (message.startsWith("[System]")) {
            System.out.println("System message: " + message);
        } else if (message.startsWith("[Private]")) {
            System.out.println("Private message: " + message);
        } else {
            System.out.println("Chat message: " + message);
        }
    }

    @Override
    public void receiveMessages() {
        super.receiveMessages();
    }

    public void startChat() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("Escribe un mensaje: ");
            String message = scanner.nextLine();

            if (message.equalsIgnoreCase("/exit")) {
                sendMessage(new Message("/exit", user));
                disconnect();
                break;
            } else {
                sendMessage(new Message(message, user));
            }
        }
    }
}
