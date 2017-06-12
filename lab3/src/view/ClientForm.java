package view;

import model.client.Client;
import model.client.MessageHandler;
import model.message.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Created by Alexander on 29/05/2017.
 */
public class ClientForm extends JFrame implements MessageHandler {
    private Client client;
    private JLabel loginL;
    private JTextField loginTF;
    private JButton connectB;
    private JButton disconnectB;
    private JTextArea outgoingTF;
    private JButton sendB;
    private JTextArea incomingTA;
    private OnlineUsersPanel usersP;
    private static final String CONNECT = "CONNECT";
    private static final String DISCONNECT = "DISCONNECT";
    private static final String SEND = "SEND";

    private static final Logger log = LogManager.getLogger(ClientForm.class);

    public ClientForm(Client client) throws HeadlessException {
        super("Chat");
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.client = client;
        usersP = new OnlineUsersPanel();

        client.addObserver((message -> {
            message.process(this);
        }));

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(setNorthPanel(), BorderLayout.NORTH);
        mainPanel.add(setCenterPanel(), BorderLayout.CENTER);
        mainPanel.add(usersP, BorderLayout.EAST);
        mainPanel.add(setSouthPanel(), BorderLayout.SOUTH);
        this.setContentPane(mainPanel);
        this.pack();
        this.setMinimumSize(new Dimension(450, 300));
    }

    private JScrollPane setCenterPanel() {
        incomingTA = new JTextArea();
        incomingTA.setLineWrap(true);
        incomingTA.setWrapStyleWord(true);
        incomingTA.setEditable(false);
        JScrollPane scroller = new JScrollPane(incomingTA);
        scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        EmptyBorder emptyBorder = new EmptyBorder(6,6,6,3);
        EtchedBorder etchedBorder = new EtchedBorder();
        scroller.setBorder(new CompoundBorder(emptyBorder,etchedBorder));
        DefaultCaret caret = (DefaultCaret) incomingTA.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        return scroller;
    }

    private JPanel setNorthPanel() {
        JPanel northP = new JPanel(new GridLayout(1,4));
        loginL = new JLabel("Enter username");
        loginL.setHorizontalAlignment(SwingConstants.CENTER);
        loginTF = new JTextField();
        connectB = new JButton(CONNECT);
        disconnectB = new JButton(DISCONNECT);
        northP.add(loginL);
        northP.add(loginTF);
        northP.add(connectB);
        northP.add(disconnectB);

        client.setConnectionObserver(connected -> {
            if (!connected) {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(this,
                            "Server connecting error",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);

                    loginTF.setEditable(true);
                    loginTF.requestFocus();
                    getRootPane().setDefaultButton(connectB);
                    connectB.setEnabled(false);
                    disconnectB.setEnabled(false);
                    outgoingTF.setEnabled(false);
                });
            }
        });

        connectB.addActionListener((e) -> {
            if (!this.client.isConnectedToServer()) {
                this.client.connectToServer();
            }
            String name = loginTF.getText();
            client.setName(name);
            LoginRequest msg = new LoginRequest();
            msg.setName(name);
            msg.setType(client.getType());
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
        return northP;
    }

    private JPanel setSouthPanel() {
        JPanel southP = new JPanel(new BorderLayout());
        sendB = new JButton(SEND);
        outgoingTF = new JTextArea(4,10);
        outgoingTF.setLineWrap(true);
        outgoingTF.setWrapStyleWord(true);
        southP.add(sendB, BorderLayout.EAST);

        JScrollPane scroller = new JScrollPane(outgoingTF);
        scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        southP.add(scroller, BorderLayout.CENTER);

        sendB.setEnabled(false);

        sendB.addActionListener((e) -> {
            String text = outgoingTF.getText();
            text = text.replaceAll("^\\s+|\\s+$", "");
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

        KeyStroke shiftEnter = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, KeyEvent.SHIFT_DOWN_MASK, false);
        KeyStroke enter = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, false);

        outgoingTF.getInputMap().put(shiftEnter, "shift+enter");
        outgoingTF.getInputMap().put(enter, "enter");

        outgoingTF.getActionMap().put("shift+enter", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                outgoingTF.append("\n");
            }
        });
        outgoingTF.getActionMap().put("enter", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendB.doClick();
            }
        });

        outgoingTF.setEditable(false);
        southP.setBorder(new EmptyBorder(0,6,3,6));
        return southP;
    }

    public void process(LoginSuccess message) {
        clear();
        outgoingTF.setEditable(true);
        loginTF.setText(client.getName());
        loginTF.setEditable(false);
        outgoingTF.requestFocus();
        loginL.setText("Logged in as");
        getRootPane().setDefaultButton(sendB);
        disconnectB.setEnabled(true);
        connectB.setEnabled(false);
        log.info("form is ready to exchange messages");
    }

    public void process(LoginError message) {
        JOptionPane.showMessageDialog(this, message.getError());
        log.info("user informed about login error");
    }

    public void process(ListUsersSuccess message) {
        usersP.refreshUsersList(client.getUsers());
        log.info("refreshed online users");
    }

    public void process(ListUsersError message) {
        log.info("online users cannot be displayed");
    }

    public void process(TextSuccess message) {
        appendText( client.getName() + ": " + client.takeTextMessage().getText());
        log.info("message displayed");
    }

    public void process(TextError message) {
        appendText("your message was not delivered : \"" + message.getText() + "\"");
        log.info("message marked as undelivered");
    }

    public void process(LogoutSuccess message) {
        loginL.setText("Last logged in as");
        usersP.refreshUsersList(client.getUsers());
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
        appendMessage(message);
        log.info("user [{}] added to userList", message.getName());
    }

    public void process(UserLogoutMessage message) {
        usersP.refreshUsersList(client.getUsers());
        appendMessage(message);
        log.info("user [{}] removed from userList", message.getName());
    }

    public void process(UserMessage message) {
        appendMessage(message);
        log.info("message \"{}\" from {} displayed", message.getMessage(), message.getName());
    }

    private void appendText(String text) {
        incomingTA.append(text + "\n");
    }

    private void clear() {
        incomingTA.setText("");
    }

    public void appendMessage(DisplayMessage message) {
        appendText(message.getName() + ": " + message.messageToShow());
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
