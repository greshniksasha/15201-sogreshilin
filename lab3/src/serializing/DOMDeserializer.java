package serializing;

import model.Client;
import model.message.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
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
    private static final String LISTUSERS = "listusers";
    private static final String SESSION = "session";
    private static final String NAME = "name";

    private String data;
    private DocumentBuilder documentBuilder;
    private BlockingQueue<Class> sentMessageTypes;
    private static final Logger log = LogManager.getLogger(Client.class);

    public DOMDeserializer() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            documentBuilder = factory.newDocumentBuilder();
        } catch (Exception e) {
            log.error("creating deserializer error", e);
        }
    }

    public void setQueue(BlockingQueue<Class> sentMessageTypes) {
        this.sentMessageTypes = sentMessageTypes;
    }

    public Message serializeMessage(Class c) {
        Message message = null;
        try {
            JAXBContext context = JAXBContext.newInstance(c);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            message = (Message) unmarshaller.unmarshal(new StringReader(data));
        } catch (JAXBException e) {
            log.error("jaxb serializing message error");
        }
        return message;
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
            switch (root.getNodeName()) {
                case COMMAND:
                    switch (root.getAttribute(NAME)) {
                        case LIST: return serializeMessage(ListUsersRequest.class);
                        case LOGIN: return serializeMessage(LoginRequest.class);
                        case LOGOUT: return serializeMessage(LogoutRequest.class);
                        case MESSAGE: return serializeMessage(TextMessage.class);
                    }
                case EVENT:
                    switch (root.getAttribute(NAME)) {
                        case MESSAGE: return serializeMessage(UserMessage.class);
                        case USERLOGIN: return serializeMessage(UserLoginMessage.class);
                        case USERLOGOUT: return serializeMessage(UserLogoutMessage.class);
                    }
                case ERROR: return serializeMessage(getAppropriateClass(ERROR, sentMessageTypes.take()));
                case SUCCESS: return serializeMessage(getAppropriateClass(SUCCESS, sentMessageTypes.take()));
            }
        } catch (InterruptedException e) {
            log.error("interrupted while taking message from sentMessageTypes queue");
        }
        return null;
    }

    public Message deserialize(String data) {
        try {
            this.data = data;
            Document doc = documentBuilder.parse(new ByteArrayInputStream(data.getBytes()));
            return parse(doc);
        } catch (SAXException | IOException e) {
            log.error("parsing message error", e);
        }
        return null;
    }


}