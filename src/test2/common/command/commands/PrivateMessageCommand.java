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

public class PrivateMessageCommand implements Command {
    private static final Logger logger = ChatLogger.getLogger(PrivateMessageCommand.class.getName());
    private static final String COMMAND_NAME = "private";
    private final CommandContext context;

    public PrivateMessageCommand(CommandContext context) {
        this.context = context;
    }

    @Override
    public void execute(DatagramSocket socket) throws IOException {
        String content = context.getUser().getUsername();
        InetSocketAddress clientAddress = context.getClientAddress();
        handlePrivateMessage(content, clientAddress);
    }

    public void handlePrivateMessage(String content, InetSocketAddress clientAddress) throws IOException {
        UDPServer server = context.getServer();

        String[] parts = content.split(" ", 2);
        if (parts.length < 2) {
            logger.log(Level.WARNING, "Incorrect private message format from " + clientAddress);
            server.sendMessage("Incorrect format. Use: /private <target_user> <message>", clientAddress);
            return;
        }
        String targetUser = parts[0];
        String privateMessage = parts[1];

        User target = server.getUserManager().getUserByName(targetUser);
        if (target != null) {
            logger.log(Level.INFO, "Sending private message to " + targetUser + ": " + privateMessage);
            server.sendMessage(new Message(server.getUserManager().getUserByAddress(clientAddress), "[Private] " + privateMessage).toString(), target.getAddress());
        } else {
            logger.log(Level.WARNING, "User not found for private message: " + targetUser);
            server.sendMessage("User " + targetUser + " not found.", clientAddress);
        }
    }

    public static String getCommandName() {
        return COMMAND_NAME;
    }
}
