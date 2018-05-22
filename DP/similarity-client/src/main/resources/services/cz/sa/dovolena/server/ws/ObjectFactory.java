
package cz.sa.dovolena.server.ws;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the cz.sa.dovolena.server.ws package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _AttachResponse_QNAME = new QName("http://ws.server.dovolena.sa.cz/", "attachResponse");
    private final static QName _LoginResponse_QNAME = new QName("http://ws.server.dovolena.sa.cz/", "loginResponse");
    private final static QName _GetDataToCompareResponse_QNAME = new QName("http://ws.server.dovolena.sa.cz/", "getDataToCompareResponse");
    private final static QName _Detach_QNAME = new QName("http://ws.server.dovolena.sa.cz/", "detach");
    private final static QName _SubmitResultResponse_QNAME = new QName("http://ws.server.dovolena.sa.cz/", "submitResultResponse");
    private final static QName _ChallengeLoginResponse_QNAME = new QName("http://ws.server.dovolena.sa.cz/", "challengeLoginResponse");
    private final static QName _LogError_QNAME = new QName("http://ws.server.dovolena.sa.cz/", "logError");
    private final static QName _SubmitResult_QNAME = new QName("http://ws.server.dovolena.sa.cz/", "submitResult");
    private final static QName _LogErrorResponse_QNAME = new QName("http://ws.server.dovolena.sa.cz/", "logErrorResponse");
    private final static QName _ChallengeLogin_QNAME = new QName("http://ws.server.dovolena.sa.cz/", "challengeLogin");
    private final static QName _Attach_QNAME = new QName("http://ws.server.dovolena.sa.cz/", "attach");
    private final static QName _CheckLogin_QNAME = new QName("http://ws.server.dovolena.sa.cz/", "checkLogin");
    private final static QName _Challenge_QNAME = new QName("http://ws.server.dovolena.sa.cz/", "challenge");
    private final static QName _CheckLoginResponse_QNAME = new QName("http://ws.server.dovolena.sa.cz/", "checkLoginResponse");
    private final static QName _DetachResponse_QNAME = new QName("http://ws.server.dovolena.sa.cz/", "detachResponse");
    private final static QName _GetDataToCompare_QNAME = new QName("http://ws.server.dovolena.sa.cz/", "getDataToCompare");
    private final static QName _ChallengeResponse_QNAME = new QName("http://ws.server.dovolena.sa.cz/", "challengeResponse");
    private final static QName _Login_QNAME = new QName("http://ws.server.dovolena.sa.cz/", "login");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: cz.sa.dovolena.server.ws
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Detach }
     * 
     */
    public Detach createDetach() {
        return new Detach();
    }

    /**
     * Create an instance of {@link AttachResponse }
     * 
     */
    public AttachResponse createAttachResponse() {
        return new AttachResponse();
    }

    /**
     * Create an instance of {@link LoginResponse }
     * 
     */
    public LoginResponse createLoginResponse() {
        return new LoginResponse();
    }

    /**
     * Create an instance of {@link GetDataToCompareResponse }
     * 
     */
    public GetDataToCompareResponse createGetDataToCompareResponse() {
        return new GetDataToCompareResponse();
    }

    /**
     * Create an instance of {@link LogErrorResponse }
     * 
     */
    public LogErrorResponse createLogErrorResponse() {
        return new LogErrorResponse();
    }

    /**
     * Create an instance of {@link LogError }
     * 
     */
    public LogError createLogError() {
        return new LogError();
    }

    /**
     * Create an instance of {@link SubmitResult }
     * 
     */
    public SubmitResult createSubmitResult() {
        return new SubmitResult();
    }

    /**
     * Create an instance of {@link ChallengeLoginResponse }
     * 
     */
    public ChallengeLoginResponse createChallengeLoginResponse() {
        return new ChallengeLoginResponse();
    }

    /**
     * Create an instance of {@link SubmitResultResponse }
     * 
     */
    public SubmitResultResponse createSubmitResultResponse() {
        return new SubmitResultResponse();
    }

    /**
     * Create an instance of {@link CheckLoginResponse }
     * 
     */
    public CheckLoginResponse createCheckLoginResponse() {
        return new CheckLoginResponse();
    }

    /**
     * Create an instance of {@link Challenge }
     * 
     */
    public Challenge createChallenge() {
        return new Challenge();
    }

    /**
     * Create an instance of {@link CheckLogin }
     * 
     */
    public CheckLogin createCheckLogin() {
        return new CheckLogin();
    }

    /**
     * Create an instance of {@link Attach }
     * 
     */
    public Attach createAttach() {
        return new Attach();
    }

    /**
     * Create an instance of {@link ChallengeLogin }
     * 
     */
    public ChallengeLogin createChallengeLogin() {
        return new ChallengeLogin();
    }

    /**
     * Create an instance of {@link Login }
     * 
     */
    public Login createLogin() {
        return new Login();
    }

    /**
     * Create an instance of {@link ChallengeResponse }
     * 
     */
    public ChallengeResponse createChallengeResponse() {
        return new ChallengeResponse();
    }

    /**
     * Create an instance of {@link GetDataToCompare }
     * 
     */
    public GetDataToCompare createGetDataToCompare() {
        return new GetDataToCompare();
    }

    /**
     * Create an instance of {@link DetachResponse }
     * 
     */
    public DetachResponse createDetachResponse() {
        return new DetachResponse();
    }

    /**
     * Create an instance of {@link WsChallenge }
     * 
     */
    public WsChallenge createWsChallenge() {
        return new WsChallenge();
    }

    /**
     * Create an instance of {@link SimilarityData }
     * 
     */
    public SimilarityData createSimilarityData() {
        return new SimilarityData();
    }

    /**
     * Create an instance of {@link SFileSource }
     * 
     */
    public SFileSource createSFileSource() {
        return new SFileSource();
    }

    /**
     * Create an instance of {@link SUser }
     * 
     */
    public SUser createSUser() {
        return new SUser();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AttachResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.server.dovolena.sa.cz/", name = "attachResponse")
    public JAXBElement<AttachResponse> createAttachResponse(AttachResponse value) {
        return new JAXBElement<AttachResponse>(_AttachResponse_QNAME, AttachResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LoginResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.server.dovolena.sa.cz/", name = "loginResponse")
    public JAXBElement<LoginResponse> createLoginResponse(LoginResponse value) {
        return new JAXBElement<LoginResponse>(_LoginResponse_QNAME, LoginResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetDataToCompareResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.server.dovolena.sa.cz/", name = "getDataToCompareResponse")
    public JAXBElement<GetDataToCompareResponse> createGetDataToCompareResponse(GetDataToCompareResponse value) {
        return new JAXBElement<GetDataToCompareResponse>(_GetDataToCompareResponse_QNAME, GetDataToCompareResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Detach }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.server.dovolena.sa.cz/", name = "detach")
    public JAXBElement<Detach> createDetach(Detach value) {
        return new JAXBElement<Detach>(_Detach_QNAME, Detach.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SubmitResultResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.server.dovolena.sa.cz/", name = "submitResultResponse")
    public JAXBElement<SubmitResultResponse> createSubmitResultResponse(SubmitResultResponse value) {
        return new JAXBElement<SubmitResultResponse>(_SubmitResultResponse_QNAME, SubmitResultResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ChallengeLoginResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.server.dovolena.sa.cz/", name = "challengeLoginResponse")
    public JAXBElement<ChallengeLoginResponse> createChallengeLoginResponse(ChallengeLoginResponse value) {
        return new JAXBElement<ChallengeLoginResponse>(_ChallengeLoginResponse_QNAME, ChallengeLoginResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LogError }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.server.dovolena.sa.cz/", name = "logError")
    public JAXBElement<LogError> createLogError(LogError value) {
        return new JAXBElement<LogError>(_LogError_QNAME, LogError.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SubmitResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.server.dovolena.sa.cz/", name = "submitResult")
    public JAXBElement<SubmitResult> createSubmitResult(SubmitResult value) {
        return new JAXBElement<SubmitResult>(_SubmitResult_QNAME, SubmitResult.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LogErrorResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.server.dovolena.sa.cz/", name = "logErrorResponse")
    public JAXBElement<LogErrorResponse> createLogErrorResponse(LogErrorResponse value) {
        return new JAXBElement<LogErrorResponse>(_LogErrorResponse_QNAME, LogErrorResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ChallengeLogin }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.server.dovolena.sa.cz/", name = "challengeLogin")
    public JAXBElement<ChallengeLogin> createChallengeLogin(ChallengeLogin value) {
        return new JAXBElement<ChallengeLogin>(_ChallengeLogin_QNAME, ChallengeLogin.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Attach }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.server.dovolena.sa.cz/", name = "attach")
    public JAXBElement<Attach> createAttach(Attach value) {
        return new JAXBElement<Attach>(_Attach_QNAME, Attach.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CheckLogin }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.server.dovolena.sa.cz/", name = "checkLogin")
    public JAXBElement<CheckLogin> createCheckLogin(CheckLogin value) {
        return new JAXBElement<CheckLogin>(_CheckLogin_QNAME, CheckLogin.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Challenge }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.server.dovolena.sa.cz/", name = "challenge")
    public JAXBElement<Challenge> createChallenge(Challenge value) {
        return new JAXBElement<Challenge>(_Challenge_QNAME, Challenge.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CheckLoginResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.server.dovolena.sa.cz/", name = "checkLoginResponse")
    public JAXBElement<CheckLoginResponse> createCheckLoginResponse(CheckLoginResponse value) {
        return new JAXBElement<CheckLoginResponse>(_CheckLoginResponse_QNAME, CheckLoginResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DetachResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.server.dovolena.sa.cz/", name = "detachResponse")
    public JAXBElement<DetachResponse> createDetachResponse(DetachResponse value) {
        return new JAXBElement<DetachResponse>(_DetachResponse_QNAME, DetachResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetDataToCompare }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.server.dovolena.sa.cz/", name = "getDataToCompare")
    public JAXBElement<GetDataToCompare> createGetDataToCompare(GetDataToCompare value) {
        return new JAXBElement<GetDataToCompare>(_GetDataToCompare_QNAME, GetDataToCompare.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ChallengeResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.server.dovolena.sa.cz/", name = "challengeResponse")
    public JAXBElement<ChallengeResponse> createChallengeResponse(ChallengeResponse value) {
        return new JAXBElement<ChallengeResponse>(_ChallengeResponse_QNAME, ChallengeResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Login }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.server.dovolena.sa.cz/", name = "login")
    public JAXBElement<Login> createLogin(Login value) {
        return new JAXBElement<Login>(_Login_QNAME, Login.class, null, value);
    }

}
