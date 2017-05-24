package view;

import model.BlockingQueue;
import model.contractor.Contractor;
import model.contractor.TransactionCounterObserver;
import model.warehouse.Warehouse;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;

public class InformationLabels extends JPanel {
    private JLabel total;
    private JLabel name;
    private JLabel warehouseSize;
    private JLabel warehouseCapacity;

    public InformationLabels(String s) {
        setLayout(new GridLayout(1,4));
        this.total = new JLabel(String.valueOf(0));
        this.name = new JLabel(s);
        this.warehouseSize = new JLabel(String.valueOf(0));
        this.warehouseCapacity = new JLabel();

        total.setHorizontalAlignment(SwingConstants.CENTER);
        name.setHorizontalAlignment(SwingConstants.CENTER);
        warehouseSize.setHorizontalAlignment(SwingConstants.CENTER);
        warehouseCapacity.setHorizontalAlignment(SwingConstants.CENTER);

        total.setBorder(new EtchedBorder());
        name.setBorder(new EtchedBorder());
        warehouseSize.setBorder(new EtchedBorder());
        warehouseCapacity.setBorder(new EtchedBorder());


        add(total);
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

    public void setTransactionCounterObserver(Contractor contractor) {
        contractor.addTransactionCounterObserver(new TransactionCounterObserver() {
            public void transactionsMade(int count) {
                total.setText(String.valueOf(count));
            }
        });
    }
}
