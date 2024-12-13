package test;

import common.model.User;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

public class ConnectionHandler {
    private static final Map<String, User> usuariosConectados = new HashMap<>();

    public static String handleConnection(String receivedMessage, InetAddress clientAddress, int clientPort) throws UnknownHostException {
        String[] userDetails = receivedMessage.split(",");
        if (userDetails.length == 3) {
            String nombreUsuario = userDetails[0];
            String contraseñaUsuario = userDetails[1];
            String ipUsuario = userDetails[2];

            if (usuariosConectados.containsKey(nombreUsuario)) {
                // Nombre de usuario ya en uso
                return "El nombre de usuario '" + nombreUsuario + "' ya está en uso. Intenta con otro.";
            } else {
                // Crear el nuevo usuario y añadirlo a la lista de usuarios
                User newUser = new User(nombreUsuario, contraseñaUsuario, ipUsuario);
                usuariosConectados.put(nombreUsuario, newUser);
                System.out.println("Nuevo usuario conectado: " + newUser);

                // Notificar a todos los clientes sobre la nueva conexión
                return "Bienvenido al chat, " + nombreUsuario + "!";
            }
        } else {
            return "Formato incorrecto. Usa: nombre,contraseña,ip";
        }
    }
}