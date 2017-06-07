import model.User;
import model.message.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import serializing.DOMDeserializer;
import serializing.JAXBSerializer;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by Alexander on 03/06/2017.
 */
public class XMLSerializingTest {

    private static JAXBSerializer serializer;
    private static File tmpFile;
    private static DataInputStream in;
    private static DataOutputStream out;
    private static DOMDeserializer deserializer;
    private static BlockingQueue<Class> queue;

    private static final Integer ID = 1001000111;
    private static final String NAME = "alexander";
    private static final String TYPE = "xml";
    private static final String TEXT = "this is TEXT message";
    private static final String ERROR_MSG = "this is error message";
    private static final List<User> USERS = new ArrayList<>();
    private static final int USER_COUNT = 10;

    static {
        try {
            serializer = new JAXBSerializer();
            tmpFile = File.createTempFile("test", "txt");
            tmpFile.deleteOnExit();
            in = new DataInputStream(new FileInputStream(tmpFile));
            out = new DataOutputStream(new FileOutputStream(tmpFile));
            queue = new ArrayBlockingQueue<>(10);
            deserializer = new DOMDeserializer();
            deserializer.setQueue(queue);
            for (int i = 0; i < USER_COUNT; ++i) {
                User user = new User();
                user.setName("name-" + i);
                user.setType(TYPE);
                USERS.add(user);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Message sendAndReceiveMessage(Message sentMessage) throws Exception {
        // serializing message
        byte[] data = serializer.messageToXMLString(sentMessage).getBytes(StandardCharsets.UTF_8);

        // writing message
        out.writeInt(data.length);
        out.write(data);

        // reading message
        int len = in.readInt();
        byte[] inputData = new byte[len];
        if (in.read(inputData, 0, len) != len) {
            throw new Exception("less bytes read than supposed to");
        }
        String string = new String(inputData, StandardCharsets.UTF_8);
        System.out.println(string);

        // deserializing message
        return deserializer.deserialize(string);
    }

    @Test
    public void serializeLoginRequest() throws Exception {
        // creating message
        LoginRequest sentMessage = new LoginRequest();
        sentMessage .setName(NAME);
        sentMessage.setType(TYPE);

        LoginRequest receivedMessage = (LoginRequest) sendAndReceiveMessage(sentMessage);

        // comparing results
        Assert.assertEquals(sentMessage.getName(), receivedMessage.getName());
        Assert.assertEquals(sentMessage.getType(), receivedMessage.getType());
    }

    @Test
    public void serializeLoginSuccess() throws Exception {
        LoginSuccess sentMessage = new LoginSuccess();
        sentMessage.setSessionID(ID);
        queue.add(LoginRequest.class);
        LoginSuccess receivedMessage = (LoginSuccess) sendAndReceiveMessage(sentMessage);
        Assert.assertEquals(sentMessage.getSessionID(), receivedMessage.getSessionID());
    }

    @Test
    public void serializeLoginError() throws Exception {
        LoginError sentMessage = new LoginError();
        sentMessage.setError(ERROR_MSG);
        queue.add(LoginRequest.class);
        LoginError receivedMessage = (LoginError) sendAndReceiveMessage(sentMessage);
        Assert.assertEquals(sentMessage.getError(), receivedMessage.getError());
    }

    @Test
    public void serializeListUsersRequest() throws Exception {
        ListUsersRequest sentMessage = new ListUsersRequest();
        sentMessage.setSessionID(ID);
        ListUsersRequest receivedMessage = (ListUsersRequest) sendAndReceiveMessage(sentMessage);
        Assert.assertEquals(sentMessage.getSessionID(), receivedMessage.getSessionID());
    }

    @Test
    public void serializeListUsersSuccess() throws Exception {
        ListUsersSuccess sentMessage = new ListUsersSuccess();
        sentMessage.setUsers(USERS);
        queue.add(ListUsersRequest.class);
        ListUsersSuccess receivedMessage = (ListUsersSuccess) sendAndReceiveMessage(sentMessage);
        Assert.assertEquals(sentMessage.getUsers(), receivedMessage.getUsers());
    }

    @Test
    public void serializeListUsersError() throws Exception {
        ListUsersError sentMessage = new ListUsersError();
        sentMessage.setError(ERROR_MSG);
        queue.add(ListUsersRequest.class);
        ListUsersError receivedMessage = (ListUsersError) sendAndReceiveMessage(sentMessage);
        Assert.assertEquals(sentMessage.getError(), receivedMessage.getError());
    }

    @Test
    public void serializeTextMessage() throws Exception {
        TextMessage sentMessage = new TextMessage();
        sentMessage.setSessionID(ID);
        sentMessage.setText(TEXT);
        TextMessage receivedMessage = (TextMessage) sendAndReceiveMessage(sentMessage);
        Assert.assertEquals(sentMessage.getSessionID(), receivedMessage.getSessionID());
        Assert.assertEquals(sentMessage.getText(), receivedMessage.getText());
    }

    @Test
    public void serializeTextSuccess() throws Exception {
        TextSuccess sentMessage = new TextSuccess();
        queue.add(TextMessage.class);
        TextSuccess receivedMessage = (TextSuccess) sendAndReceiveMessage(sentMessage);
        Assert.assertNotNull(receivedMessage);
    }

    @Test
    public void serializeTextError() throws Exception {
        TextError sentMessage = new TextError();
        sentMessage.setError(ERROR_MSG);
        queue.add(TextMessage.class);
        TextError receivedMessage = (TextError) sendAndReceiveMessage(sentMessage);
        Assert.assertEquals(sentMessage.getError(), receivedMessage.getError());
    }

    @Test
    public void serializeLogoutRequest() throws Exception {
        LogoutRequest sentMessage = new LogoutRequest();
        sentMessage.setSessionID(ID);
        LogoutRequest receivedMessage = (LogoutRequest) sendAndReceiveMessage(sentMessage);
        Assert.assertEquals(sentMessage.getSessionID(), receivedMessage.getSessionID());
    }

    @Test
    public void serializeLogoutSuccess() throws Exception {
        LogoutSuccess sentMessage = new LogoutSuccess();
        queue.add(LogoutRequest.class);
        LogoutSuccess receivedMessage = (LogoutSuccess) sendAndReceiveMessage(sentMessage);
        Assert.assertNotNull(receivedMessage);
    }

    @Test
    public void serializeLogoutError() throws Exception {
        LogoutError sentMessage = new LogoutError();
        sentMessage.setError(ERROR_MSG);
        queue.add(LogoutRequest.class);
        LogoutError receivedMessage = (LogoutError) sendAndReceiveMessage(sentMessage);
        Assert.assertEquals(sentMessage.getError(), receivedMessage.getError());
    }

    @Test
    public void serializeUserMessage() throws Exception {
        UserMessage sentMessage = new UserMessage();
        sentMessage.setMessage(TEXT);
        sentMessage.setName(NAME);
        UserMessage receivedMessage = (UserMessage) sendAndReceiveMessage(sentMessage);
        Assert.assertEquals(sentMessage.getName(), receivedMessage.getName());
        Assert.assertEquals(sentMessage.getMessage(), receivedMessage.getMessage());
    }

    @Test
    public void serializeUserLoginMessage() throws Exception {
        UserLoginMessage sentMessage = new UserLoginMessage();
        sentMessage.setName(NAME);
        sentMessage.setType(TYPE);
        UserLoginMessage receivedMessage = (UserLoginMessage) sendAndReceiveMessage(sentMessage);
        Assert.assertEquals(sentMessage.getName(), receivedMessage.getName());
    }

    @Test
    public void serializeUserLogoutMessage() throws Exception {
        UserLogoutMessage sentMessage = new UserLogoutMessage();
        sentMessage.setName(NAME);
        UserLogoutMessage receivedMessage = (UserLogoutMessage) sendAndReceiveMessage(sentMessage);
        Assert.assertEquals(sentMessage.getName(), receivedMessage.getName());
    }
}