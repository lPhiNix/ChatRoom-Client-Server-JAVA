package common.data;

import common.model.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MessageHistoryManager {
    private static final Logger logger = Logger.getLogger(MessageHistoryManager.class.getName());
    private static final int MESSAGE_HISTORY_LIMIT = 10;
    private final List<Message> messageHistory;

    public MessageHistoryManager() {
        this.messageHistory = new ArrayList<>();
    }

    public void addMessage(Message message) {
        messageHistory.add(message);

        if (messageHistory.size() > MESSAGE_HISTORY_LIMIT) {
            Message removedMessage = messageHistory.remove(0);
            logger.log(Level.INFO, "Message removed from history due to limit: {0}", removedMessage);
        }
    }

    public List<Message> getHistory() {
        logger.log(Level.CONFIG, "Fetching message history. Current size: {0}", messageHistory.size());
        return new ArrayList<>(messageHistory);
    }
}