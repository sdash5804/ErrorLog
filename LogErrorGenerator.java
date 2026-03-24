package test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

// IMPORTANT: use java.util.logging imports, not System.Logger
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LogErrorGenerator {

    private static final Path LOG_PATH = Paths.get("C:\\test", "app.log");

    public static void main(String[] args) {
        try {
            Files.createDirectories(LOG_PATH.getParent());

            FileHandler fh = new FileHandler(LOG_PATH.toString(), true);
            fh.setFormatter(new SimpleFormatter());
            fh.setLevel(Level.ALL);

            Logger logger = Logger.getLogger(LogErrorGenerator.class.getName());
            logger.setUseParentHandlers(false);

            // Avoid duplicate logs
            for (Handler h : logger.getHandlers()) {
                logger.removeHandler(h);
            }
            logger.addHandler(fh);
            logger.setLevel(Level.ALL);

            logger.info("=== Generating sample error for testing ===");

            try {
                String s = null;
                s.length(); // NPE
            } catch (Exception ex) {
                logger.log(Level.SEVERE, "Unhandled exception occurred", ex);
                logger.severe("Application failed: see stack trace above. Action=ProcessRequest, Environment=DEV");
            }

            fh.close();
        } catch (IOException ioe) {
            System.err.println("Failed to configure file logging: " + ioe);
            ioe.printStackTrace();
        }
    }
}
