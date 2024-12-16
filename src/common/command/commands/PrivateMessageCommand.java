package common.command.commands;

import common.command.Command;
import common.command.CommandContext;
import common.model.User;
import server.AbstractUDPServer;
import java.util.Arrays;

public class PrivateMessageCommand implements Command {
    private static final String COMMAND = "private";
    private final CommandContext context;

    public PrivateMessageCommand(CommandContext context) {
        this.context = context;
    }

    @Override
    public void execute() {
        String[] args = context.getArgs();
        if (args.length < 2) {
            context.getServer().sendMessageToUser(context.getUser(), "Usage: /private <user> <message>");
            return;
        }

        String recipientName = args[0];
        String message = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
        AbstractUDPServer server = context.getServer();
        User recipient = server.getUserByName(recipientName);

        if (recipient != null) {
            server.sendMessageToUser(recipient, "[Private from " + context.getUser().getName() + "] " + message);
        } else {
            server.sendMessageToUser(context.getUser(), "User " + recipientName + " not found.");
        }
    }

    public static String getCommand() {
        return COMMAND;
    }
}