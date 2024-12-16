package common.util.data;

import common.model.User;
import common.util.logger.ChatLogger;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Users {
    private static final Logger logger = ChatLogger.getLogger(Users.class.getName());
    private final int MAX_USERS;
    private final Set<User> userList;
    public Users(int maxUsers) {
        this.MAX_USERS = maxUsers;
        userList = new HashSet<>(maxUsers);
    }

    public boolean addUser(User user) {
        if (user == null) {
            logger.log(Level.WARNING, "Attempted to add a null user!");
            return false;
        }

        if (isFull()) {
            logger.log(Level.WARNING, "Chat is full!");
            return false;
        }

        if (userList.add(user)) {
            logger.log(Level.INFO, "User connected successfully!");
            return true;
        } else {
            logger.log(Level.WARNING, "User name already exist!");
            return false;
        }
    }

    public boolean deleteUser(User user) {
        if (user == null) {
            logger.log(Level.WARNING, "Attempted to remove a null user!");
            return false;
        }

        return userList.remove(user);
    }

    public boolean isFull() {
        return userList.size() == MAX_USERS;
    }

    public boolean isEmpty() {
        return userList.isEmpty();
    }

    public Set<String> getUserNames() {
        Set<String> userNames = new HashSet<>();
        for (User user : userList) {
            userNames.add(user.getName());
        }
        return userNames;
    }

    public Set<User> getUserList() {
        return userList;
    }

    public int getAmountUsers() {
        return userList.size();
    }

    private void printUsers() {
        int count = 0;
        for (User user : userList) {
            System.out.println(user);
            count++;
        }

        System.out.println(count);
    }
}
