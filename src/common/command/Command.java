package common.command;

public interface Command {
    void execute();

    static String getCommand() {
        return null;
    }
}
