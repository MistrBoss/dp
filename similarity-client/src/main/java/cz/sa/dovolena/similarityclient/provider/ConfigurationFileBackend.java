package cz.sa.dovolena.similarityclient.provider;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "backend")
public class ConfigurationFileBackend {

    @XmlElement(name = "domain_url")
    private final String domainUrl;

    @XmlElement(name = "username")
    private final String username;

    @XmlElement(name = "password")
    private final String password;

    @XmlElement(name = "search_logging")
    private final boolean searchLoggingEnabled;

    public ConfigurationFileBackend(
            final String domainUrl,
            final String username,
            final String password
    ) {
        this.searchLoggingEnabled = false;

        this.domainUrl = domainUrl;
        this.username = username;
        this.password = password;

    }

    public String getDomainUrl() {
        return domainUrl;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public boolean isSearchLoggingEnabled() {
        return searchLoggingEnabled;
    }

};
