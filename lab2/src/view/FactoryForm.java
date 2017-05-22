package view;

import model.Factory;
import view.panel.ButtonPanel;
import view.panel.ControlPanel;
import view.panel.InformationPanel;
import view.panel.TitlesPanel;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Alexander on 22/05/2017.
 */
public class FactoryForm extends JFrame {
    private JPanel mainPanel;
    private JPanel controlPanel;
    private JPanel informationPanel;
    private JPanel buttonPanel;
    private JPanel tableTitles;
    private Factory factory;

    public FactoryForm(Factory factory) throws HeadlessException {
        super("Car Factory");
        this.factory = factory;
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setContentPane(mainPanel);
        pack();
        this.setResizable(false);
    }

    private void createUIComponents() {
        controlPanel = new ControlPanel(factory);
        informationPanel = new InformationPanel(factory);
        buttonPanel = new ButtonPanel(factory);
    }
}
