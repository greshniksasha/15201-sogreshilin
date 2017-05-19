import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexander on 18/05/2017.
 */
public class SliderWithTextfield {
    List<ValueChangedHandler> handlers = new ArrayList<>();
    JTextField textField;


    public SliderWithTextfield() {
        textField.addActionListener((e) -> {
            String text = textField.getText();
            int value = Integer.parseInt(text);
            notifyValueChanged(value);
        });
    }

    public void addHandler(ValueChangedHandler h) {
        handlers.add(h);
    }

    public void removeHandler(ValueChangedHandler h) {
        handlers.remove(h);
    }

    private void notifyValueChanged(int value) {
        for (ValueChangedHandler h : handlers) {
            if (h != null) {
                h.handle(value);
            }
        }
    }
}
