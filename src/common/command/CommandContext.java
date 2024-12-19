package common.command;

import common.model.User;
import server.UDPServer;

import java.net.InetSocketAddress;

/**
 * La clase {@code CommandContext} proporciona el contexto necesario para ejecutar un comando.
 * <p>
 * Esta clase encapsula información relevante como el servidor que está ejecutando el comando,
 * el usuario que ejecuta el comando y la dirección del cliente desde donde se recibió la solicitud.
 * </p>
 */
public class CommandContext {

    /**
     * El servidor UDP que está gestionando las solicitudes de los clientes.
     */
    private final UDPServer server;

    /**
     * El usuario que está ejecutando el comando.
     */
    private final User user;

    /**
     * La dirección del cliente (IP y puerto) desde la que se ha recibido la solicitud.
     */
    private final InetSocketAddress clientAddress;

    /**
     * Constructor de la clase {@code CommandContext}.
     * Se utiliza para crear un contexto que contiene la información relevante para ejecutar un comando.
     *
     * @param server El servidor UDP que está gestionando la solicitud.
     * @param user El usuario que ejecuta el comando.
     * @param clientAddress La dirección del cliente (IP y puerto).
     */
    public CommandContext(UDPServer server, User user, InetSocketAddress clientAddress) {
        this.server = server;
        this.user = user;
        this.clientAddress = clientAddress;
    }

    /**
     * Obtiene el servidor UDP que está gestionando la solicitud.
     *
     * @return El servidor UDP.
     */
    public UDPServer getServer() {
        return server;
    }

    /**
     * Obtiene el usuario que está ejecutando el comando.
     *
     * @return El usuario.
     */
    public User getUser() {
        return user;
    }

    /**
     * Obtiene la dirección del cliente desde la que se ha recibido la solicitud.
     *
     * @return La dirección del cliente.
     */
    public InetSocketAddress getClientAddress() {
        return clientAddress;
    }
}
