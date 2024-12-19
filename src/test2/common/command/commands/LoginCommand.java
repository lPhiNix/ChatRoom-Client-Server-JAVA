package test2.common.command.commands;


import test2.common.logger.ChatLogger;
import test2.common.model.Message;
import test2.common.model.User;
import test2.common.command.Command;
import test2.common.command.CommandContext;
import test2.server.UDPServer;

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