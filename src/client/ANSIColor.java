package client;

public enum ANSIColor {
    RESET("\033[0m"),
    BOLD("\033[1m"),
    UNDERLINE("\033[4m"),
    GREEN("\033[32m"),
    RED("\033[31m"),
    CYAN("\033[36m"),
    YELLOW("\033[33m");
    private final String ansi;

    ANSIColor(String ansi) {
        this.ansi = ansi;
    }

    public String get() {
        return ansi;
    }
}
