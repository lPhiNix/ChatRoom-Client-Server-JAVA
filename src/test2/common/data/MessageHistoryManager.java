package test2.common.data;

import test2.common.model.Message;

import java.util.ArrayList;
import java.util.List;

public class MessageHistoryManager {
    private static final int MESSAGE_HISTORY_LIMIT = 10;
    private final List<Message> messageHistory;

    public MessageHistoryManager() {
        this.messageHistory = new ArrayList<>();
    }

    public void addMessage(Message message) {
        messageHistory.add(message);
        if (messageHistory.size() > MESSAGE_HISTORY_LIMIT) {
            messageHistory.remove(0);
        }
    }

    public List<Message> getHistory() {
        return new ArrayList<>(messageHistory);
    }
}