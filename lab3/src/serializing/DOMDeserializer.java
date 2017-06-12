package serializing;

import model.message.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.BlockingQueue;

public class DOMDeserializer {

    private static final String COMMAND = "command";
    private static final String EVENT = "event";
    private static final String ERROR = "error";
    private static final String SUCCESS = "success";

    private static final String LOGIN = "login";
    private static final String LIST = "list";
    private static final String MESSAGE = "message";
    private static final String LOGOUT = "logout";

    private static final String USERLOGIN = "userlogin";
    private static final String USERLOGOUT = "userlogout";
    private static final String NAME = "name";

    private byte[] data;
    private DocumentBuilder documentBuilder;
    private BlockingQueue<Class> sentMessageTypes;
    private static final Logger log = LogManager.getLogger(DOMDeserializer.class);

    public DOMDeserializer() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            documentBuilder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            log.error("parsing : creating deserializer error");
        }
    }

    public void setQueue(BlockingQueue<Class> sentMessageTypes) {
        this.sentMessageTypes = sentMessageTypes;
    }

    private Message unmarshallMessage(Class c) {
        try {
            JAXBContext context = JAXBContext.newInstance(c);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            String sdata = new String(data, StandardCharsets.UTF_8);
            return (Message) unmarshaller.unmarshal(new StringReader(sdata));
        } catch (JAXBException e) {
            log.error("parsing : JAXB deserializing error");
        }
        return null;
    }

    private Class getAppropriateClass(String type, Class sentMessage) {
        if (sentMessage.equals(TextMessage.class)) {
            return (type.equals(SUCCESS)) ? TextSuccess.class : TextError.class;
        }
        if (sentMessage.equals(LoginRequest.class)) {
            return (type.equals(SUCCESS)) ? LoginSuccess.class : LoginError.class;
        }
        if (sentMessage.equals(LogoutRequest.class)) {
            return (type.equals(SUCCESS)) ? LogoutSuccess.class : LogoutError.class;
        }
        if (sentMessage.equals(ListUsersRequest.class)) {
            return (type.equals(SUCCESS)) ? ListUsersSuccess.class : ListUsersError.class;
        }
        return null;
    }

    private Message parse(Document doc) {
        Element root = doc.getDocumentElement();
        try {
            log.info("parsing : root element={}", root.getNodeName());
            switch (root.getNodeName()) {
                case COMMAND:
                    log.info("parsing : root attribute={}", root.getAttribute(NAME));
                    switch (root.getAttribute(NAME)) {
                        case LIST: return unmarshallMessage(ListUsersRequest.class);
                        case LOGIN: return unmarshallMessage(LoginRequest.class);
                        case LOGOUT: return unmarshallMessage(LogoutRequest.class);
                        case MESSAGE: return unmarshallMessage(TextMessage.class);
                    }
                case EVENT:
                  log.info("parsing : root attribute={}", root.getAttribute(NAME));
                    switch (root.getAttribute(NAME)) {
                        case MESSAGE: return unmarshallMessage(UserMessage.class);
                        case USERLOGIN: return unmarshallMessage(UserLoginMessage.class);
                        case USERLOGOUT: return unmarshallMessage(UserLogoutMessage.class);
                    }
                case ERROR: return unmarshallMessage(getAppropriateClass(ERROR, sentMessageTypes.take()));
                case SUCCESS: return unmarshallMessage(getAppropriateClass(SUCCESS, sentMessageTypes.take()));
            }
        } catch (InterruptedException e) {
            log.error("parsing : interrupted");
        }
        return null;
    }

    public Message deserialize(byte[] data) throws SAXException, IOException {
        this.data = data;
        Document doc = documentBuilder.parse(new InputSource(new InputStreamReader(new ByteArrayInputStream(data,0,data.length),"UTF-8")));
        return parse(doc);
    }
}
