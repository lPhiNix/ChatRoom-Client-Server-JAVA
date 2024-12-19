package common.data;

import common.model.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * La clase {@code MessageHistoryManager} gestiona el historial de mensajes del sistema.
 * Se encarga de almacenar los mensajes recibidos y mantener un límite en el número de mensajes almacenados.
 * Cuando se alcanza el límite, el mensaje más antiguo es eliminado.
 * <p>
 * El historial de mensajes es almacenado en una lista y se puede recuperar utilizando el método {@code getHistory}.
 * </p>
 */
public class MessageHistoryManager {
    // Logger para registrar operaciones relacionadas con el historial de mensajes
    private static final Logger logger = Logger.getLogger(MessageHistoryManager.class.getName());

    // Límite máximo de mensajes a mantener en el historial
    private static final int MESSAGE_HISTORY_LIMIT = 10;

    // Lista que almacena los mensajes, manteniendo solo los últimos MESSAGE_HISTORY_LIMIT mensajes
    private final List<Message> messageHistory;

    /**
     * Constructor de la clase {@code MessageHistoryManager}.
     * Inicializa la lista que almacenará el historial de mensajes.
     */
    public MessageHistoryManager() {
        this.messageHistory = new ArrayList<>();
    }

    /**
     * Agrega un mensaje al historial. Si el número de mensajes supera el límite,
     * elimina el mensaje más antiguo.
     *
     * @param message El mensaje a agregar al historial.
     */
    public void addMessage(Message message) {
        // Añadir el nuevo mensaje al historial
        messageHistory.add(message);

        // Si el historial excede el límite, eliminar el mensaje más antiguo
        if (messageHistory.size() > MESSAGE_HISTORY_LIMIT) {
            // Eliminar el primer mensaje de la lista (el más antiguo)
            Message removedMessage = messageHistory.remove(0);
            // Registrar que se ha eliminado un mensaje por exceder el límite
            logger.log(Level.INFO, "Message removed from history due to limit: {0}", removedMessage);
        }
    }

    /**
     * Recupera una copia del historial de mensajes.
     *
     * @return Una lista de los mensajes almacenados en el historial.
     */
    public List<Message> getHistory() {
        // Registrar que se está obteniendo el historial de mensajes
        logger.log(Level.CONFIG, "Fetching message history. Current size: {0}", messageHistory.size());
        // Retornar una copia del historial para evitar modificaciones externas
        return new ArrayList<>(messageHistory);
    }
}
