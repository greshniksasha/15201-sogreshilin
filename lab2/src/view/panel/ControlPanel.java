package view.panel;

import model.Factory;
import view.LabeledSliderWithTextField;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class ControlPanel extends JPanel {
    private LabeledSliderWithTextField body;
    private LabeledSliderWithTextField accessory;
    private LabeledSliderWithTextField engine;
    private LabeledSliderWithTextField car;

    private static final int MIN_TIMEOUT = 0;
    private static final int MAX_TIMEOUT = 10000;
    private static final int SPACING = 500;
    private static final String TITLE = "Suppliers timeouts, sec.";
    private static final String BODY_LABEL = "Body supplier";
    private static final String ACCESSORY_LABEL = "Accessory supplier";
    private static final String ENGINE_LABEL = "Engine suplier";
    private static final String CAR_LABEL = "Car dealer";

    public ControlPanel(Factory factory) {
        setBorder(new TitledBorder(TITLE));
        setLayout(new GridLayout(4, 1));

        body = new LabeledSliderWithTextField(BODY_LABEL, MIN_TIMEOUT, MAX_TIMEOUT, SPACING);
        accessory = new LabeledSliderWithTextField(ACCESSORY_LABEL, MIN_TIMEOUT, MAX_TIMEOUT, SPACING);
        engine = new LabeledSliderWithTextField(ENGINE_LABEL, MIN_TIMEOUT, MAX_TIMEOUT, SPACING);
        car = new LabeledSliderWithTextField(CAR_LABEL, MIN_TIMEOUT, MAX_TIMEOUT, SPACING);

        body.setValue(factory.getBodySupplier().getTimeout());
        accessory.setValue(factory.getAccessorySupplier().getTimeout());
        engine.setValue(factory.getEngineSupplier().getTimeout());
        car.setValue(factory.getDealer().getTimeout());

        body.addValueChangedObserver(timeout -> factory.getBodySupplier().setTimeout(timeout));
        accessory.addValueChangedObserver(timeout -> factory.getAccessorySupplier().setTimeout(timeout));
        engine.addValueChangedObserver(timeout -> factory.getEngineSupplier().setTimeout(timeout));
        car.addValueChangedObserver(timeout -> factory.getDealer().setTimeout(timeout));

        add(body);
        add(accessory);
        add(engine);
        add(car);


    }




}
