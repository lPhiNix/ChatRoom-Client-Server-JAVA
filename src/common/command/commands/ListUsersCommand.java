package common.command.commands;

import common.logger.ChatLogger;
import common.command.Command;
import common.command.CommandContext;
import server.UDPServer;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * El comando {@code ListUsersCommand} maneja la solicitud de listar los usuarios conectados al servidor.
 * <p>
 * Cuando un usuario envía el comando "users", este comando obtiene la lista de usuarios actualmente
 * conectados y la envía al cliente que ha realizado la solicitud.
 * </p>
 */
public class ListUsersCommand implements Command {

    /**
     * El logger utilizado para registrar los eventos en este comando.
     */
    private static final Logger logger = ChatLogger.getLogger(ListUsersCommand.class.getName());

    /**
     * El nombre del comando "users".
     */
    private static final String COMMAND_NAME = "users";

    /**
     * El contexto asociado con este comando, que proporciona acceso al servidor y a la dirección del cliente.
     */
    private final CommandContext context;

    /**
     * Constructor de la clase {@code ListUsersCommand}. Inicializa el contexto necesario para ejecutar el comando.
     *
     * @param context El contexto del comando que proporciona acceso al servidor y a la dirección del cliente.
     */
    public ListUsersCommand(CommandContext context) {
        this.context = context;
    }

    /**
     * Ejecuta el comando para obtener y enviar la lista de usuarios conectados al cliente.
     *
     * @param socket El socket utilizado para enviar mensajes, aunque no se utiliza directamente en este caso.
     * @throws IOException Si ocurre un error al enviar la lista de usuarios.
     */
    @Override
    public void execute(DatagramSocket socket) throws IOException {
        // Se obtiene la dirección del cliente desde el contexto
        InetSocketAddress clientAddress = context.getClientAddress();

        // Maneja la solicitud de la lista de usuarios
        handleUserList(clientAddress);
    }

    /**
     * Maneja la solicitud de lista de usuarios. Obtiene la lista de usuarios conectados
     * y la envía al cliente que realizó la solicitud.
     *
     * @param clientAddress La dirección del cliente que solicitó la lista de usuarios.
     * @throws IOException Si ocurre un error al enviar la lista de usuarios.
     */
    public void handleUserList(InetSocketAddress clientAddress) throws IOException {
        // Se obtiene el servidor desde el contexto
        UDPServer server = context.getServer();

        // Se obtiene la lista de usuarios conectados
        String userList = server.getUserManager().getUserList();

        // Se registra el evento de envío de la lista de usuarios
        logger.log(Level.INFO, "Sending user list to " + clientAddress);

        // Se envía la lista de usuarios al cliente
        server.sendMessage(userList, clientAddress);
    }

    /**
     * Obtiene el nombre del comando.
     *
     * @return El nombre del comando, que es "users".
     */
    public static String getCommandName() {
        return COMMAND_NAME;
    }
}
