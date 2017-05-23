package view.panel;

import model.Assembly;
import model.Factory;
import view.InformationLabels;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class InformationPanel extends JPanel {

    private InformationLabels car;
    private InformationLabels body;
    private InformationLabels engine;
    private InformationLabels accessory;

    public InformationPanel(Factory factory) {
        setBorder(new TitledBorder("Warehouses"));
        setLayout(new GridLayout(5,1));

        car = new InformationLabels("Car");
        body = new InformationLabels("Body");
        engine = new InformationLabels("Engine");
        accessory = new InformationLabels("Accessory");

        Assembly assembly = factory.getAssembly();
        car.setSizeObserver(assembly.getCarWarehouse());
        body.setSizeObserver(assembly.getBodyWarehouse());
        engine.setSizeObserver(assembly.getEngineWarehouse());
        accessory.setSizeObserver(assembly.getAccessoryWarehouse());

        car.setTransactionCounterObserver(factory.getDealer());
        body.setTransactionCounterObserver(factory.getBodySupplier());
        engine.setTransactionCounterObserver(factory.getEngineSupplier());
        accessory.setTransactionCounterObserver(factory.getAccessorySupplier());

        car.setWarehouseCapacity(assembly.getCarWarehouse());
        body.setWarehouseCapacity(assembly.getBodyWarehouse());
        engine.setWarehouseCapacity(assembly.getEngineWarehouse());
        accessory.setWarehouseCapacity(assembly.getAccessoryWarehouse());

        add(new TitlesPanel());
        add(accessory);
        add(engine);
        add(body);
        add(car);
    }
}
