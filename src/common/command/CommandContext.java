package common.command;

import common.model.User;
import server.AbstractUDPServer;

public record CommandContext(String[] args, User user, AbstractUDPServer server) {
    public String[] getArgs() {
        return args;
    }

    public User getUser() {
        return user;
    }

    public AbstractUDPServer getServer() {
        return server;
    }
}
