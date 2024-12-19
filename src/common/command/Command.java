package common.command;

import java.io.IOException;
import java.net.DatagramSocket;

/**
 * La interfaz {@code Command} define el contrato para los comandos ejecutables en el sistema de chat.
 * Todos los comandos deben implementar el método {@code execute}, el cual define las acciones que se deben realizar
 * cuando el comando es invocado.
 * <p>
 * Los comandos implementados permitirán distintas funcionalidades dentro de la aplicación, como enviar mensajes,
 * listar usuarios, etc.
 * </p>
 */
public interface Command {

    /**
     * Ejecuta el comando utilizando un socket de Datagram, realizando las operaciones definidas por cada comando.
     *
     * @param socket El socket de tipo {@code DatagramSocket} que se utilizará para enviar o recibir datos.
     * @throws IOException Si ocurre un error al interactuar con el socket.
     */
    void execute(DatagramSocket socket) throws IOException;

    /**
     * Método estático opcional que puede ser utilizado para obtener el nombre o identificador del comando.
     * Este método puede ser implementado por las clases concretas de comando si es necesario.
     *
     * @return El nombre o identificador del comando.
     */
    static String getCommand() {
        return null;
    }
}
