package common.model;

/**
 * La clase {@code Message} representa un mensaje enviado por un usuario en el sistema de chat.
 * Cada mensaje contiene información sobre el usuario que lo envió y el texto del mensaje.
 *
 * Esta clase permite almacenar, acceder y modificar la información de un mensaje.
 */
public class Message {

    /** El usuario que envió el mensaje. */
    private User user;

    /** El texto del mensaje. */
    private String text;

    /**
     * Constructor de la clase {@code Message}.
     *
     * @param user El usuario que envía el mensaje.
     * @param text El texto del mensaje.
     */
    public Message(User user, String text) {
        this.user = user;
        this.text = text;
    }

    /**
     * Obtiene el usuario que envió el mensaje.
     *
     * @return El usuario que envió el mensaje.
     */
    public User getUser() {
        return user;
    }

    /**
     * Obtiene el texto del mensaje.
     *
     * @return El texto del mensaje.
     */
    public String getText() {
        return text;
    }

    /**
     * Establece el usuario que envió el mensaje.
     *
     * @param user El nuevo usuario que envía el mensaje.
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Establece el texto del mensaje.
     *
     * @param text El nuevo texto del mensaje.
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Genera una representación en cadena del mensaje.
     *
     * @return Una cadena que representa el mensaje, con el formato "[nombreDeUsuario] textoDelMensaje".
     */
    @Override
    public String toString() {
        return "[" + user.getUsername() + "] " + text;  // Representación del mensaje con el nombre de usuario y el texto
    }
}
