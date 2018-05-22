package cz.sa.dovolena.similarityclient;

import cz.sa.dovolena.similarityclient.logging.LiveViewAppender;
import cz.sa.dovolena.similarityclient.provider.ConfigurationFileBackend;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.RollingFileAppender;

/**
 *
 * @author dobroslav.pelc
 */
public class Settings {

    public static final String HOME_DIR_NAME;
    public static final String HOME_DIR;
    public static final String IMG_DIR_NAME;
    public static final String IMG_DIR;
    public static final String LOG_DIR_NAME;
    public static final String LOG_DIR;

    public static final String SERVER_LOG;
    public static final PatternLayout CONSOLE_PATTERN;
    public static final String EXPORT;

    public static final String LOG4J_PROPERTIES_NAME;
    public static final String LOG4J_PROPERTIES_AS_RESOURCE;
    public static final String LOG4J_PROPERTIES_EXPORTED;

    public static final String USER_PROPERTIES_NAME;
    public static final String USER_PROPERTIES_AS_RESOURCE;
    public static final String USER_PROPERTIES_EXPORTED;

    private static final String DOMAIN;
    private static final String USER;
    private static final String PASS;

    private static ConfigurationFileBackend configuration;

    /**
     * Appender umoznujici nacitat logy podle casu
     */
    private static final LiveViewAppender LIVE_VIEW_APPENDER;

    static {
        DOMAIN = "domain";
        USER = "user";
        PASS = "pass";

        HOME_DIR_NAME = ".similarity-client";
        HOME_DIR = System.getProperty("user.home") + File.separator + HOME_DIR_NAME;
        initDir(HOME_DIR);

        LOG_DIR_NAME = "log";
        LOG_DIR = HOME_DIR + File.separator + LOG_DIR_NAME;
        initDir(LOG_DIR);

        IMG_DIR_NAME = "images";
        IMG_DIR = HOME_DIR + File.separator + IMG_DIR_NAME;
        initDir(IMG_DIR);

        EXPORT = "export/";

        LOG4J_PROPERTIES_NAME = "log4j.properties";
        LOG4J_PROPERTIES_AS_RESOURCE = EXPORT + LOG4J_PROPERTIES_NAME;
        LOG4J_PROPERTIES_EXPORTED = HOME_DIR + File.separator + LOG4J_PROPERTIES_NAME;

        SERVER_LOG = LOG_DIR + File.separator + "similarity-client.log";
        CONSOLE_PATTERN = new PatternLayout("%d{MM/dd HH:mm:ss} %5p [%4.4t] (%30.30F:%3L) %m%n");
        LIVE_VIEW_APPENDER = new LiveViewAppender(CONSOLE_PATTERN);

        USER_PROPERTIES_NAME = "user.properties";
        USER_PROPERTIES_AS_RESOURCE = EXPORT + USER_PROPERTIES_NAME;
        USER_PROPERTIES_EXPORTED = HOME_DIR + File.separator + USER_PROPERTIES_NAME;
    }

    private static void initDir(final String dir) {
        final File homeDirFile = new File(dir);
        if (!homeDirFile.exists()) {
            if (!homeDirFile.mkdirs()) {
                throw new RuntimeException("Nepodařilo se vytvořit adresář '" + HOME_DIR + "'");
            }
        }
    }

    /**
     * Zavede log4j. Dokud nedoběhne ÚSPĚŠNĚ tato metoda, není možné využívat log4j a je nutné logovat na STDOUT
     *
     * @throws SecurityException
     */
    public static void initLogger() throws SecurityException {
        System.out.println("Similarity client - init logger");
        /* export konfiguracniho souboru s nastavenim */
        try {
            exportResource(LOG4J_PROPERTIES_AS_RESOURCE, LOG4J_PROPERTIES_EXPORTED);

        } catch (Throwable t) {
            System.out.println("Similarity client - Error while distribution log4j configuration\n" + t.getMessage());
        }

        /* nejprve instalace loggeru! */
        Logger.getRootLogger().removeAllAppenders();
        PropertyConfigurator.configure(LOG4J_PROPERTIES_EXPORTED);

        /* inicializace logovani do souboru */
        try {
            final RollingFileAppender rollingFileAppender = new RollingFileAppender(
                    CONSOLE_PATTERN, SERVER_LOG
            );
            rollingFileAppender.setMaxFileSize("5000KB"); // 5MB je idealni pro stahovani pres net - dle Radka :)
            rollingFileAppender.setMaxBackupIndex(30);
            Logger.getRootLogger().addAppender(rollingFileAppender);
        } catch (IOException ex) {
            System.out.println("Similarity client - error while appending FileAppender\n" + ex.getMessage());
        }

        /* inicializace appenderu pro ziskavani logu podle casu zapisu */
        Logger.getRootLogger().addAppender(LIVE_VIEW_APPENDER);
    }

    /**
     * Zavede user.properties. Dokud nedoběhne ÚSPĚŠNĚ tato metoda, není možné se připojit na webové služby
     *
     * @throws SecurityException
     */
    public static void initConfiguration() throws SecurityException {
        System.out.println("Similarity client - init user.properties");
        /* export konfiguracniho souboru s nastavenim */
        try {
            exportResource(USER_PROPERTIES_AS_RESOURCE, USER_PROPERTIES_EXPORTED);

        } catch (Throwable t) {
            System.out.println("Similarity client - Error while distribution user.properties\n" + t.getMessage());
        }

    }

    public static ConfigurationFileBackend getConfiguration() throws IOException {

        if (configuration != null) {
            return configuration;
        }

        loadConfiguration();
        return configuration;
    }

    public static void loadConfiguration() throws IOException {
        try (final FileInputStream inputStream = new FileInputStream(new File(USER_PROPERTIES_EXPORTED))) {
            final Properties prop = new Properties();
            String propFileName = "config.properties";

            if (inputStream != null) {
                prop.load(inputStream);
            } else {
                throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
            }

            final String domain = prop.getProperty(DOMAIN);
            final String user = prop.getProperty(USER);
            final String pass = prop.getProperty(PASS);

            configuration = new ConfigurationFileBackend(domain, user, pass);
        }
    }

    private static String exportResource(
            final String resource,
            final String target
    ) throws Exception {

        assert (resource != null);
        assert (target != null);

        /* Resource je již vyexportovaný, přepisovat ho nebudu */
        if (new File(target).exists()) {
            return target;
        }

        try (final InputStream stream = Settings.class
                .getClassLoader()
                .getResourceAsStream(resource)) {

            if (stream == null) {
                System.out.println("Resource stream IS NULL " + resource);
                throw new Exception("Cannot get resource \"" + resource + "\" from Jar file.");
            }

            int readBytes;
            final byte[] buffer = new byte[4096];

            try (final OutputStream resStreamOut = new FileOutputStream(target)) {
                while ((readBytes = stream.read(buffer)) > 0) {
                    resStreamOut.write(buffer, 0, readBytes);
                }
            }

            return target;
        }
    }
}
