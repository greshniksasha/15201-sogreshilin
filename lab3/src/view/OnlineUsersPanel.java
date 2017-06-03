package view;


import javax.swing.*;
import java.awt.*;
import java.util.Collections;
import java.util.List;

/**
 * Created by Alexander on 01/06/2017.
 */
public class OnlineUsersPanel extends JPanel {

    private JTextArea usersTA;

    public OnlineUsersPanel() {
        usersTA = new JTextArea(15, 10);
        usersTA.setLineWrap(true);
        usersTA.setWrapStyleWord(true);
        usersTA.setEditable(false);

        JScrollPane scroller = new JScrollPane(usersTA);
        scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        add(scroller);
    }

    public void refreshUsersList(List<String> users) {
        usersTA.setText("");
        Collections.sort(users);
        for (String user : users) {
            usersTA.append(user + "\n");
        }
    }
}
