package view;

import model.Client;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Alexander on 29/05/2017.
 */
public class ClientForm extends JFrame {
    public ClientForm(Client client) throws HeadlessException {
        super("Client");
        this.setDefaultCloseOperation(3);
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        JTextArea incoming = new JTextArea(15, 20);
        incoming.setLineWrap(true);
        incoming.setWrapStyleWord(true);
        incoming.setEditable(false);
        JScrollPane scroller = new JScrollPane(incoming);
        scroller.setVerticalScrollBarPolicy(22);
        scroller.setHorizontalScrollBarPolicy(31);
        client.addIncomingMessageListener((message) -> {
            incoming.append(message + "\n");
        });
        JPanel inputPanel = new JPanel();
        JTextField outgoing = new JTextField(20);
        JButton sendButton = new JButton("send");
        sendButton.addActionListener((e) -> {
            client.setOutcoming(outgoing.getText());
            outgoing.setText("");
        });
        mainPanel.add(scroller, "North");
        inputPanel.add(outgoing);
        inputPanel.add(sendButton);
        mainPanel.add(inputPanel, "South");
        this.setContentPane(mainPanel);
        this.pack();
        this.setResizable(false);
    }
}
