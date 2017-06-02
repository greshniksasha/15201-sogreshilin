package view;

import model.ClientReader;

import javax.swing.*;
import javax.swing.text.DefaultCaret;

/**
 * Created by Alexander on 01/06/2017.
 */
public class IncomingMessagesPanel extends JPanel {
    public IncomingMessagesPanel(ClientReader clientReader) {
        JTextArea incoming = new JTextArea(15, 20);
        incoming.setLineWrap(true);
        incoming.setWrapStyleWord(true);
        incoming.setEditable(false);
        JScrollPane scroller = new JScrollPane(incoming);
        scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        DefaultCaret caret = (DefaultCaret)incoming.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        clientReader.addIncomingMessageListener((message) -> {
            switch (message.getType()) {
                case JOINED:
                    incoming.append(message.getContent() + " joined chat\n\n");
                    break;
                case LEFT:
                    incoming.append(message.getContent() + " left chat\n\n");
                    break;
                case REGULAR:
                    incoming.append(message.getSender() + ":\n");
                    incoming.append(message.getContent() + "\n\n");
                    break;
            }
        });

        add(scroller);
    }




}
