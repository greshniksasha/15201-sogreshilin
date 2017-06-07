package view;

import model.Client;
import model.message.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Created by Alexander on 29/05/2017.
 */
public class ClientForm extends JFrame {
    private Client client;
    private static final Logger log = LogManager.getLogger(Client.class);


    private JLabel loginL;
    private JTextField loginTF;
    private JButton connectB;
    private JButton disconnectB;
    private JTextField outgoingTF;
    private JButton sendB;
    private IncomingMessagesPanel incomingP;
    private OnlineUsersPanel usersP;


    public ClientForm(Client client) throws HeadlessException {
        super("Client");
        this.client = client;
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        client.addObserver((message -> {
            message.process(this);
        }));

        //***** NORTH PANEL *****/

        JPanel northP = new JPanel(new GridLayout(1,4));
        loginL = new JLabel("Enter username");
        loginTF = new JTextField();
        connectB = new JButton("CONNECT");
        disconnectB = new JButton("DISCONNECT");
        northP.add(loginL);
        northP.add(loginTF);
        northP.add(connectB);
        northP.add(disconnectB);

        loginL.setHorizontalAlignment(SwingConstants.CENTER);

        connectB.addActionListener((e) -> {
            if (!this.client.isConnectedToServer()) {
                this.client.connectToServer();
            }
            String name = loginTF.getText();
            client.setName(name);
            LoginRequest msg = new LoginRequest();
            msg.setName(name);
            msg.setChatClientName(client.getChatClientName());
            client.addOutgoingMessage(msg);
            log.info("trying to log in as {}", name);
        });

        connectB.setEnabled(false);
        getRootPane().setDefaultButton(connectB);

        disconnectB.addActionListener((e) -> {
            LogoutRequest msg = new LogoutRequest();
            msg.setSessionID(client.getSessionID());
            client.addOutgoingMessage(msg);
            sendB.setEnabled(false);
            disconnectB.setEnabled(false);
            outgoingTF.setEditable(false);
            log.info("trying to disconnect");
        });

        disconnectB.setEnabled(false);

        loginTF.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                if (loginTF.isEditable()) {
                    if (loginTF.getText().trim().isEmpty()) {
                        connectB.setEnabled(false);
                    } else {
                        connectB.setEnabled(true);
                    }
                }
            }
        });

        //***** SOUTH PANEL *****/

        JPanel southP = new JPanel();
        sendB = new JButton("SEND");
        outgoingTF = new JTextField(20);
        southP.add(outgoingTF);
        southP.add(sendB);

        sendB.setEnabled(false);

        sendB.addActionListener((e) -> {
            String text = outgoingTF.getText();
            outgoingTF.setText("");
            TextMessage msg = new TextMessage();
            msg.setText(text);
            msg.setSessionID(client.getSessionID());
            client.addOutgoingMessage(msg);
        });

        outgoingTF.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                if (disconnectB.isEnabled()) {
                    if (outgoingTF.getText().trim().isEmpty()) {
                        sendB.setEnabled(false);
                    } else {
                        sendB.setEnabled(true);
                    }
                }
            }
        });

        outgoingTF.setEditable(false);

        //***** CENTER PANEL *****/

        incomingP = new IncomingMessagesPanel();
        usersP = new OnlineUsersPanel();

        //***** ALL FORM *****/

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(northP, BorderLayout.NORTH);
        mainPanel.add(incomingP, BorderLayout.WEST);
        mainPanel.add(usersP, BorderLayout.EAST);
        mainPanel.add(southP, BorderLayout.SOUTH);
        this.setContentPane(mainPanel);
        this.pack();
        this.setResizable(false);
    }









    public void process(LoginSuccess message) {
        client.setSessionID(message.getSessionID());
        outgoingTF.setEditable(true);
        loginTF.setText(client.getName());
        loginTF.setEditable(false);
        outgoingTF.requestFocus();
        loginL.setText("Logged in as");
        getRootPane().setDefaultButton(sendB);
        disconnectB.setEnabled(true);
        connectB.setEnabled(false);
        log.info("view is ready to exchange messages");
    }

    public void process(LoginError message) {
        JOptionPane.showMessageDialog(this, message.getError());
        log.info("view informed user about error");
    }

    public void process(ListUsersSuccess message) {
        usersP.refreshUsersList(client.getUsers());
        log.info("view displayed online users");
    }

    public void process(ListUsersError message) {
        log.info("view could not display online users");
    }

    public void process(TextSuccess message) {
        log.info("message displayed");
    }

    public void process(TextError message) {
        incomingP.appendText("your message was not delivered : \"" + message.getText() + "\"");
        log.info("message marked as undelivered");
    }

    public void process(LogoutSuccess message) {
        //TODO
        loginL.setText("Last logged in as");
        usersP.refreshUsersList(client.getUsers());
        incomingP.appendText("you are disconnected from chat");
        loginTF.setEditable(true);
        loginTF.requestFocus();
        getRootPane().setDefaultButton(connectB);
        connectB.setEnabled(true);
        client.disconnectFromServer();
        log.info("disconnected from the chat");
    }

    public void process(LogoutError message) {
        disconnectB.setEnabled(true);
        JOptionPane.showMessageDialog(this, message.getError());
        sendB.setEnabled(true);
        outgoingTF.setEnabled(true);
        log.info("logout failed");
    }

    public void process(UserLoginMessage message) {
        usersP.refreshUsersList(client.getUsers());
        incomingP.appendText(message.messageToShow());
        log.info("user {} added to userList", message.getName());
    }

    public void process(UserLogoutMessage message) {
        usersP.refreshUsersList(client.getUsers());
        incomingP.appendText(message.messageToShow());
        log.info("user {} removed from userList", message.getName());
    }

    public void process(UserMessage message) {
        incomingP.appendText(message.messageToShow());
        log.info("displayed message \"{}\" from {}", message.getMessage(), message.getName());
    }


    public void dispose() {
        if (disconnectB.isEnabled()) {
            disconnectB.doClick();
        }
        super.dispose();
        log.info("exiting");
        System.exit(0);
    }







}
