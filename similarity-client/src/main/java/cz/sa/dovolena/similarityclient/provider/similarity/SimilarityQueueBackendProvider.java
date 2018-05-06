package cz.sa.dovolena.similarityclient.provider.similarity;

import com.google.common.base.Preconditions;
import cz.sa.dovolena.server.ws.SimilarityData;
import cz.sa.dovolena.server.ws.SimilarityQueueService;
import cz.sa.dovolena.server.ws.SimilarityQueueServiceImplService;
import cz.sa.dovolena.server.ws.WsChallenge;
import cz.sa.dovolena.similarityclient.Settings;
import cz.sa.dovolena.similarityclient.provider.BackendProvider;
import cz.sa.dovolena.similarityclient.provider.ConfigurationFileBackend;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import javax.xml.ws.BindingProvider;
import java.net.URL;
import java.net.MalformedURLException;

import static javax.xml.ws.handler.MessageContext.HTTP_RESPONSE_HEADERS;

/**
 *
 * @author dobroslav.pelc
 */
public class SimilarityQueueBackendProvider
        extends BackendProvider<SimilarityQueueService>
        implements SimilarityQueueProvider {

    private static final String SERVICE_PREFIX = "SimilarityQueue";
    private static final String SET_COOKIE = "Set-Cookie";

    private final ConfigurationFileBackend config;
    private static SimilarityQueueService PORT;

    private String cookieName;

    public SimilarityQueueBackendProvider() throws IOException {
        config = Settings.getConfiguration();
        setConfiguration(config);
    }

    @Override
    protected SimilarityQueueService loadService() throws MalformedURLException {

        assert (address != null);

        final URL urlAddress = new URL(address);
        final SimilarityQueueServiceImplService service = new SimilarityQueueServiceImplService(urlAddress);

        if (PORT == null) {
            PORT = connect(
                    service.getSimilarityQueueServiceImplPort()
            );
        }

        return PORT;
    }

    @Override
    protected void initService() throws MalformedURLException {
        super.initService();
        refreshCookieName();
    }

    private void refreshCookieName() {

        final Map<String, Object> responseContext = ((BindingProvider) PORT).getResponseContext();
        if (responseContext != null
                && responseContext.containsKey(HTTP_RESPONSE_HEADERS)) {

            final Map<String, Object> responsseHeader = (Map<String, Object>) responseContext.get(HTTP_RESPONSE_HEADERS);

            if (responsseHeader != null
                    && responsseHeader.containsKey(SET_COOKIE)) {

                final Object rawCookies = responsseHeader.get(SET_COOKIE);

                if (rawCookies != null
                        && rawCookies instanceof Collection) {

                    final Collection<String> cookies = (Collection<String>) rawCookies;

                    final StringBuilder cookiesValue = new StringBuilder();
                    for (final String cookieValue : cookies) {
                        if (cookiesValue.length() > 0) {
                            cookiesValue.append("; ");
                        }
                        cookiesValue.append(cookieValue);
                    }

                    cookieName = cookiesValue.toString();
                }
            }
        }
    }

    @Override
    protected WsChallenge challenge(
            final SimilarityQueueService port,
            final String user
    ) {
        assert (port != null);
        assert (user != null);

        final WsChallenge result = port.challenge(user);
        refreshCookieName();

        return result;
    }

    @Override
    protected boolean challengeLogin(
            final SimilarityQueueService port,
            final String user,
            final String pass,
            final Long challengeId
    ) {
        assert (port != null);
        assert (user != null);
        assert (pass != null);
        assert (challengeId != null);

        final boolean result = port.challengeLogin(user, pass, challengeId);
        refreshCookieName();

        return result;
    }

    @Override
    protected boolean checkLogin(
            final SimilarityQueueService port
    ) {
        assert (port != null);

        final boolean result = port.checkLogin();
        refreshCookieName();

        return result;
    }

    @Override
    protected String getServicePrefix() {
        return SERVICE_PREFIX;
    }

    @Override
    public void attach(final String slaveId) throws MalformedURLException {
        Preconditions.checkNotNull(slaveId, "SlaveId nesmí být null");

        initService();
        PORT.attach(slaveId);
    }

    @Override
    public void detach(final String slaveId) throws MalformedURLException {
        Preconditions.checkNotNull(slaveId, "SlaveId nesmí být null");

        initService();
        PORT.detach(slaveId);
    }

    @Override
    public SimilarityData getDataToCompare(final String slaveId) throws MalformedURLException {
        Preconditions.checkNotNull(slaveId, "SlaveId nesmí být null");

        initService();
        return PORT.getDataToCompare(slaveId);
    }

    @Override
    public void submitResult(final String slaveId, final SimilarityData result) throws MalformedURLException {

        Preconditions.checkNotNull(slaveId, "SlaveId nesmí být null");
        Preconditions.checkNotNull(result, "Result nesmí být null");

        initService();
        PORT.submitResult(slaveId, result);
    }

    @Override
    public void logError(String slaveId, String logMessage) throws MalformedURLException {
        Preconditions.checkNotNull(slaveId, "SlaveId nesmí být null");
        Preconditions.checkNotNull(logMessage, "LogMessage nesmí být null");

        initService();
        PORT.logError(slaveId, logMessage);
    }

    @Override
    public String getCookie() {
        return cookieName;
    }
}
