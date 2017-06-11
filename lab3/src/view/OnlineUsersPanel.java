package view;


import model.User;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Alexander on 01/06/2017.
 */
public class OnlineUsersPanel extends JPanel {

    private JTextArea usersTA;

    public OnlineUsersPanel() {
        setLayout(new BorderLayout());
        usersTA = new JTextArea(10,13);
        usersTA.setLineWrap(true);
        usersTA.setWrapStyleWord(true);
        usersTA.setEditable(false);

        JScrollPane scroller = new JScrollPane(usersTA);
        scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        EmptyBorder emptyBorder = new EmptyBorder(6,3,6,6);
        EtchedBorder etchedBorder = new EtchedBorder();
        scroller.setBorder(new CompoundBorder(emptyBorder,etchedBorder));
        add(scroller, BorderLayout.CENTER);
    }

    public void refreshUsersList(List<User> users) {
        List<String> names = new ArrayList<>();
        for (User user : users) {
            names.add(user.getName() + " via " + user.getType());
        }
        usersTA.setText("");
        Collections.sort(names);
        for (String user : names) {
            usersTA.append(user + "\n");
        }
    }
}
