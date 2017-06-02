package view;

import model.contractor.Contractor;
import model.contractor.TransactionCounterObserver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class LabeledSliderWithTextField extends JPanel {
    private JLabel name;
    private JSlider slider;
    private JTextField textField;
    private JLabel transactionsLabel;

    private List<ValueChangedObserver> observers = new ArrayList<>();
    private static final Logger log = LogManager.getLogger(LabeledSliderWithTextField.class);


    public LabeledSliderWithTextField(String name, int min, int max, int spacing) {
        setLayout(new FlowLayout(0,0,0));
        this.name = new JLabel(name);
        this.name.setPreferredSize(new Dimension(200, 20));
        slider = new JSlider(min, max);
        slider.setMajorTickSpacing(spacing);
        slider.setPaintTicks(true);
        slider.setSnapToTicks(false);
        slider.setFocusable(false);
        textField = new JTextField(5);
//        textField.setPreferredSize(new Dimension(10,2));
        transactionsLabel = new JLabel("0");
        transactionsLabel.setHorizontalAlignment(SwingConstants.CENTER);
        transactionsLabel.setPreferredSize(new Dimension(80, 20));
        add(this.name);
        add(slider);
        add(textField);
        add(transactionsLabel);
        setBorder(new EtchedBorder());

        slider.addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent e) {
                int value = slider.getValue();
                notifyValueChanged(value);
                textField.setText(String.valueOf(value));
            }
        });

        textField.addActionListener(a -> {
            String text = textField.getText();
            try {
                int value = Integer.parseInt(text);
                if (min <= value && value <= max) {
                    notifyValueChanged(value);
                    textField.setText(String.valueOf(value));
                    textField.setForeground(Color.black);
                    slider.setValue(value);
                } else {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                textField.setForeground(Color.red);
                log.error("Invalid data entered to \"" + this.name.getText() + "\" TextField");
            }
        });

        textField.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                if (textField.getText().isEmpty()) {
                    textField.setForeground(Color.black);
                }
            }
        });
    }

    public void addValueChangedObserver(ValueChangedObserver o) {
        observers.add(o);
    }

    private void notifyValueChanged(int value) {
        for (ValueChangedObserver o : observers) {
            if (o != null) {
                o.setValue(value);
            }
        }
    }

    public void setValue(int value) {
        textField.setText(String.valueOf(value));
        slider.setValue(value);
    }

    public interface ValueChangedObserver {
        void setValue(int value);
    }

    public void setTransactionCounterObserver(Contractor contractor) {
        contractor.addTransactionCounterObserver((count) -> transactionsLabel.setText(String.valueOf(count)));
    }

}
