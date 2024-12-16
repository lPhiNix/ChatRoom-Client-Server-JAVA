package server;

import common.command.*;
import common.command.commands.DisconnectCommand;
import common.command.commands.ListUsersCommand;
import common.command.commands.PrivateMessageCommand;
import common.model.User;
import common.util.logger.ChatLogger;

import java.util.logging.Level;
import java.util.logging.Logger;

public class UDPServer extends AbstractUDPServer {
    private static final Logger logger = ChatLogger.getLogger(UDPServer.class.getName());
    private static final String COMMAND_SYMBOL = "/";
    private final CommandFactory commandFactory;

    public UDPServer(int port, int maxUsers) {
        super(port, maxUsers);
        this.commandFactory = new CommandFactory();
        registerCommands();
    }

    private void registerCommands() {
        commandFactory.registerCommand(COMMAND_SYMBOL + ListUsersCommand.getCommand(), ListUsersCommand.class);
        commandFactory.registerCommand(COMMAND_SYMBOL + DisconnectCommand.getCommand(), DisconnectCommand.class);
        commandFactory.registerCommand(COMMAND_SYMBOL + PrivateMessageCommand.getCommand(), PrivateMessageCommand.class);
    }

    @Override
    public void handleClientMessage(String message, String clientAddress, int clientPort) {
        if (message.startsWith(COMMAND_SYMBOL)) {
            handleCommand(message, clientAddress, clientPort);
        } else {
            broadcastMessage(message, clientAddress, clientPort);
        }
    }

    private void handleCommand(String command, String clientAddress, int clientPort) {
        String[] parts = command.split(" ", 2);
        String cmd = parts[0];
        String[] args = parts.length > 1 ? parts[1].split(" ") : new String[0];

        User user = getUserByAddress(clientAddress);
        CommandContext context = new CommandContext(args, user, this);

        try {
            Command commandObject = commandFactory.createCommand(cmd, context);
            if (commandObject != null) {
                commandObject.execute();
                logger.info("Executed command: " + cmd + " for user: " + user.getName());
            } else {
                sendErrorMessage("Unknown command: " + cmd, clientAddress, clientPort);
            }
        } catch (Exception e) {
            sendErrorMessage("Error processing command: " + e.getMessage(), clientAddress, clientPort);
            logger.log(Level.SEVERE, "Error processing command: " + e.getMessage(), e);
        }
    }

    @Override
    public void sendErrorMessage(String errorMessage, String clientAddress, int clientPort) {
        super.sendErrorMessage(errorMessage, clientAddress, clientPort);
        logger.warning("Error message sent to client (" + clientAddress + ":" + clientPort + "): " + errorMessage);
    }

    @Override
    public void broadcastMessage(String message, String senderAddress, int senderPort) {
        super.broadcastMessage(message, senderAddress, senderPort);
        logger.info("Broadcast message: " + message);
    }
}