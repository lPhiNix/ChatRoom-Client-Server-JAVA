package common.model;

import java.net.InetSocketAddress;
import java.util.Objects;

/**
 * La clase {@code User} representa a un usuario en el sistema de chat.
 * Un usuario está identificado por un nombre de usuario único y una dirección de red
 * que corresponde a su conexión en la red (puerto y dirección IP).
 *
 * La clase proporciona métodos para acceder y modificar los atributos del usuario,
 * así como para comparar usuarios y representarlos como cadenas.
 */
public class User {

    /** Nombre de usuario del usuario. */
    private String username;

    /** Dirección de la red del usuario. Representa el puerto y la dirección IP. */
    private InetSocketAddress address;

    /**
     * Constructor de la clase {@code User}.
     *
     * @param username El nombre de usuario del usuario.
     * @param address La dirección de red del usuario.
     */
    public User(String username, InetSocketAddress address) {
        this.username = username;
        this.address = address;
    }

    /**
     * Obtiene el nombre de usuario.
     *
     * @return El nombre de usuario del usuario.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Obtiene la dirección de red del usuario.
     *
     * @return La dirección de red del usuario (puerto y dirección IP).
     */
    public InetSocketAddress getAddress() {
        return address;
    }

    /**
     * Establece el nombre de usuario.
     *
     * @param username El nuevo nombre de usuario.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Establece la dirección de red del usuario.
     *
     * @param address La nueva dirección de red del usuario.
     */
    public void setAddress(InetSocketAddress address) {
        this.address = address;
    }

    /**
     * Compara dos objetos {@code User}.
     * Los usuarios son considerados iguales si tienen el mismo nombre de usuario.
     *
     * @param o El objeto con el que comparar.
     * @return {@code true} si ambos usuarios tienen el mismo nombre de usuario, {@code false} en caso contrario.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;  // Si son el mismo objeto, son iguales
        if (o == null || getClass() != o.getClass()) return false;  // Si el objeto es nulo o no es de la misma clase, no son iguales
        User user = (User) o;  // Convertimos el objeto a tipo User
        return Objects.equals(username, user.username);  // Comparamos los nombres de usuario
    }

    /**
     * Genera un código hash para el usuario basado en su nombre de usuario.
     *
     * @return El código hash generado para el usuario.
     */
    @Override
    public int hashCode() {
        return Objects.hash(username);  // Usamos el nombre de usuario para generar el código hash
    }

    /**
     * Genera una representación en cadena del usuario.
     *
     * @return El nombre de usuario como cadena.
     */
    @Override
    public String toString() {
        return username;  // Retorna el nombre de usuario como representación en cadena
    }
}
