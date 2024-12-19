package client;

import common.logger.ChatLogger;
import common.command.CommandFactory;
import common.command.commands.ExitCommand;
import common.command.commands.LoginCommand;
import common.model.User;
import common.socket.UDPSocketCommunication;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import static client.ANSIColor.*;

/**
 * UDPClient es la implementación de un cliente UDP que se conecta a un servidor y permite la interacción del usuario.
 * Este cliente envía y recibe mensajes del servidor, gestiona comandos y permite que el usuario envíe mensajes.
 */
public class UDPClient extends AbstractUDPClient {
    // Logger para el registro de eventos y errores
    private static final Logger logger = ChatLogger.getLogger(UDPClient.class.getName());

    // Usuario asociado al cliente
    private User user;

    /**
     * Constructor que inicializa el cliente UDP con la dirección del servidor y el usuario.
     *
     * @param serverAddress Dirección del servidor al que se conecta el cliente.
     * @param user El usuario que se va a asociar al cliente.
     */
    public UDPClient(InetSocketAddress serverAddress, User user) {
        super(serverAddress, user.getAddress().getPort());
        this.user = user;
    }

    /**
     * Inicia el cliente UDP. Envía un comando de login al servidor e inicia un hilo para escuchar los mensajes
     * del servidor y manejar las entradas del usuario.
     */
    @Override
    public void start() {
        try {
            // Construye el comando de login para el servidor
            String loginCommand = CommandFactory.COMMAND_SYMBOL + LoginCommand.getCommandName() + " " + user.getUsername();

            // Envia el comando de login al servidor
            sendMessage(loginCommand);

            // Inicia el hilo para escuchar mensajes del servidor
            Thread listenerThread = startListenerThread();

            // Maneja las entradas del usuario mientras el hilo escucha mensajes
            handleUserInput(listenerThread);
        } catch (IOException e) {
            // En caso de error, se registra el fallo
            logger.log(Level.SEVERE, "Client error: " + e.getMessage(), e);
        }
    }

    /**
     * Método para escuchar los mensajes entrantes del servidor en un bucle infinito.
     * Recibe los mensajes a través del socket UDP y los maneja en el hilo principal.
     */
    @Override
    protected void listen() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                // Recibe un paquete de datos del servidor
                DatagramPacket packet = UDPSocketCommunication.receiveMessage(clientSocket);
                // Maneja el mensaje recibido
                handleReceiveMessage(packet);
            } catch (IOException e) {
                // En caso de error, se maneja el error de recepción
                handleReceiveError(e);
            }
        }
    }

    /**
     * Inicia un hilo para ejecutar el método de escucha de mensajes.
     *
     * @return El hilo de escucha iniciado.
     */
    private Thread startListenerThread() {
        // Crea un hilo que ejecutará el método listen() para recibir mensajes.
        Thread listenerThread = new Thread(() -> listen());
        listenerThread.start();
        return listenerThread;
    }

    /**
     * Maneja el comando de salida, deteniendo el hilo de escucha y enviando el comando de salida al servidor.
     *
     * @param listenerThread El hilo que escucha los mensajes del servidor.
     */
    private void handleExitCommand(Thread listenerThread) {
        System.out.println(CYAN.get() + BOLD.get() + "Ending session..." + RESET.get());
        try {
            // Envía el comando de salida al servidor
            sendMessage(CommandFactory.COMMAND_SYMBOL + ExitCommand.getCommandName());
        } catch (IOException e) {
            // Si hay un error al enviar el comando, se registra el fallo
            logger.log(Level.SEVERE, "Error sending exit command: " + e.getMessage(), e);
        }
        // Interrumpe el hilo de escucha
        listenerThread.interrupt();
    }

    /**
     * Maneja las entradas del usuario desde la consola.
     * Detecta el comando de salida y detiene la conexión si es necesario.
     *
     * @param listenerThread El hilo que escucha los mensajes del servidor.
     */
    protected void handleUserInput(Thread listenerThread) {
        Scanner scanner = new Scanner(System.in);

        // Bucle para leer la entrada del usuario
        while (true) {
            System.out.print(CYAN.get() + "> " + RESET.get());  // Estilo del prompt
            String input = scanner.nextLine();

            // Si el usuario ingresa el comando de salida, se maneja y se detiene el bucle
            if (isExitCommand(input)) {
                handleExitCommand(listenerThread);
                break;
            }
            // Si no es el comando de salida, procesa la entrada como un mensaje regular
            processUserInput(input);
        }

        // Desconecta al cliente al finalizar
        disconnect();
    }

    /**
     * Comprueba si el comando ingresado por el usuario es el comando de salida.
     *
     * @param input El comando ingresado por el usuario.
     * @return true si es el comando de salida, false en caso contrario.
     */
    private boolean isExitCommand(String input) {
        return input.equalsIgnoreCase(CommandFactory.COMMAND_SYMBOL + ExitCommand.getCommandName());
    }

    /**
     * Procesa la entrada del usuario enviándola al servidor como un mensaje.
     *
     * @param input El mensaje o comando a enviar al servidor.
     */
    protected void processUserInput(String input) {
        try {
            // Envía el mensaje o comando al servidor
            sendMessage(input);
        } catch (IOException e) {
            // Si hay un error al enviar el mensaje, se registra el fallo
            logger.log(Level.SEVERE, "Error sending command: " + e.getMessage(), e);
        }
    }

    /**
     * Maneja un mensaje recibido del servidor y lo imprime en la consola.
     * Si el mensaje contiene un error, lo imprime con formato de error.
     *
     * @param packet El paquete de datos recibido del servidor.
     */
    protected void handleReceiveMessage(DatagramPacket packet) {
        String message = new String(packet.getData(), 0, packet.getLength());

        // Si el mensaje comienza con "ERROR", lo imprime en rojo
        if (message.startsWith("ERROR")) {
            System.out.println(RED.get() + "[ERROR] " + RESET.get() + message);
        } else {
            // Si el mensaje es normal, lo imprime en cian con negrita
            System.out.println(CYAN.get() + BOLD.get() + message + RESET.get());
        }
    }

    /**
     * Maneja los errores ocurridos durante la recepción de un mensaje del servidor.
     *
     * @param e La excepción de tipo IOException que ocurrió durante la recepción.
     */
    protected void handleReceiveError(IOException e) {
        if (!Thread.currentThread().isInterrupted()) {
            // Si el hilo no ha sido interrumpido, se registra el error
            logger.log(Level.SEVERE, "Error receiving message: " + e.getMessage(), e);
            System.out.println(RED.get() + "[ERROR] " + e.getMessage() + RESET.get());
        }
    }

    /**
     * Solicita los detalles del usuario (nombre de usuario) e inicializa el objeto User.
     *
     * @param userPort El puerto del cliente.
     * @return Un objeto User con el nombre de usuario ingresado y la dirección del cliente.
     */
    public static User promptUserDetails(int userPort) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(YELLOW.get() + BOLD.get() + "Welcome to the Chat Client!" + RESET.get());

        // Solicita el nombre de usuario
        System.out.print(GREEN.get() + "Enter your username: " + RESET.get());
        String username = scanner.nextLine();

        // Muestra el nombre de usuario ingresado
        System.out.println(CYAN.get() + "Username entered: " + UNDERLINE.get() + username + RESET.get());

        // Crea y devuelve un objeto User con el nombre de usuario y la dirección
        return new User(username, new InetSocketAddress("localhost", userPort));
    }
}
