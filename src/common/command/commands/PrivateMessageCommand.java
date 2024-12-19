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
 * El comando {@code PrivateMessageCommand} maneja el envío de mensajes privados entre usuarios en el servidor.
 * <p>
 * Cuando un cliente envía el comando "/private", este comando extrae el nombre del usuario destino y el contenido
 * del mensaje. Luego, si el usuario destino existe, se envía el mensaje de forma privada. Si el formato del mensaje
 * es incorrecto o el usuario destino no se encuentra, se le notifica al remitente.
 * </p>
 */
public class PrivateMessageCommand implements Command {

    /**
     * El logger utilizado para registrar los eventos en este comando.
     */
    private static final Logger logger = ChatLogger.getLogger(PrivateMessageCommand.class.getName());

    /**
     * El nombre del comando "private".
     */
    private static final String COMMAND_NAME = "private";

    /**
     * El contexto asociado con este comando, que proporciona acceso al servidor y al usuario que envía el mensaje.
     */
    private final CommandContext context;

    /**
     * Constructor de la clase {@code PrivateMessageCommand}. Inicializa el contexto necesario para ejecutar el comando.
     *
     * @param context El contexto del comando que proporciona acceso al servidor y al usuario.
     */
    public PrivateMessageCommand(CommandContext context) {
        this.context = context;
    }

    /**
     * Ejecuta el comando de mensaje privado, manejando el contenido y enviando el mensaje a la dirección del usuario destino.
     *
     * @param socket El socket utilizado para enviar mensajes, aunque no se utiliza directamente en este caso.
     * @throws IOException Si ocurre un error al enviar el mensaje privado.
     */
    @Override
    public void execute(DatagramSocket socket) throws IOException {
        // Se obtiene el contenido del mensaje privado del nombre de usuario
        String content = context.getUser().getUsername();
        InetSocketAddress clientAddress = context.getClientAddress();

        // Maneja el envío del mensaje privado
        handlePrivateMessage(content, clientAddress);
    }

    /**
     * Maneja el envío del mensaje privado a un usuario específico. Si el formato es incorrecto o el usuario destino
     * no existe, se notifica al cliente.
     *
     * @param content El contenido del mensaje, que debe incluir el nombre del usuario destino y el mensaje.
     * @param clientAddress La dirección del cliente que envía el mensaje.
     * @throws IOException Si ocurre un error al enviar el mensaje.
     */
    public void handlePrivateMessage(String content, InetSocketAddress clientAddress) throws IOException {
        UDPServer server = context.getServer();

        // Se divide el contenido del mensaje en el nombre del usuario destino y el mensaje privado
        String[] parts = content.split(" ", 2);
        if (parts.length < 2) {
            // Si el formato es incorrecto, se notifica al cliente
            logger.log(Level.WARNING, "Incorrect private message format from " + clientAddress);
            server.sendMessage("Incorrect format. Use: /private <target_user> <message>", clientAddress);
            return;
        }

        // Se obtiene el nombre del usuario destino y el contenido del mensaje privado
        String targetUser = parts[0];
        String privateMessage = parts[1];

        // Se busca al usuario destino en el servidor
        User target = server.getUserManager().getUserByName(targetUser);
        if (target != null) {
            // Si el usuario destino existe, se envía el mensaje privado
            logger.log(Level.INFO, "Sending private message to " + targetUser + ": " + privateMessage);
            server.sendMessage(new Message(server.getUserManager().getUserByAddress(clientAddress), "[Private] " + privateMessage).toString(), target.getAddress());
        } else {
            // Si el usuario destino no existe, se notifica al remitente
            logger.log(Level.WARNING, "User not found for private message: " + targetUser);
            server.sendMessage("User " + targetUser + " not found.", clientAddress);
        }
    }

    /**
     * Obtiene el nombre del comando.
     *
     * @return El nombre del comando, que es "private".
     */
    public static String getCommandName() {
        return COMMAND_NAME;
    }
}
