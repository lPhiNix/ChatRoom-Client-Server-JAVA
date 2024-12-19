package common.command;

import common.command.commands.LoginCommand;
import common.command.commands.ExitCommand;
import common.command.commands.ListUsersCommand;
import common.command.commands.PrivateMessageCommand;

import java.util.HashMap;
import java.util.Map;

/**
 * La clase {@code CommandFactory} es responsable de crear comandos basados en su nombre.
 * Se encarga de registrar las clases de comandos disponibles y de crear instancias de ellas
 * cuando se solicita un comando específico.
 * <p>
 * Esta clase utiliza un mapa ({@code HashMap}) para asociar los nombres de los comandos con
 * sus respectivas clases concretas.
 * </p>
 */
public class CommandFactory {

    /**
     * El símbolo que precede a los comandos. Este carácter (/) es utilizado para identificar
     * que una cadena es un comando.
     */
    public static final char COMMAND_SYMBOL = '/';

    // Mapa que asocia nombres de comando con sus clases correspondientes
    private final Map<String, Class<? extends Command>> commands;

    /**
     * Constructor de la clase {@code CommandFactory}.
     * Registra los comandos básicos (Login, ListUsers, PrivateMessage, Exit) en el mapa {@code commands}.
     */
    public CommandFactory() {
        commands = new HashMap<>();

        // Registro de comandos disponibles en el sistema
        registerCommand(LoginCommand.getCommandName(), LoginCommand.class);
        registerCommand(ListUsersCommand.getCommandName(), ListUsersCommand.class);
        registerCommand(PrivateMessageCommand.getCommandName(), PrivateMessageCommand.class);
        registerCommand(ExitCommand.getCommandName(), ExitCommand.class);
    }

    /**
     * Registra un comando en el mapa {@code commands} asociando un nombre con una clase de comando.
     *
     * @param commandName El nombre del comando (por ejemplo, "/login").
     * @param commandClass La clase que implementa el comando correspondiente.
     */
    public void registerCommand(String commandName, Class<? extends Command> commandClass) {
        commands.put(commandName, commandClass);
    }

    /**
     * Crea una nueva instancia de un comando basado en el nombre del comando proporcionado.
     *
     * @param commandName El nombre del comando que se desea crear.
     * @param context El contexto necesario para la ejecución del comando.
     * @return Una instancia del comando correspondiente o {@code null} si el comando no existe.
     * @throws Exception Si ocurre un error al instanciar el comando.
     */
    public Command createCommand(String commandName, CommandContext context) throws Exception {
        // Busca la clase asociada al nombre del comando
        Class<? extends Command> commandClass = commands.get(commandName);
        if (commandClass != null) {
            // Si se encuentra la clase, crea una nueva instancia usando el constructor que acepta un CommandContext
            return commandClass.getConstructor(CommandContext.class).newInstance(context);
        }
        return null; // Retorna null si no se encuentra el comando
    }
}
