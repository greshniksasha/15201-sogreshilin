//package serializing;
//
//import model.Client;
//import model.message.*;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import org.xml.sax.Attributes;
//import org.xml.sax.SAXException;
//import org.xml.sax.helpers.DefaultHandler;
//
//import javax.xml.bind.JAXBContext;
//import javax.xml.bind.JAXBException;
//import javax.xml.bind.Unmarshaller;
//import javax.xml.parsers.ParserConfigurationException;
//import javax.xml.parsers.SAXParser;
//import javax.xml.parsers.SAXParserFactory;
//import java.io.File;
//import java.io.IOException;
//import java.util.Stack;
//
///**
// * Created by Alexander on 06/06/2017.
// */
//public class SAXDeserializer extends DefaultHandler {
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
//
//    private Message message;
//    private SAXParser parser;
//    private String currentElement;
//    private String attribute;
//    private File file;
//    private Boolean seenSuccessElement;
//
//    private static final Logger log = LogManager.getLogger(SAXDeserializer.class);
//
//    public SAXDeserializer() {
//        try {
//            SAXParserFactory factory = SAXParserFactory.newInstance();
//            parser = factory.newSAXParser();
//        } catch (SAXException e) {
//            e.printStackTrace();
//        } catch (ParserConfigurationException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public void startDocument() throws SAXException {
//        seenSuccessElement = false;
//    }
//
//    @Override
//    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
//        currentElement = qName;
//        if (currentElement.equals(COMMAND) || currentElement.equals(EVENT)) {
//            attribute = atts.getValue("name");
//        }
//        System.out.println("start element : " + currentElement);
//    }
//
//    @Override
//    public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
//        currentElement = "";
//        System.out.println("finished element : " + currentElement);
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
//    @Override
//    public void characters(char[] ch, int start, int length) throws SAXException {
//        if (currentElement.equals(COMMAND)) {
//            switch (attribute) {
//                case LOGIN :
//                    message = (LoginRequest)getObject(file, LoginRequest.class);
//                    break;
//                case LIST :
//                    message = (ListUsersRequest)getObject(file, ListUsersRequest.class);
//                    break;
//                case MESSAGE :
//                    message = (TextMessage)getObject(file, TextMessage.class);
//                    break;
//                case LOGOUT :
//                    message = (LogoutRequest)getObject(file, LogoutRequest.class);
//                    break;
//            }
//        }
//
//
//        if (currentElement.equals(EVENT)) {
//            switch (attribute) {
//                case USERLOGIN :
//                    message = (UserLoginMessage)getObject(file, UserLoginMessage.class);
//                    break;
//                case MESSAGE :
//                    message = (UserMessage)getObject(file, UserMessage.class);
//                    break;
//                case USERLOGOUT :
//                    message = (UserLogoutMessage)getObject(file, UserLogoutMessage.class);
//                    break;
//            }
//        }
//
//
//        if (currentElement.equals(ERROR)) {
//            message = (ErrorMessage)getObject(file, ErrorMessage.class);
//        }
//
//
//        if (currentElement.equals(SUCCESS)) {
//            System.out.println("add SUCCESS to stack");
//            seenSuccessElement = true;
//        }
//
//
//        if (currentElement.equals(SESSION) && seenSuccessElement) {
//            message = (LoginSuccess)getObject(file, LoginSuccess.class);
//        }
//
//
//        if (currentElement.equals(LISTUSERS) && seenSuccessElement) {
//            System.out.println("create LIST_USERS_RESPONSE");
//            message = (ListUsersSuccess)getObject(file, ListUsersSuccess.class);
//        }
//
//
////        if (currentElement.equals(SUCCESS) && stack.empty()) {
////            message =
////        }
//
//    }
//
//    @Override
//    public void endDocument() {
//        //
//    }
//
//
//    public Message getMessage() {
//        return message;
//    }
//
//    public void method(File file) {
//        try {
//            this.file = file;
//            parser.parse(file, this);
//        } catch (SAXException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    public static void main(String[] args) {
//        SAXDeserializer d = new SAXDeserializer();
//        d.method(new File("/Users/Alexander/Documents/java_programming/15201-sogreshilin/lab3/message.xml"));
//        Message m = d.getMessage();
////        ListUsersSuccess mes = (ListUsersSuccess)m;
//        ListUsersRequest mes = (ListUsersRequest)m;
////        log.info("users : {}", mes.getUsers());
//        log.info("session : {}", mes.getSessionID());
//    }
//
//}
