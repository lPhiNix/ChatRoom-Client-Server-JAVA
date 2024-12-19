package common.command.commands;

import common.logger.ChatLogger;

import java.util.logging.Logger;

public class ExitCommand {
    private static final Logger logger = ChatLogger.getLogger(ExitCommand.class.getName());
    private static final String COMMAND_NAME = "exit";

    public static String getCommandName() {
        return COMMAND_NAME;
    }
}