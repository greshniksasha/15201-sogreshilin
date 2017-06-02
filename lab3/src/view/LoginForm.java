package view;

import model.ClientReader;
import model.MessageType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Created by Alexander on 30/05/2017.
 */
public class LoginForm extends JFrame {
    private static final String INVITE_MSG = "Enter your chat name";
    private static final String CONFIRM_BUTTON_MSG = "Join";
    private ClientReader.IncomingMessageObserver listener;
    private static final Logger log = LogManager.getLogger(LoginForm.class);
    private ClientReader clientReader;

    public LoginForm(ClientReader clientReader) throws HeadlessException {
        super("Login");
        this.clientReader = clientReader;

        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        JPanel mainPanel = new JPanel(new BorderLayout());

        JLabel label = new JLabel(INVITE_MSG);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        JTextField textField = new JTextField(20);
        JButton confirmButton = new JButton(CONFIRM_BUTTON_MSG);

        listener = message -> {
            if (message.getType() == MessageType.DENY) {
                Toolkit.getDefaultToolkit().beep();
                label.setText("This name is used, try another one");
            }
        };

        clientReader.addIncomingMessageListener(listener);

        confirmButton.addActionListener((e) -> {
            String nickname = textField.getText();
            clientReader.getClient().addOutgoingMessage(MessageType.LOGIN, nickname);
            log.info("nickname {} added to message queue", nickname);
        });

        confirmButton.setEnabled(false);
        textField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                if (textField.getText().trim().isEmpty()) {
                    confirmButton.setEnabled(false);
                } else {
                    confirmButton.setEnabled(true);
                }
            }
        });

        getRootPane().setDefaultButton(confirmButton);


        mainPanel.add(label, BorderLayout.NORTH);
        mainPanel.add(textField, BorderLayout.CENTER);
        mainPanel.add(confirmButton, BorderLayout.SOUTH);

        this.setContentPane(mainPanel);
        this.pack();
        this.setResizable(false);
    }

    public void dispose() {
        clientReader.getClient().addOutgoingMessage(MessageType.LOGOUT, null);
        super.dispose();
        log.info("exiting");
        System.exit(0);

    }

    public void startChatForm() {
        clientReader.removeIncomingMessageListener(listener);
        super.dispose();
        ClientForm form = new ClientForm(clientReader);
        form.setVisible(true);
    }


}
