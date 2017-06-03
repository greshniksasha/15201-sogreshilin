package view;

import javax.swing.*;
import javax.swing.text.DefaultCaret;

/**
 * Created by Alexander on 01/06/2017.
 */
public class IncomingMessagesPanel extends JPanel {

    private JTextArea incomingTA;

    public IncomingMessagesPanel() {
        incomingTA = new JTextArea(15, 30);
        incomingTA.setLineWrap(true);
        incomingTA.setWrapStyleWord(true);
        incomingTA.setEditable(false);

        JScrollPane scroller = new JScrollPane(incomingTA);
        scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        DefaultCaret caret = (DefaultCaret) incomingTA.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        add(scroller);
    }

    public void appendText(String text) {
        incomingTA.append(text + "\n");
    }




}
