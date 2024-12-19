package common.logger;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.*;

public class ChatLogger {

    private static final Logger logger = ChatLogger.getLogger(ChatLogger.class.getName());

    static {
        Logger rootLogger = Logger.getLogger("");
        Handler[] handlers = rootLogger.getHandlers();
        for (Handler handler : handlers) {
            rootLogger.removeHandler(handler);
        }

        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setFormatter(new CustomFormatter(true));
        rootLogger.addHandler(consoleHandler);

        try {
            FileHandler fileHandler = new FileHandler("log.txt", true);
            fileHandler.setFormatter(new CustomFormatter(false));
            rootLogger.addHandler(fileHandler);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "log.txt Not Found.", e);
        }

        rootLogger.setLevel(Level.ALL);
        consoleHandler.setLevel(Level.ALL);
    }

    public static Logger getLogger(String className) {
        return Logger.getLogger(className);
    }

    private static class CustomFormatter extends Formatter {
        private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        private final boolean isConsole;

        public CustomFormatter(boolean isConsole) {
            this.isConsole = isConsole;
        }

        @Override
        public String format(LogRecord record) {
            String timestamp = dateFormat.format(new Date(record.getMillis()));
            String className = record.getLoggerName();
            String level = record.getLevel().getName();
            String message = formatMessage(record);

            String color = isConsole ? ColorLog.getColorForLevel(record.getLevel()) : "";

            String logMessage = String.format("%s[%s] <%s> [%s]: %s%s\n", color, timestamp, className, level, message, isConsole ? "\u001B[0m" : "");

            if (record.getThrown() != null) {
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                record.getThrown().printStackTrace(pw);
                logMessage += sw.toString();
            }

            return logMessage;
        }
    }
}
