package common.command.commands;


import common.logger.ChatLogger;
import common.model.Message;
import common.model.User;
import common.command.Command;
import common.command.CommandContext;
import server.UDPServer;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoginCommand implements Command {
    private static final Logger logger = ChatLogger.getLogger(LoginCommand.class.getName());
    private static final String COMMAND_NAME = "login";
    private final CommandContext context;

    public LoginCommand(CommandContext context) {
        this.context = context;
    }

    @Override
    public void execute(DatagramSocket socket) throws IOException {
        String username = context.getUser().getUsername();
        InetSocketAddress clientAddress = context.getClientAddress();
        handleConnect(username, clientAddress);
    }

    private void handleConnect(String username, InetSocketAddress clientAddress) throws IOException {
        UDPServer server = context.getServer();

        if (!server.getUserManager().addUser(username, clientAddress)) {
            logger.log(Level.WARNING, "User already exists: " + username);
            server.sendMessage("User already exists. Try another name.", clientAddress);
        } else {
            User newUser = server.getUserManager().getUserByName(username);
            logger.log(Level.INFO, "User connected: " + username);
            server.broadcastMessage(new Message(newUser, "is connected."), null);
            server.sendHistory(clientAddress);
        }
    }

    public static String getCommandName() {
        return COMMAND_NAME;
    }
}