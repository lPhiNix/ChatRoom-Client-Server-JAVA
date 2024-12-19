package client;

/**
 * Enum ANSIColor utilizado para el formateo de colores de {@link UDPClient}
 */
public enum ANSIColor {
    RESET("\033[0m"),            // Resetea el color y estilo a los predeterminados
    BOLD("\033[1m"),             // Aplica el estilo de texto en negrita
    UNDERLINE("\033[4m"),        // Aplica el subrayado al texto
    GREEN("\033[32m"),           // Establece el color verde para el texto
    RED("\033[31m"),             // Establece el color rojo para el texto
    CYAN("\033[36m"),            // Establece el color cian para el texto
    YELLOW("\033[33m");          // Establece el color amarillo para el texto

    private final String ansi;   // Almacena el código de escape ANSI correspondiente

    /**
     * Constructor del enumerado. Asocia cada valor del enumerado con su código ANSI correspondiente.
     *
     * @param ansi Código de escape ANSI que aplica el color o estilo correspondiente.
     */
    ANSIColor(String ansi) {
        this.ansi = ansi;
    }

    /**
     * Obtiene el código ANSI correspondiente.
     *
     * @return El código de escape ANSI como una cadena.
     */
    public String get() {
        return ansi;
    }
}