package view;

import model.BlockingQueue;
import model.contractor.Contractor;
import model.contractor.TransactionCounterObserver;
import model.warehouse.Warehouse;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;

public class InformationLabels extends JPanel {
    private JLabel name;
    private JLabel warehouseSize;
    private JLabel warehouseCapacity;

    public InformationLabels(String s) {
        setLayout(new GridLayout(1,3,2,5));
        this.name = new JLabel(s);
        this.warehouseSize = new JLabel(String.valueOf(0));
        this.warehouseCapacity = new JLabel();

        name.setHorizontalAlignment(SwingConstants.CENTER);
        warehouseSize.setHorizontalAlignment(SwingConstants.CENTER);
        warehouseCapacity.setHorizontalAlignment(SwingConstants.CENTER);

        name.setBorder(new EtchedBorder());
        warehouseSize.setBorder(new EtchedBorder());
        warehouseCapacity.setBorder(new EtchedBorder());

        add(name);
        add(warehouseSize);
        add(warehouseCapacity);
    }

    public void setWarehouseCapacity(Warehouse warehouse) {
        this.warehouseCapacity.setText(String.valueOf(warehouse.getCapacity()));
    }

    public void setSizeObserver(Warehouse warehouse) {
        warehouse.addSizeObserver(new BlockingQueue.SizeObserver() {
            public void sizeChanged(int size) {
                warehouseSize.setText(String.valueOf(size));
            }
        });
    }
}
