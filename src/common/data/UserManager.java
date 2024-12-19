package common.data;
import common.model.User;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserManager {
    private static final Logger logger = Logger.getLogger(UserManager.class.getName());
    private final Map<String, User> clients;

    public UserManager() {
        this.clients = new HashMap<>();
    }

    public boolean addUser(String username, InetSocketAddress address) {
        if (clients.containsKey(username)) {
            logger.log(Level.WARNING, "Attempt to add user with existing username: {0}", username);
            return false;
        }
        clients.put(username, new User(username, address));
        return true;
    }

    public void removeUser(String username) {
        if (clients.remove(username) != null) {
            logger.log(Level.WARNING, "Attempt to remove non-existing user: {0}", username);
        }
    }

    public User getUserByAddress(InetSocketAddress address) {
        for (Map.Entry<String, User> entry : clients.entrySet()) {
            if (entry.getValue().getAddress().equals(address)) {
                return entry.getValue();
            }
        }
        logger.log(Level.WARNING, "No user found for address: {0}", address);
        return null;
    }

    public User getUserByName(String username) {
        User user = clients.get(username);
        if (user == null) {
            logger.log(Level.WARNING, "No user found with username: {0}", username);
        }
        return user;
    }

    public String getUserList() {
        StringBuilder userList = new StringBuilder("Connected Users: ");
        for (String user : clients.keySet()) {
            userList.append(user).append(", ");
        }
        logger.log(Level.INFO, "Generated user list: {0}", userList.toString());
        return userList.toString();
    }

    public boolean isUserConnected(String username) {
        return clients.containsKey(username);
    }

    public Map<String, User> getClients() {
        return clients;
    }
}
