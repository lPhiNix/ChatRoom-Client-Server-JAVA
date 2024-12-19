package common.command;

import common.model.User;
import server.UDPServer;

import java.net.InetSocketAddress;

public class CommandContext {
    private final UDPServer server;
    private final User user;
    private final InetSocketAddress clientAddress;

    public CommandContext(UDPServer server, User user, InetSocketAddress clientAddress) {
        this.server = server;
        this.user = user;
        this.clientAddress = clientAddress;
    }

    public UDPServer getServer() {
        return server;
    }

    public User getUser() {
        return user;
    }

    public InetSocketAddress getClientAddress() {
        return clientAddress;
    }
}
