package client;

import common.command.commands.LoginCommand;
import common.logger.ChatLogger;
import common.command.CommandFactory;
import common.command.commands.ExitCommand;
import common.model.User;
import common.socket.UDPSocketCommunication;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import static client.ANSIColor.*;

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

            sendMessage(loginCommand);

            Thread listenerThread = startListenerThread();
            handleUserInput(listenerThread);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Client error: " + e.getMessage(), e);
        }
    }

    @Override
    protected void listen() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                DatagramPacket packet = UDPSocketCommunication.receiveMessage(clientSocket);
                handleReceiveMessage(packet);
            } catch (IOException e) {
                handleReceiveError(e);
            }
        }
    }

    private Thread startListenerThread() {
        Thread listenerThread = new Thread(() -> listen());
        listenerThread.start();
        return listenerThread;
    }

    private void handleExitCommand(Thread listenerThread) {
        System.out.println(CYAN.get() + BOLD.get() + "Ending session..." + RESET.get());
        try {
            sendMessage(CommandFactory.COMMAND_SYMBOL + ExitCommand.getCommandName());
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error sending exit command: " + e.getMessage(), e);
        }
        listenerThread.interrupt();
    }

    protected void handleUserInput(Thread listenerThread) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print(CYAN.get() + "> " + RESET.get());  // Prompt color
            String input = scanner.nextLine();
            if (isExitCommand(input)) {
                handleExitCommand(listenerThread);
                break;
            }
            processUserInput(input);
        }

        disconnect();
    }

    private boolean isExitCommand(String input) {
        return input.equalsIgnoreCase(CommandFactory.COMMAND_SYMBOL + ExitCommand.getCommandName());
    }

    protected void processUserInput(String input) {
        try {
            sendMessage(input);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error sending command: " + e.getMessage(), e);
        }
    }

    protected void handleReceiveMessage(DatagramPacket packet) {
        String message = new String(packet.getData(), 0, packet.getLength());
        if (message.startsWith("ERROR")) {
            System.out.println(RED.get() + "[ERROR] " + RESET.get() + message);
        } else {
            System.out.println(CYAN.get() + BOLD.get() + message + RESET.get());  // Server message with style
        }
    }

    protected void handleReceiveError(IOException e) {
        if (!Thread.currentThread().isInterrupted()) {
            logger.log(Level.SEVERE, "Error receiving message: " + e.getMessage(), e);
            System.out.println(RED.get() + "[ERROR] " + e.getMessage() + RESET.get());
        }
    }

    public static User promptUserDetails(int userPort) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(YELLOW.get() + BOLD.get() + "Welcome to the Chat Client!" + RESET.get());
        System.out.print(GREEN.get() + "Enter your username: " + RESET.get());
        String username = scanner.nextLine();
        System.out.println(CYAN.get() + "Username entered: " + UNDERLINE.get() + username + RESET.get());
        return new User(username, new InetSocketAddress("localhost", userPort));
    }
}
