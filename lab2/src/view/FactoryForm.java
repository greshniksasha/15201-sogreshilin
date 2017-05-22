package view;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Alexander on 22/05/2017.
 */
public class FactoryForm extends JFrame {
//    private JPanel panel;
    private JPanel factoryRootPanel;

    public FactoryForm() throws HeadlessException {
        super("Car Factory");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//        JPanel panel = new JPanel();
//        panel.setLayout(new BorderLayout());
        setContentPane(factoryRootPanel);


    }
}
