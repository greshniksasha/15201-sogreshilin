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

    private final int MIN_TIMEOUT = 0;
    private final int MAX_TIMEOUT = 10000;
    private final int SPACING = 500;
    private final String TITLE = "Suppliers";
    private final String BODY_LABEL = "Body supplier";
    private final String ENGINE_LABEL = "Engine suplier";
    private final String ACCESSORY_LABEL;
    private final String CAR_LABEL;

    public ControlPanel(Factory factory) {
        setBorder(new TitledBorder(TITLE));
        setLayout(new GridLayout(4, 1));

        int supplierCount = factory.getAccessorySupplierCount();
        int dealerCount = factory.getDealerCount();
        this.ACCESSORY_LABEL = "Accessory supplier" + (supplierCount > 1 ? "s, " + supplierCount : "");
        this.CAR_LABEL = "Car dealer" + (dealerCount > 1 ? "s, " + dealerCount : "");


        body = new LabeledSliderWithTextField(BODY_LABEL, MIN_TIMEOUT, MAX_TIMEOUT, SPACING);
        accessory = new LabeledSliderWithTextField(ACCESSORY_LABEL, MIN_TIMEOUT, MAX_TIMEOUT, SPACING);
        engine = new LabeledSliderWithTextField(ENGINE_LABEL, MIN_TIMEOUT, MAX_TIMEOUT, SPACING);
        car = new LabeledSliderWithTextField(CAR_LABEL, MIN_TIMEOUT, MAX_TIMEOUT, SPACING);

        body.setValue(factory.getBodySupplier().getTimeout());
        accessory.setValue(factory.getAccessorySupplier().getTimeout());
        engine.setValue(factory.getEngineSupplier().getTimeout());
        car.setValue(factory.getDealer().getTimeout());

        body.setTransactionCounterObserver(factory.getBodySupplier());
        accessory.setTransactionCounterObserver(factory.getAccessorySupplier());
        engine.setTransactionCounterObserver(factory.getEngineSupplier());
        car.setTransactionCounterObserver(factory.getDealer());

        body.addValueChangedObserver(timeout -> factory.getBodySupplier().setTimeout(timeout));
        accessory.addValueChangedObserver(timeout -> factory.getAccessorySupplier().setTimeout(timeout));
        engine.addValueChangedObserver(timeout -> factory.getEngineSupplier().setTimeout(timeout));
        car.addValueChangedObserver(timeout -> factory.getDealer().setTimeout(timeout));

        add(accessory);
        add(engine);
        add(body);
        add(car);
    }
}
