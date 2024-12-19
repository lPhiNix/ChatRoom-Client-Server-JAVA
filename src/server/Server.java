package server;

import common.model.Message;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * La interfaz {@code Server} define las operaciones básicas de un servidor de chat basado en UDP.
 * Los servidores que implementan esta interfaz deben ser capaces de iniciar y detener el servidor,
 * enviar mensajes a clientes y difundir mensajes a todos los usuarios conectados.
 */
public interface Server {

    /**
     * Inicia el servidor. Este método debe configurar y comenzar a escuchar en el puerto UDP,
     * además de gestionar las conexiones y la recepción de mensajes.
     */
    void start();

    /**
     * Detiene el servidor. Este método debe cerrar todas las conexiones y recursos abiertos
     * y finalizar cualquier proceso relacionado con el servidor.
     */
    void stop();

    /**
     * Difunde un mensaje a todos los usuarios conectados.
     * Este mensaje es enviado a todos los clientes registrados en el servidor,
     * excepto al remitente.
     *
     * @param message El mensaje que se enviará a los usuarios.
     * @param sender La dirección del remitente. El remitente no recibirá el mensaje de difusión.
     * @throws IOException Si ocurre un error al intentar enviar el mensaje.
     */
    void broadcastMessage(Message message, InetSocketAddress sender) throws IOException;

    /**
     * Envía un mensaje a un cliente específico.
     *
     * @param message El mensaje que se enviará al cliente.
     * @param clientAddress La dirección del cliente al que se enviará el mensaje.
     * @throws IOException Si ocurre un error al intentar enviar el mensaje.
     */
    void sendMessage(String message, InetSocketAddress clientAddress) throws IOException;
}
