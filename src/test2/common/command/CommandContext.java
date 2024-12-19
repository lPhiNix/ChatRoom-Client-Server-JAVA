package test2.common.command;

import test2.server.AbstractUDPServer;
import test2.common.model.User;
import test2.server.UDPServer;

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
