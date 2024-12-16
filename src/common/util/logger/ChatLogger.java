package common.util.logger;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.*;

public class ChatLogger {
    private static final Logger logger = Logger.getLogger(ChatLogger.class.getName());

    static {
        Logger rootLogger = Logger.getLogger("");
        Handler[] handlers = rootLogger.getHandlers();
        for (Handler handler : handlers) {
            rootLogger.removeHandler(handler);
        }

        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setFormatter(new CustomFormatter());
        rootLogger.addHandler(consoleHandler);

        rootLogger.setLevel(Level.ALL);
        consoleHandler.setLevel(Level.ALL);
    }

    public static Logger getLogger(String className) {
        return Logger.getLogger(className);
    }

    private static class CustomFormatter extends Formatter {
        private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        @Override
        public String format(LogRecord record) {
            String timestamp = dateFormat.format(new Date(record.getMillis()));
            String className = record.getLoggerName();
            String level = record.getLevel().getName();
            String message = formatMessage(record);

            String color = ColorLog.getColorForLevel(record.getLevel());

            String logMessage = String.format("%s[%s] <%s> [%s]: %s\u001B[0m\n", color, timestamp, className, level, message);

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
