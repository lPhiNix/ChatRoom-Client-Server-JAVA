package common.data;

import common.model.User;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * La clase {@code UserManager} gestiona los usuarios conectados en el sistema.
 * Permite agregar, eliminar y obtener información de los usuarios mediante su nombre de usuario o dirección IP.
 * <p>
 * Los usuarios son almacenados en un mapa donde la clave es el nombre de usuario y el valor es el objeto {@code User}.
 * Esta clase también proporciona métodos para verificar si un usuario está conectado y para generar una lista de los usuarios conectados.
 * </p>
 */
public class UserManager {
    // Logger para registrar las operaciones de manejo de usuarios
    private static final Logger logger = Logger.getLogger(UserManager.class.getName());

    // Mapa que almacena los usuarios, con el nombre de usuario como clave y el objeto User como valor
    private final Map<String, User> clients;

    /**
     * Constructor de la clase {@code UserManager}.
     * Inicializa el mapa de usuarios.
     */
    public UserManager() {
        this.clients = new HashMap<>();
    }

    /**
     * Agrega un nuevo usuario al sistema.
     *
     * @param username El nombre de usuario a agregar.
     * @param address La dirección IP del usuario.
     * @return {@code true} si el usuario fue agregado correctamente, {@code false} si el nombre de usuario ya existe.
     */
    public boolean addUser(String username, InetSocketAddress address) {
        // Verificar si el nombre de usuario ya está en uso
        if (clients.containsKey(username)) {
            logger.log(Level.WARNING, "Attempt to add user with existing username: {0}", username);
            return false; // Si ya existe, no agregar el usuario
        }
        // Agregar el usuario al mapa
        clients.put(username, new User(username, address));
        return true; // Usuario agregado correctamente
    }

    /**
     * Elimina un usuario del sistema por su nombre de usuario.
     *
     * @param username El nombre de usuario a eliminar.
     */
    public void removeUser(String username) {
        // Eliminar el usuario y verificar si fue exitoso
        if (clients.remove(username) == null) {
            logger.log(Level.WARNING, "Attempt to remove non-existing user: {0}", username);
        }
    }

    /**
     * Obtiene un usuario por su dirección IP.
     *
     * @param address La dirección IP del usuario a buscar.
     * @return El objeto {@code User} correspondiente a la dirección, o {@code null} si no se encuentra.
     */
    public User getUserByAddress(InetSocketAddress address) {
        // Buscar el usuario en el mapa por la dirección IP
        for (Map.Entry<String, User> entry : clients.entrySet()) {
            if (entry.getValue().getAddress().equals(address)) {
                return entry.getValue(); // Usuario encontrado por dirección
            }
        }
        // Si no se encuentra el usuario con esa dirección, registrar un warning
        logger.log(Level.WARNING, "No user found for address: {0}", address);
        return null; // Retornar null si no se encuentra el usuario
    }

    /**
     * Obtiene un usuario por su nombre de usuario.
     *
     * @param username El nombre de usuario a buscar.
     * @return El objeto {@code User} correspondiente al nombre de usuario, o {@code null} si no se encuentra.
     */
    public User getUserByName(String username) {
        // Obtener el usuario por nombre
        User user = clients.get(username);
        // Si el usuario no se encuentra, registrar un warning
        if (user == null) {
            logger.log(Level.WARNING, "No user found with username: {0}", username);
        }
        return user; // Retornar el usuario encontrado o null
    }

    /**
     * Genera una lista de los usuarios conectados en formato de texto.
     *
     * @return Una cadena de texto con los nombres de los usuarios conectados.
     */
    public String getUserList() {
        StringBuilder userList = new StringBuilder("Connected Users: ");
        // Iterar sobre los usuarios y agregar sus nombres a la lista
        for (String user : clients.keySet()) {
            userList.append(user).append(", ");
        }
        // Registrar la lista generada
        logger.log(Level.INFO, "Generated user list: {0}", userList.toString());
        return userList.toString(); // Retornar la lista de usuarios
    }

    /**
     * Verifica si un usuario está conectado en el sistema.
     *
     * @param username El nombre de usuario a verificar.
     * @return {@code true} si el usuario está conectado, {@code false} si no lo está.
     */
    public boolean isUserConnected(String username) {
        return clients.containsKey(username); // Verificar si el usuario está en el mapa
    }

    /**
     * Obtiene el mapa completo de usuarios conectados.
     *
     * @return Un mapa con los usuarios, donde la clave es el nombre de usuario y el valor es el objeto {@code User}.
     */
    public Map<String, User> getClients() {
        return clients; // Retornar el mapa de usuarios
    }
}
