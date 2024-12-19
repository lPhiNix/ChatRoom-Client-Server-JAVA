package test2.client;

import test2.common.logger.ChatLogger;
import test2.common.command.CommandFactory;
import test2.common.command.commands.ExitCommand;
import test2.common.command.commands.LoginCommand;
import test2.common.model.User;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UDPClient extends AbstractUDPClient {
    private static final Logger logger = ChatLogger.getLogger(UDPClient.class.getName());
    private User user;
    public UDPClient(InetSocketAddress serverAddress, User user) {
        super(serverAddress, user.getAddress().getPort());
        this.user = user;
    }

    @Override
    public void start() {
        try {
            String loginCommand = CommandFactory.COMMAND_SYMBOL + LoginCommand.getCommandName() + " " + user.getUsername();

            logger.info("Sending login command: " + loginCommand);
            sendMessage(loginCommand);

            Thread listenerThread = startListenerThread();
            handleUserInput(listenerThread);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Client error: " + e.getMessage(), e);
        }
    }

    @Override
    protected void listen() {
        byte[] buffer = new byte[1024];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

        while (!Thread.currentThread().isInterrupted()) {
            try {
                clientSocket.receive(packet);
                handleReceiveMessage(packet);
            } catch (IOException e) {
                handleReceiveError(e);
            }
        }
    }

    private Thread startListenerThread() {
        Thread listenerThread = new Thread(() -> listen());
        listenerThread.start();
        logger.info("Listener thread started.");
        return listenerThread;
    }

    private void handleExitCommand(Thread listenerThread) {
        logger.info("Exit command received. Ending session.");
        try {
            sendMessage(CommandFactory.COMMAND_SYMBOL + ExitCommand.getCommandName());
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error sending exit command: " + e.getMessage(), e);
        }
        listenerThread.interrupt();
        logger.info("Listener thread interrupted.");
    }

    protected void handleUserInput(Thread listenerThread) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            String input = scanner.nextLine();
            if (isExitCommand(input)) {
                handleExitCommand(listenerThread);
                break;
            }
            processUserInput(input);
        }

        disconnect();
    }

    protected void handleReceiveMessage(DatagramPacket packet) {
        String message = new String(packet.getData(), 0, packet.getLength());
        logger.info("Message received from server: " + message);
        System.out.println(message);
    }

    protected void handleReceiveError(IOException e) {
        if (!Thread.currentThread().isInterrupted()) {
            logger.log(Level.SEVERE, "Error receiving message: " + e.getMessage(), e);
        }
    }

    public static User promptUserDetails() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your username: ");
        String username = scanner.nextLine();
        logger.info("Username entered: " + username);
        return new User(username, new InetSocketAddress("localhost", 12345));
    }
}
