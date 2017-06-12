import model.Client;
import model.ClientConfigs;
import model.Server;
import model.ServerConfigs;
import model.message.LoginRequest;
import model.message.LogoutRequest;
import model.message.TextMessage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by Alexander on 11/06/2017.
 */
public class ServerTest {
    private static final short PORT = 5001;
    private static final String NAME = "test_name";
    private static final String IP = "localhost";
    private static final String TYPE = "xml";
    private Server server;

    @Before
    public void setUp() throws Exception {
        ServerConfigs configs = new ServerConfigs();
        configs.setPortXML(PORT);
        configs.setLogOn(true);
        server = new Server(configs);
        server.start();
    }

    @Test
    public void messageAttack() throws Exception {
        ClientConfigs clientConfigs = new ClientConfigs();
        clientConfigs.setPort(PORT);
        clientConfigs.setIp(IP);
        clientConfigs.setType(TYPE);
        Client client = new Client(clientConfigs);
        client.connectToServer();


        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setType(client.getType());
        loginRequest.setName(NAME);
        client.addOutgoingMessage(loginRequest);


        for (int i = 0; i < 1000; ++i) {
            TextMessage message = new TextMessage();
            message.setSessionID(client.getSessionID());
            message.setText("message-" + i);
            client.addOutgoingMessage(message);
        }


        LogoutRequest logoutRequest = new LogoutRequest();
        logoutRequest.setSessionID(client.getSessionID());
        client.addOutgoingMessage(logoutRequest);

        client.joinThreads();
    }

    @After
    public void tearDown() throws Exception {
        server.stop();
    }
}