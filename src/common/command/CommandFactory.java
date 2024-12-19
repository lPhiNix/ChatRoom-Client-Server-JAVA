package common.command;

import common.command.commands.LoginCommand;
import common.command.commands.ExitCommand;
import common.command.commands.ListUsersCommand;
import common.command.commands.PrivateMessageCommand;

import java.util.HashMap;
import java.util.Map;

public class CommandFactory {
    public static final char COMMAND_SYMBOL = '/';
    private final Map<String, Class<? extends Command>> commands;
    public CommandFactory() {
        commands = new HashMap<>();

        registerCommand(LoginCommand.getCommandName(), LoginCommand.class);
        registerCommand(ListUsersCommand.getCommandName(), ListUsersCommand.class);
        registerCommand(PrivateMessageCommand.getCommandName(), PrivateMessageCommand.class);
        registerCommand(ExitCommand.getCommandName(), ExitCommand.class);
    }

    public void registerCommand(String commandName, Class<? extends Command> commandClass) {
        commands.put(commandName, commandClass);
    }

    public Command createCommand(String commandName, CommandContext context) throws Exception {
        Class<? extends Command> commandClass = commands.get(commandName);
        if (commandClass != null) {
            return commandClass.getConstructor(CommandContext.class).newInstance(context);
        }
        return null;
    }
}
