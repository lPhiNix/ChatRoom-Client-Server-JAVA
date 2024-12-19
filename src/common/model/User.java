package common.model;

import java.net.InetSocketAddress;
import java.util.Objects;

public class User {
    private String username;
    private InetSocketAddress address;

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

    public void setUsername(String username) {
        this.username = username;
    }

    public void setAddress(InetSocketAddress address) {
        this.address = address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }

    @Override
    public String toString() {
        return username;
    }
}
