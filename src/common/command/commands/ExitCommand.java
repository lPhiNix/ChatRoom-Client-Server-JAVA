package common.command.commands;

import common.logger.ChatLogger;
import common.model.Message;
import common.model.User;
import common.command.Command;
import common.command.CommandContext;
import server.UDPServer;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * El comando {@code ExitCommand} maneja la desconexión de un usuario del servidor.
 * <p>
 * Cuando un usuario envía el comando "exit", este comando se encarga de eliminar al usuario
 * de la lista de usuarios conectados y de enviar un mensaje de desconexión a todos los demás usuarios.
 * </p>
 */
public class ExitCommand implements Command {

    /**
     * El logger utilizado para registrar los eventos en este comando.
     */
    private static final Logger logger = ChatLogger.getLogger(ExitCommand.class.getName());

    /**
     * El nombre del comando "exit".
     */
    private static final String COMMAND_NAME = "exit";

    /**
     * El contexto asociado con este comando, que proporciona acceso al servidor, al usuario y a la dirección del cliente.
     */
    private final CommandContext context;

    /**
     * Constructor de la clase {@code ExitCommand}. Inicializa el contexto necesario para ejecutar el comando.
     *
     * @param context El contexto del comando que proporciona acceso al servidor y al usuario.
     */
    public ExitCommand(CommandContext context) {
        this.context = context;
    }

    /**
     * Ejecuta el comando de desconexión. El método elimina al usuario de la lista de usuarios conectados
     * y transmite un mensaje de desconexión a los demás usuarios.
     *
     * @param socket El socket utilizado para enviar mensajes, aunque no se utiliza directamente en este caso.
     * @throws IOException Si ocurre un error al enviar mensajes o al realizar la desconexión.
     */
    @Override
    public void execute(DatagramSocket socket) throws IOException {
        // Se obtiene la dirección del cliente desde el contexto
        InetSocketAddress clientAddress = context.getClientAddress();

        // Maneja la desconexión del usuario
        handleDisconnect(clientAddress);
    }

    /**
     * Maneja la desconexión de un usuario. El método elimina al usuario de la lista de usuarios conectados
     * y notifica a todos los demás usuarios sobre la desconexión.
     *
     * @param clientAddress La dirección del cliente que desea desconectarse.
     * @throws IOException Si ocurre un error al enviar el mensaje de desconexión.
     */
    public void handleDisconnect(InetSocketAddress clientAddress) throws IOException {
        // Se obtiene el servidor desde el contexto
        UDPServer server = context.getServer();

        // Se obtiene el usuario correspondiente a la dirección del cliente
        User user = server.getUserManager().getUserByAddress(clientAddress);

        if (user != null) {
            // Se registra el evento de desconexión
            logger.log(Level.CONFIG, "User disconnected: " + user.getUsername());

            // Se elimina al usuario de la lista de usuarios conectados
            server.getUserManager().removeUser(user.getUsername());

            // Se notifica a todos los usuarios que el usuario se ha desconectado
            server.broadcastMessage(new Message(user, "has disconnected."), null);
        }
    }

    /**
     * Obtiene el nombre del comando.
     *
     * @return El nombre del comando, que es "exit".
     */
    public static String getCommandName() {
        return COMMAND_NAME;
    }
}
