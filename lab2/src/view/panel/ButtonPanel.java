package view.panel;


import model.Factory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ButtonPanel extends JPanel {

    private static final Logger log = LogManager.getLogger(ButtonPanel.class);

    public ButtonPanel(Factory factory) {

        JButton startButton = new JButton("start");
        JButton stopButton = new JButton("stop");

        startButton.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (startButton.isEnabled()) {
                    log.info("start button pressed, starting all threads in factory");
                    startButton.setEnabled(false);
                    factory.start();
                }
            }
        });

        stopButton.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                log.info("stop button pressed, stopping");
                stopButton.setEnabled(false);
                factory.finish();
                System.exit(0);
            }
        });

        setLayout(new GridLayout(1,2));

        add(startButton);
        add(stopButton);

    }
}
