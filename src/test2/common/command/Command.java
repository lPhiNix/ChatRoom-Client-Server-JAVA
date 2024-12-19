package test2.common.command;

import java.io.IOException;
import java.net.DatagramSocket;

public interface Command {
    void execute(DatagramSocket socket) throws IOException;
    static String getCommand() {
        return null;
    }
}
