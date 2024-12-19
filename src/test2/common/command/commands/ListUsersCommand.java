package test2.common.command.commands;

import test2.common.logger.ChatLogger;
import test2.common.command.Command;
import test2.common.command.CommandContext;
import test2.server.UDPServer;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ListUsersCommand implements Command {
    private static final Logger logger = ChatLogger.getLogger(ListUsersCommand.class.getName());
    private static final String COMMAND_NAME = "users";
    private final CommandContext context;

    public ListUsersCommand(CommandContext context) {
        this.context = context;
    }

    @Override
    public void execute(DatagramSocket socket) throws IOException {
        InetSocketAddress clientAddress = context.getClientAddress();
        handleUserList(clientAddress);
    }

    public void handleUserList(InetSocketAddress clientAddress) throws IOException {
        UDPServer server = context.getServer();

        String userList = server.getUserManager().getUserList();
        logger.log(Level.INFO, "Sending user list to " + clientAddress);
        server.sendMessage(userList, clientAddress);
    }

    public static String getCommandName() {
        return COMMAND_NAME;
    }
}
