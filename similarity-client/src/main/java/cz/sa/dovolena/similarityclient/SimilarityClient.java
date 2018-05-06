package cz.sa.dovolena.similarityclient;

import cz.sa.dovolena.server.ws.SFileSource;
import cz.sa.dovolena.similarityclient.provider.similarity.SimilarityQueueBackendProvider;
import cz.sa.dovolena.server.ws.SimilarityData;
import static cz.sa.dovolena.similarityclient.ImageDownloader.SERVLET;
import cz.sa.dovolena.similarityclient.similarity.ImageComparatorWithVariableScale;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;
import org.apache.log4j.Logger;

/**
 *
 * @author dobroslav.pelc
 */
public class SimilarityClient {

    private static final Logger LOG;
    private static final long SLEEP_TIME;
    private static final String SLAVE_ID;

    static {

        Settings.initLogger();
        LOG = Logger.getLogger(SimilarityClient.class);

        try {
            Settings.initConfiguration();
            System.setProperty("java.awt.headless", "false");

            SLEEP_TIME = 1000 * 60L;
            final InetAddress addr = InetAddress.getLocalHost();
            SLAVE_ID = addr.getHostName();

        } catch (final UnknownHostException ex) {
            LOG.fatal("Hostname can not be resolved", ex);
            throw new IllegalArgumentException("Hostname can not be resolved", ex);

        } catch (final Throwable t) {
            LOG.fatal("Unknowen error by project inicialization", t);
            throw new IllegalArgumentException("Unknowen error by project inicialization", t);
        }

    }

    /**
     * @param args the command line arguments
     *
     * @throws java.lang.InterruptedException
     * @throws java.net.MalformedURLException
     */
    public static void main(String[] args) throws InterruptedException, MalformedURLException, Throwable {

        /* Buď se připojím na server, nebo skončím */
        final SimilarityQueueBackendProvider provider;
        try {
            provider = new SimilarityQueueBackendProvider();

            try {
                LOG.info("Connecting on " + Settings.getConfiguration().getDomainUrl());
                provider.attach(SLAVE_ID);

            } catch (final Throwable t) {
                LOG.fatal("Connecting failed - try reconnecting");
                provider.detach(SLAVE_ID);
                provider.attach(SLAVE_ID);
            }

        } catch (final Throwable t) {
            LOG.fatal("Connecting failed", t);
            throw t;
        }

        /* Podpora pro detach při ukončení */
        Runtime.getRuntime().addShutdownHook(
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            LOG.info("Disconnecting from " + Settings.getConfiguration().getDomainUrl());
                            provider.detach(SLAVE_ID);
                        } catch (final Throwable t) {
                            LOG.fatal("Disconnection failed", t);
                        }
                    }
                }
        );

        /* Podpora pro neuspávání stroje */
        new Thread(
                new Runnable() {

                    @Override
                    public void run() {
                        try {
                            while (!Thread.currentThread().isInterrupted()) {
                                final boolean state = Toolkit.getDefaultToolkit().getLockingKeyState(KeyEvent.VK_SCROLL_LOCK);
                                Toolkit.getDefaultToolkit().setLockingKeyState(KeyEvent.VK_SCROLL_LOCK, !state);
                                Thread.sleep(10000);
                            }

                        } catch (final InterruptedException e) {
                            LOG.warn("Keep-awaking thread was interrupted", e);

                        } catch (final Throwable t) {
                            LOG.fatal("Keep-awaking thread failed", t);
                        }
                    }
                }
        ).start();

        /* Jsem připojený a na konci se musím odpojit od serveru */
        while (!Thread.currentThread().isInterrupted()) {

            try {
                final SimilarityData data = provider.getDataToCompare(SLAVE_ID);
                if (data != null) {
                    final Double variableSimilarity = process(provider, data);
                    LOG.debug("Submit result to the server (" + variableSimilarity + ")");
                    data.setSimilarity(variableSimilarity);
                    provider.submitResult(SLAVE_ID, data);

                } else {
                    LOG.debug("No data to process, sleep " + SLEEP_TIME + " ms.");
                    Thread.sleep(SLEEP_TIME);
                }

            } catch (final InterruptedException e) {
                LOG.warn("Working thread was interrupted", e);

            } catch (final Throwable t) {
                tryLogging(t, provider);
            }
        }
    }

    private static void tryLogging(
            final Throwable t,
            final SimilarityQueueBackendProvider provider
    ) {

        final String msg = t.getMessage();
        LOG.fatal("Reporting unknowen error to the server", t);

        try {
            provider.logError(
                    SLAVE_ID,
                    msg == null
                            ? "null"
                            : msg
            );

        } catch (final Throwable th) {
            LOG.fatal("Repoting to the server failed", th);
        }
    }

    private static Double process(
            final SimilarityQueueBackendProvider provider,
            final SimilarityData data
    ) throws NoSuchAlgorithmException, IOException {

        final String cookie = provider.getCookie();
        final SFileSource pattern = data.getPattern();
        final String patternAbsolutePath = ImageDownloader.downloadImage(pattern, cookie);
        if (patternAbsolutePath == null) {
            throw new IllegalStateException(
                    "Image '"
                    + Settings.getConfiguration().getDomainUrl()
                    + SERVLET
                    + "?name=" + pattern.getRelativePath() + "' "
                    + "was not correctly downloaded"
            );
        }

        /* Scale bez ohledu na vzorový obrázek */
        LOG.trace(("is java.awt.headles:           " + GraphicsEnvironment.isHeadless()));
        final ImageComparatorWithVariableScale variableFinder = new ImageComparatorWithVariableScale(patternAbsolutePath);
        variableFinder.bindModel();

        final SFileSource compare = data.getCompare();
        final String compareAbsolutePath = ImageDownloader.downloadImage(compare, cookie);
        if (compareAbsolutePath == null) {
            throw new IllegalStateException(
                    "Image '"
                    + Settings.getConfiguration().getDomainUrl()
                    + SERVLET
                    + "?name=" + compare.getRelativePath() + "' "
                    + "was not correctly downloaded"
            );
        }

        LOG.trace(("is java.awt.headles:           " + GraphicsEnvironment.isHeadless()));
        return variableFinder.compare(compareAbsolutePath);
    }
}
