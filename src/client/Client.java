package client;

import java.io.IOException;

/**
 * Interfaz que define las operaciones básicas para un cliente en un sistema de chat.
 * Los métodos incluyen la conexión al servidor, desconexión y envío de mensajes.
 */
public interface Client {

    /**
     * Establece una conexión con el servidor.
     */
    void connect();

    /**
     * Desconecta al cliente del servidor.
     */
    void disconnect();

    /**
     * Envía un mensaje al servidor.
     *
     * @param message El mensaje que se enviará.
     * @throws IOException Si ocurre un error al intentar enviar el mensaje.
     */
    void sendMessage(String message) throws IOException;
}
