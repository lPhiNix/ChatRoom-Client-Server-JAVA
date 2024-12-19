package test2.common.data;
import test2.common.model.User;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

public class UserManager {
    private final Map<String, User> clients;

    public UserManager() {
        this.clients = new HashMap<>();
    }

    public boolean addUser(String username, InetSocketAddress address) {
        if (clients.containsKey(username)) {
            return false;
        }
        clients.put(username, new User(username, address));
        return true;
    }

    public void removeUser(String username) {
        clients.remove(username);
    }

    public User getUserByAddress(InetSocketAddress address) {
        for (Map.Entry<String, User> entry : clients.entrySet()) {
            if (entry.getValue().getAddress().equals(address)) {
                return entry.getValue();
            }
        }
        return null;
    }

    public User getUserByName(String username) {
        return clients.get(username);
    }

    public String getUserList() {
        StringBuilder userList = new StringBuilder("Usuarios conectados: ");
        for (String user : clients.keySet()) {
            userList.append(user).append(", ");
        }
        return userList.toString();
    }

    public boolean isUserConnected(String username) {
        return clients.containsKey(username);
    }

    public Map<String, User> getClients() {
        return clients;
    }
}