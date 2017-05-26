package view;

import model.Factory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import view.panel.AssemblyPanel;
import view.panel.ButtonPanel;
import view.panel.ControlPanel;
import view.panel.WarehousePanel;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

/**
 * Created by Alexander on 22/05/2017.
 */
public class FactoryForm extends JFrame {
    private Factory factory;

    private static final Logger log = LogManager.getLogger(ButtonPanel.class);

    public FactoryForm(Factory factory) throws HeadlessException {
        super("Car Factory");
        this.factory = factory;

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(new ControlPanel(factory), BorderLayout.NORTH);
        mainPanel.add(new AssemblyPanel(factory), BorderLayout.WEST);
        mainPanel.add(new WarehousePanel(factory), BorderLayout.CENTER);
        mainPanel.add(new ButtonPanel(factory), BorderLayout.SOUTH);

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setContentPane(mainPanel);
        pack();
        this.setResizable(false);
    }

    public void dispose() {
        factory.finish();
        super.dispose();
        log.info("exiting: stopping all threads in factory");
        System.exit(0);
    }
}
