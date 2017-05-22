package view.panel;


import model.Factory;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ButtonPanel extends JPanel {

    private static final Logger log = Logger.getLogger(ButtonPanel.class);

    public ButtonPanel(Factory factory) {

        JButton startButton = new JButton("start");
        JButton finishButton = new JButton("finish");

        startButton.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                log.trace("Start button clicked, start all threads in factory");
                factory.start();
                startButton.setEnabled(false);
            }
        });

        finishButton.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                log.trace("Finish button clicked, interrupt all threads in factory");
                factory.finish();
            }
        });

        setLayout(new GridLayout(1,2));

        add(startButton);
        add(finishButton);
    }
}
