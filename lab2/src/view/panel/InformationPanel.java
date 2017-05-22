package view.panel;

import model.Assembly;
import model.Factory;
import view.InformationLabels;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class InformationPanel extends JPanel {

    private InformationLabels body;
    private InformationLabels accessory;
    private InformationLabels engine;
    private InformationLabels car;

    public InformationPanel(Factory factory) {
        setBorder(new TitledBorder("Warehouses"));
        setLayout(new GridLayout(5,1));

        body = new InformationLabels("Body");
        accessory = new InformationLabels("Accessory");
        engine = new InformationLabels("Engine");
        car = new InformationLabels("Car");

        Assembly assembly = factory.getAssembly();
        body.setSizeObserver(assembly.getBodyWarehouse());
        accessory.setSizeObserver(assembly.getAccessoryWarehouse());
        engine.setSizeObserver(assembly.getEngineWarehouse());
        car.setSizeObserver(assembly.getCarWarehouse());

        body.setTransactionCounterObserver(factory.getBodySupplier());
        accessory.setTransactionCounterObserver(factory.getAccessorySupplier());
        engine.setTransactionCounterObserver(factory.getEngineSupplier());
        car.setTransactionCounterObserver(factory.getDealer());

        body.setWarehouseCapacity(assembly.getBodyWarehouse());
        accessory.setWarehouseCapacity(assembly.getAccessoryWarehouse());
        engine.setWarehouseCapacity(assembly.getEngineWarehouse());
        car.setWarehouseCapacity(assembly.getCarWarehouse());

        add(new TitlesPanel());
        add(body);
        add(accessory);
        add(engine);
        add(car);
    }
}
