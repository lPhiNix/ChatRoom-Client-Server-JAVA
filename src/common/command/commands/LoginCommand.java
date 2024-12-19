package common.command.commands;

import common.logger.ChatLogger;

import java.util.logging.Logger;

public class LoginCommand {
    private static final Logger logger = ChatLogger.getLogger(ExitCommand.class.getName());
    private static final String COMMAND_NAME = "login";

    public static String getCommandName() {
        return COMMAND_NAME;
    }
}
