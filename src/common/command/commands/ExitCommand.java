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

public class ExitCommand implements Command {
    private static final Logger logger = ChatLogger.getLogger(ExitCommand.class.getName());
    private static final String COMMAND_NAME = "exit";
    private final CommandContext context;

    public ExitCommand(CommandContext context) {
        this.context = context;
    }

    @Override
    public void execute(DatagramSocket socket) throws IOException {
        InetSocketAddress clientAddress = context.getClientAddress();
        handleDisconnect(clientAddress);
    }

    public void handleDisconnect(InetSocketAddress clientAddress) throws IOException {
        UDPServer server = context.getServer();
        User user = server.getUserManager().getUserByAddress(clientAddress);
        if (user != null) {
            logger.log(Level.INFO, "User disconnected: " + user.getUsername());
            server.getUserManager().removeUser(user.getUsername());
            server.broadcastMessage(new Message(user, "has disconnected."), null);
        }
    }

    public static String getCommandName() {
        return COMMAND_NAME;
    }
}