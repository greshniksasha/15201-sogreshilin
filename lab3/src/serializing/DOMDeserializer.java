//package serializing;
//
//import model.Client;
//import model.message.*;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import org.w3c.dom.Document;
//import org.w3c.dom.Element;
//import org.w3c.dom.Node;
//import org.xml.sax.SAXException;
//
//import javax.xml.bind.JAXBContext;
//import javax.xml.bind.JAXBException;
//import javax.xml.bind.Unmarshaller;
//import javax.xml.parsers.DocumentBuilder;
//import javax.xml.parsers.DocumentBuilderFactory;
//import java.io.File;
//import java.io.IOException;
//
//public class DOMDeserializer {
//
//    private static final String COMMAND = "command";
//    private static final String EVENT = "event";
//    private static final String ERROR = "error";
//    private static final String SUCCESS = "success";
//
//    private static final String LOGIN = "login";
//    private static final String LIST = "list";
//    private static final String MESSAGE = "message";
//    private static final String LOGOUT = "logout";
//
//    private static final String USERLOGIN = "userlogin";
//    private static final String USERLOGOUT = "userlogout";
//    private static final String LISTUSERS = "listusers";
//    private static final String SESSION = "session";
//    private static final String NAME = "name";
//
//    private File file;
//    private DocumentBuilder documentBuilder;
//    private static final Logger log = LogManager.getLogger(Client.class);
//
//    public DOMDeserializer() {
//        try {
//            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//            documentBuilder = factory.newDocumentBuilder();
//        } catch (Exception e) {
//            log.error("creating deserializer error", e);
//        }
//    }
//
//    public Object getObject(File file, Class c) {
//        Object object = null;
//        try {
//            JAXBContext context = JAXBContext.newInstance(c);
//            Unmarshaller unmarshaller = context.createUnmarshaller();
//            object = unmarshaller.unmarshal(file);
//        } catch (JAXBException e) {
//            log.error("jaxb serializing message error");
//        }
//        return object;
//    }
//
//    private Message parse(Document doc) {
//        Element root = doc.getDocumentElement();
//        switch (root.getNodeName()) {
//            case COMMAND :
//                switch (root.getAttribute(NAME)) {
//                    case LIST: return (ListUsersRequest)getObject(file, ListUsersRequest.class);
//                    case LOGIN: return (LoginRequest)getObject(file, LoginRequest.class);
//                    case LOGOUT: return (LogoutRequest)getObject(file, LogoutRequest.class);
//                    case MESSAGE: return (TextMessage)getObject(file, TextMessage.class);
//                }
//            case EVENT :
//                switch (root.getAttribute(NAME)) {
////                    case MESSAGE: return ()getObject(file, TextMessage.class);
//                    case USERLOGIN: return (UserLoginMessage)getObject(file, UserLoginMessage.class);
//                    case USERLOGOUT: return (UserLogoutMessage)getObject(file, UserLogoutMessage.class);
//                }
//            case ERROR :
//                return (ErrorMessage)getObject(file, ErrorMessage.class);
//            case SUCCESS :
//                for (Node child = root.getFirstChild(); child != null; child = child.getNextSibling()) {
//                    if (child.getNodeName().equals(LISTUSERS)) {
//                        return (ListUsersSuccess)getObject(file, ListUsersSuccess.class);
//                    }
//                    if (child.getNodeName().equals(SESSION)) {
//                        return (LoginSuccess)getObject(file, LoginSuccess.class);
//                    }
//                }
//        }
//        return null;
//    }
//
//    public Message deserialize(File file) {
//        try {
//            this.file = file;
//            Document doc = documentBuilder.parse(file);
//            return parse(doc);
//        } catch (SAXException | IOException e) {
//            log.error("parsing message error", e);
//        }
//        return null;
//    }
//
//    public static void main(String[] args) {
//        DOMDeserializer d = new DOMDeserializer();
//        Message m = d.deserialize(new File("/Users/Alexander/Documents/java_programming/15201-sogreshilin/lab3/message.xml"));
//        ListUsersSuccess mes = (ListUsersSuccess)m;
//        log.info("users : {}", mes.getUsers());
//    }
//
//
//}