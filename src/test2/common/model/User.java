package test2.common.model;

import java.net.InetSocketAddress;

public class User {
    private final String username;
    private final InetSocketAddress address;

    public User(String username, InetSocketAddress address) {
        this.username = username;
        this.address = address;
    }

    public String getUsername() {
        return username;
    }

    public InetSocketAddress getAddress() {
        return address;
    }

    @Override
    public String toString() {
        return username;
    }
}
