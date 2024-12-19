package test2.common.model;

public class Message {
    private final User user;
    private final String text;

    public Message(User user, String text) {
        this.user = user;
        this.text = text;
    }

    public User getUser() {
        return user;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return "[" + user.getUsername() + "] " + text;
    }
}
