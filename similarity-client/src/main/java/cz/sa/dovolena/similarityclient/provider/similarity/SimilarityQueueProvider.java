package cz.sa.dovolena.similarityclient.provider.similarity;

import cz.sa.dovolena.server.ws.SimilarityData;
import java.net.MalformedURLException;

/**
 *
 * @author dobroslav.pelc
 */
public interface SimilarityQueueProvider {

    void attach(String slaveId) throws MalformedURLException;
    
    void detach(String slaveId) throws MalformedURLException;
    
    SimilarityData getDataToCompare(String slaveId) throws MalformedURLException;

    void submitResult(String slaveId, SimilarityData result) throws MalformedURLException;
    
    void logError(String slaveId, String logMessage) throws MalformedURLException;

    String getCookie();
}
