package common.command.commands;

import common.command.Command;
import common.command.CommandContext;
import common.model.User;
import server.AbstractUDPServer;

public class DisconnectCommand implements Command {
    private static final String COMMAND = "exit";
    private final CommandContext context;

    public DisconnectCommand(CommandContext context) {
        this.context = context;
    }

    @Override
    public void execute() {
        AbstractUDPServer server = context.getServer();
        User user = context.getUser();

        server.disconnectUser(user);
        server.broadcastMessage(user.getName() + " has left the chat.", null, -1);
    }

    public static String getCommand() {
        return COMMAND;
    }
}