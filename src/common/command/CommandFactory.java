package common.command;

import java.util.HashMap;
import java.util.Map;

public class CommandFactory {
    private final Map<String, Class<? extends Command>> commands;

    public CommandFactory() {
        commands = new HashMap<>();
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
