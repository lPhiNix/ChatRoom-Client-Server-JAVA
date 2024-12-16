package common.model;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Objects;

public class User {
    private String name;
    private InetAddress address;
    public User(String name, String ip) throws UnknownHostException {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("User name cannot be null or empty!");
        }
        this.name = name;
        this.address = InetAddress.getByName(ip);
    }

    public User(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("User name cannot be null or empty!");
        }

        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address.getHostName();
    }

    public InetAddress getInetAddress() {
        return address;
    }

    public void setAddress(String ip) throws UnknownHostException {
        this.address = InetAddress.getByName(ip);
    }

    public void setInetAddress(InetAddress address) {
        this.address = address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(name, user.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", address=" + address +
                '}';
    }
}
