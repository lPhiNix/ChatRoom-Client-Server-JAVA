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
 * El comando {@code LoginCommand} maneja la solicitud de inicio de sesión de un usuario en el servidor.
 * <p>
 * Cuando un usuario envía el comando "login", este comando intenta agregar al usuario a la lista de usuarios conectados
 * en el servidor. Si el nombre de usuario ya está en uso, se le notifica al usuario que intente con otro nombre.
 * Si el nombre de usuario es válido, el usuario se conecta correctamente y se envía un mensaje de bienvenida.
 * </p>
 */
public class LoginCommand implements Command {

    /**
     * El logger utilizado para registrar los eventos en este comando.
     */
    private static final Logger logger = ChatLogger.getLogger(LoginCommand.class.getName());

    /**
     * El nombre del comando "login".
     */
    private static final String COMMAND_NAME = "login";

    /**
     * El contexto asociado con este comando, que proporciona acceso al servidor y al usuario que se está conectando.
     */
    private final CommandContext context;

    /**
     * Constructor de la clase {@code LoginCommand}. Inicializa el contexto necesario para ejecutar el comando.
     *
     * @param context El contexto del comando que proporciona acceso al servidor y al usuario.
     */
    public LoginCommand(CommandContext context) {
        this.context = context;
    }

    /**
     * Ejecuta el comando de inicio de sesión, intentado agregar al usuario al servidor.
     *
     * @param socket El socket utilizado para enviar mensajes, aunque no se utiliza directamente en este caso.
     * @throws IOException Si ocurre un error al intentar agregar al usuario o enviar mensajes.
     */
    @Override
    public void execute(DatagramSocket socket) throws IOException {
        // Se obtiene el nombre de usuario y la dirección del cliente desde el contexto
        String username = context.getUser().getUsername();
        InetSocketAddress clientAddress = context.getClientAddress();

        // Maneja la solicitud de conexión del usuario
        handleConnect(username, clientAddress);
    }

    /**
     * Maneja la conexión de un nuevo usuario. Si el nombre de usuario ya existe, se envía un mensaje de error.
     * Si el nombre de usuario es válido, el usuario se conecta correctamente y se envían mensajes de bienvenida.
     *
     * @param username El nombre de usuario que el cliente ha intentado utilizar.
     * @param clientAddress La dirección del cliente que está intentando conectarse.
     * @throws IOException Si ocurre un error al enviar mensajes.
     */
    private void handleConnect(String username, InetSocketAddress clientAddress) throws IOException {
        UDPServer server = context.getServer();

        // Si el nombre de usuario ya existe, se notifica al cliente
        if (!server.getUserManager().addUser(username, clientAddress)) {
            logger.log(Level.WARNING, "User already exists: " + username);
            server.sendMessage("User already exists. Try another name.", clientAddress);
        } else {
            // Si el usuario se conecta correctamente, se genera un mensaje de bienvenida
            User newUser = server.getUserManager().getUserByName(username);
            logger.log(Level.INFO, "User connected: " + username);

            // Se notifica a los demás usuarios que el nuevo usuario se ha conectado
            server.broadcastMessage(new Message(newUser, "is connected."), null);

            // Se envía el historial de mensajes al nuevo usuario
            server.sendHistory(clientAddress);
        }
    }

    /**
     * Obtiene el nombre del comando.
     *
     * @return El nombre del comando, que es "login".
     */
    public static String getCommandName() {
        return COMMAND_NAME;
    }
}
