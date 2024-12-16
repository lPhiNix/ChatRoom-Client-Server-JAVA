package client;

import common.model.Message;

public interface Client {
    void connect();
    void disconnect();
    void sendMessage(Message message);
    void receiveMessages();
}
