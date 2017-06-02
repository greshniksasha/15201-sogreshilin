package view;

import model.Client;
import model.ClientReader;
import model.MessageType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by Alexander on 29/05/2017.
 */
public class ClientForm extends JFrame {
    private ClientReader clientReader;
    private static final Logger log = LogManager.getLogger(Client.class);

    public ClientForm(ClientReader clientReader) throws HeadlessException {
        super("Client");
        this.clientReader = clientReader;
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JTextField outgoing = new JTextField(20);
        JButton sendButton = new JButton("send");
        JPanel inputPanel = new JPanel();
        inputPanel.add(outgoing);
        inputPanel.add(sendButton);

        sendButton.addActionListener((e) -> {
            clientReader.getClient().addOutgoingMessage(MessageType.REGULAR, outgoing.getText());
            outgoing.setText("");
        });
        getRootPane().setDefaultButton(sendButton);

        JButton participantsButton = new JButton("participants");
        participantsButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                clientReader.getClient().addOutgoingMessage(MessageType.PARTICIPANTS, "");
            }
        });

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(new IncomingMessagesPanel(clientReader), BorderLayout.CENTER);
        mainPanel.add(new ParticipantsPanel(clientReader), BorderLayout.WEST);
        mainPanel.add(inputPanel, BorderLayout.SOUTH);

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
}
