package view;

import model.ClientReader;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Alexander on 01/06/2017.
 */
public class ParticipantsPanel extends JPanel {
    public ParticipantsPanel(ClientReader clientReader) {
        setLayout(new BorderLayout());
        JTextArea participants = new JTextArea(15, 10);
        participants.setLineWrap(true);
        participants.setWrapStyleWord(true);
        participants.setEditable(false);
        JScrollPane participantsScroller = new JScrollPane(participants);
        participantsScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        participantsScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        clientReader.addParticipantsObserver((list) -> {
            participants.setText("");
            for (String participant : list) {
                participants.append(participant + "\n");
            }
        });
        add(participantsScroller, BorderLayout.CENTER);
    }
}
