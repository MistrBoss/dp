package cz.sa.dovolena.similarityclient.provider;

import javax.xml.ws.BindingProvider;
import java.security.NoSuchAlgorithmException;

import cz.sa.dovolena.server.ws.WsChallenge;
import static cz.sa.dovolena.similarityclient.utils.Checksum.getSHA1SumOfString;
import java.net.MalformedURLException;

/**
 * Abstract backend SCRUD provider.
 *
 *
 * @author vladimir
 * @created 2015-04-12
 */
public abstract class BackendProvider<SERVICE> {

    private static final String SERVICE_SUFFIX = "SoapCrudService";

    protected static final String AUTH_HASH_ALGORITHM = "SHA-1";
    protected static final String ADDR_PREFIX = "/services/";

    protected String address;
    protected String username;
    protected String password;

    protected SERVICE port;

    /**
     * Set configuration at runtime.
     *
     *
     * @param config
     */
    public void setConfiguration(final ConfigurationFileBackend config) {

        this.address = config.getDomainUrl();
        this.username = config.getUsername();
        this.password = config.getPassword();

        finalizeEndpointAddress(address);
    }

    /**
     * Connect to the service.
     *
     *
     * @param service to connect to
     * @return connedcted service
     */
    protected SERVICE connect(final SERVICE service) {

        BindingProvider bp = (BindingProvider) service;

        bp.getRequestContext().put(
                BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
                address
        );

        bp.getRequestContext().put(
                BindingProvider.SESSION_MAINTAIN_PROPERTY,
                true
        );

        return service;
    }

    /**
     * Get the address prefix for this service. Returns something like '/service/endpoints/'.
     *
     * @return address prefix
     */
    protected String getAddressPrefix() {
        return ADDR_PREFIX;
    }

    protected void initService() throws MalformedURLException {

        if (port == null) {
            synchronized (this) {
                if (port == null) {
                    port = loadService();
                }
            }
        }

        if (!checkLogin(port)) {
            if (!challengeResponseAuth(port, username, password)) {
                throw new SecurityException(
                        "Permission denied at the server '" + address + "' "
                );
            }
        }

    }

    /**
     * Handle salted CRAM operation.
     *
     * @param port to which we authenticate to
     * @param user to auth
     * @param pass to supply
     *
     * @return true if auth was successfull
     */
    private boolean challengeResponseAuth(SERVICE port, String user, String pass) {

        assert (port != null);
        assert (user != null);
        assert (pass != null);

        WsChallenge c = challenge(port, user);

        if (c != null) {

            try {
                final String userPassHash = getSHA1SumOfString(
                        pass + c.getUserSalt()
                );
                final String challengePassHash = getSHA1SumOfString(
                        userPassHash + c.getChallengeSalt()
                );

                return challengeLogin(port, user, challengePassHash, c.getId());

            } catch (final NoSuchAlgorithmException ex) {
                throw new RuntimeException(ex);
            }

        }

        return false;
    }

    /**
     * Finalize endpoint address. This method assembles the final url and stores it to the protected field {
     *
     * @see #address}.
     *
     * @param prefixedAddress the base address to begin with
     */
    private void finalizeEndpointAddress(final String prefixedAddress) {

        String scrudTypeName = getServicePrefix();

        scrudTypeName = scrudTypeName.replaceAll(
                SERVICE_SUFFIX + "$", ""
        );

        address = prefixedAddress + getAddressPrefix() + scrudTypeName + "?wsdl";
    }

    /**
     * Load this web service client.
     *
     * @return service client instance
     * @throws java.net.MalformedURLException
     */
    protected abstract SERVICE loadService() throws MalformedURLException;

    /**
     * Get a challenge for SCRAM. Extending classes should implement this to enable salted challenge/response
     * authentication mechanism.
     *
     * @param port to the service
     * @param user name of the user
     *
     * @return challenge
     */
    protected abstract WsChallenge challenge(SERVICE port, String user);

    /**
     * Log in using the SCRAM method.
     *
     * @param port to the service
     * @param user name of the user
     * @param pass the password
     * @param challengeId identifier of the challenge
     *
     * @return the login was sucessfull and the session cookie is held by this client
     */
    protected abstract boolean challengeLogin(SERVICE port, String user, String pass, Long challengeId);

    /**
     * Check whether we are logged in. Because session cookies validity is limited, we should check the cookie every
     * time we want to use the service.
     *
     * @param port to the service
     * @return true when we are already logged in
     */
    protected abstract boolean checkLogin(SERVICE port);

    /**
     * Get the prefix for the service endpoint. Returns something like 'AffiliateSoapCrudService'. The 'SoapCrudService'
     * part is then cut out.
     *
     * @return endpoint prefix
     */
    protected abstract String getServicePrefix();

}
