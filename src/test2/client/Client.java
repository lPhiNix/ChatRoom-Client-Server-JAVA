package test2.client;

import java.io.IOException;

public interface Client {
    void connect();
    void disconnect();
    void sendMessage(String message) throws IOException;
}
