package common.model;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class User {
    private String name;
    private String password;
    private InetAddress address;
    public User(String name, String password, String ip) throws UnknownHostException {
        this.name = name;
        this.password = password;
        this.address = InetAddress.getByName(ip);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", address=" + address +
                '}';
    }
}
