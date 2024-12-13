package common.util.logger;

import java.util.logging.Level;

public enum ColorLog {
    SEVERE("\u001B[31m"),
    WARNING("\u001B[93m"),
    INFO("\u001B[92m"),
    CONFIG("\u001B[36m"),
    DEFAULT("\u001B[0m");

    private final String color;

    ColorLog(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }

    public static String getColorForLevel(Level level) {
        try {
            return ColorLog.valueOf(level.getName()).getColor();
        } catch (IllegalArgumentException e) {
            return DEFAULT.getColor();
        }
    }
}