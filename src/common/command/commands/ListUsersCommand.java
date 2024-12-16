package common.command.commands;

import common.command.Command;
import common.command.CommandContext;
import common.model.User;
import server.AbstractUDPServer;

public class ListUsersCommand implements Command {
    private static final String COMMAND = "users";
    private final CommandContext context;

    public ListUsersCommand(CommandContext context) {
        this.context = context;
    }

    @Override
    public void execute() {
        AbstractUDPServer server = context.getServer();
        StringBuilder userList = new StringBuilder("Connected users:\n");

        for (User user : server.getConnectedUsers().getUserList()) {
            userList.append(user.getName()).append("\n");
        }

        server.sendMessageToUser(context.getUser(), userList.toString());
    }

    public static String getCommand() {
        return COMMAND;
    }
}