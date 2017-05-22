package view;

import org.apache.log4j.Logger;

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
    private JLabel label;
    private JSlider slider;
    private JTextField textField;

    List<ValueChangedObserver> observers = new ArrayList<>();
    private static final Logger log = Logger.getLogger(LabeledSliderWithTextField.class);


    public LabeledSliderWithTextField(String name, int min, int max, int spacing) {
        setLayout(new GridLayout(1,3));
        label = new JLabel(name);
        slider = new JSlider(min, max);
        slider.setMajorTickSpacing(spacing);
        slider.setPaintTicks(true);
        slider.setSnapToTicks(false);
        textField = new JTextField();
        add(label);
        add(slider);
        add(textField);
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
                log.error("Invalid data entered to \"" + label.getText() + "\" TextField");
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


}
