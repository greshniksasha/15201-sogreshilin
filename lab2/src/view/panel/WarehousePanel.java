package view.panel;

import model.Assembly;
import model.Factory;
import view.InformationLabels;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class WarehousePanel extends JPanel {

    private InformationLabels car;
    private InformationLabels body;
    private InformationLabels engine;
    private InformationLabels accessory;

    public WarehousePanel(Factory factory) {
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

        car.setWarehouseCapacity(assembly.getCarWarehouse());
        body.setWarehouseCapacity(assembly.getBodyWarehouse());
        engine.setWarehouseCapacity(assembly.getEngineWarehouse());
        accessory.setWarehouseCapacity(assembly.getAccessoryWarehouse());

        JPanel titles = new JPanel();
        titles.setLayout(new GridLayout(1,3, 2, 5));
        JLabel[] title = {new JLabel("<html><b>Warehouse</b></html>"),
                new JLabel("<html><b>Size</b></html>"),
                new JLabel("<html><b>Capacity</b></html>")};
        for (int i = 0; i < 3; ++i) {
            title[i].setBorder(new EtchedBorder());
            title[i].setHorizontalAlignment(SwingConstants.CENTER);
            titles.add(title[i]);
        }

        add(titles);
        add(accessory);
        add(engine);
        add(body);
        add(car);
    }
}
