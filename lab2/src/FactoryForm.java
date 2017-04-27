import com.sun.xml.internal.bind.v2.runtime.reflect.Accessor;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created by Alexander on 09/04/2017.
 */
public class FactoryForm extends JFrame {
    private JPanel factoryRootPanel;
    private JPanel bottomLabelPannel;
    private JButton startButton;
    private JButton resetButton;
    private JLabel timeoutsLabel;
    private JSpinner enginesSpinner;
    private JSlider dealerTimeoutSlider;
    private JSlider accessorySupplierTimeoutSlider;
    private JSlider bodySupplierTimeoutSlider;
    private JSlider engineSupplierTimeoutSlider;
    private JTextField engineSupplierTimeoutTextField;
    private JTextField bodySupplierTimeoutTextField;
    private JTextField accessorySupplierTimeoutTextField;
    private JTextField dealerTimeoutTextField;
    private JLabel engineWarehouseSizeLabel;
    private JLabel bodyWarehouseSizeLabel;
    private JLabel accessoryWarehouseSizeLabel;
    private JLabel carWarehouseSizeLabel;
    public JLabel engineWarehouseCapacityLabel;
    public JLabel bodyWarehouseCapacityLabel;
    public JLabel accessoryWarehouseCapacityLabel;
    public JLabel carWarehouseCapacityLabel;
    private JLabel totalCarsSoldLabel;
    private JLabel Items;
    private JPanel itemsCountLabel;
    private JLabel enginesCountLabel;
    private JLabel accessoriesCountLabel;
    private JLabel bodiesCountLabel;
    private JCheckBox logSalesCheckbox;

    private AssemblyController assemblyController;
    private Assembly assembly;
    private Supplier<Body> bodySupplier;
    private Supplier<Engine> engineSupplier;
    private AccessorySuppliers accessorySuppliers;
    private Dealer[] dealers;

    public FactoryForm(AssemblyController assemblyController,
                       Boolean logSales) throws HeadlessException {

        super("Factory");
        this.assemblyController = assemblyController;
        this.assembly = assemblyController.getAssembly();
        this.bodySupplier = assemblyController.getBodySupplier();
        this.engineSupplier = assemblyController.getEngineSupplier();
        this.accessorySuppliers = assemblyController.getAccessorySuppliers();
        this.dealers = assemblyController.getDealers();

        setContentPane(factoryRootPanel);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        setSupplierTimeoutSliders();
        setSupplierTimeoutTextFields();
        setWarehouseCapacityLabels();
        setWarehouseSizeListeners();
        setLogSalesCheckbox(logSales);
        setTotalCarsSoldListener();
        setTotalItemsSuppliedListeners();

//        this.addWindowListener(new WindowAdapter() {
//            @Override
//            public void windowClosing(WindowEvent e) {
//                assemblyController.finish();
//                FactoryForm.this.dispose();
//            }
//        });
    }

    private void setTotalItemsSuppliedListeners() {
        assembly.getEngineWarehouse().addObserver(() -> {
            enginesCountLabel.setText(String.valueOf(assembly.getEngineWarehouse().getItemsCount()));
        });
        assembly.getAccessoryWarehouse().addObserver(() -> {
            accessoriesCountLabel.setText(String.valueOf(assembly.getAccessoryWarehouse().getItemsCount()));
        });
        assembly.getBodyWarehouse().addObserver(() -> {
            bodiesCountLabel.setText(String.valueOf(assembly.getBodyWarehouse().getItemsCount()));
        });

        startButton.addActionListener(e -> {
            assemblyController.start();
        });
    }

    private void setTotalCarsSoldListener() {
        for (Dealer dealer : dealers) {
            dealer.addObserver(() -> {
                totalCarsSoldLabel.setText(String.valueOf(dealer.getSoldCounter()));
            });
        }
    }

    private void setLogSalesCheckbox(Boolean logSales) {
        logSalesCheckbox.setSelected(logSales);
        logSalesCheckbox.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                AbstractButton abstractButton = (AbstractButton)e.getSource();
                ButtonModel buttonModel = abstractButton.getModel();
                boolean selected = buttonModel.isSelected();
                if (selected) {
                    Logger.getRootLogger().setLevel(Level.ALL);
                } else {
                    Logger.getRootLogger().setLevel(Level.OFF);
                }
            }
        });
    }

    private void setWarehouseCapacityLabels() {
        engineWarehouseCapacityLabel.setText("/ " + String.valueOf(assembly.getEngineWarehouse().getCapacity()));
        accessoryWarehouseCapacityLabel.setText("/ " + String.valueOf(assembly.getAccessoryWarehouse().getCapacity()));
        carWarehouseCapacityLabel.setText("/ " + String.valueOf(assembly.getCarWarehouse().getCapacity()));
        bodyWarehouseCapacityLabel.setText("/ " + String.valueOf(assembly.getBodyWarehouse().getCapacity()));
    }


    private void setSupplierTimeoutTextFields() {
        engineSupplierTimeoutTextField.setText(String.valueOf(engineSupplier.getTimeout()));
        bodySupplierTimeoutTextField.setText(String.valueOf(bodySupplier.getTimeout()));
        accessorySupplierTimeoutTextField.setText(String.valueOf(accessorySuppliers.getTimeout()));
        dealerTimeoutTextField.setText(String.valueOf(dealers[0].getTimeout()));

        engineSupplierTimeoutTextField.addActionListener(e -> {
            int val = Integer.parseInt(engineSupplierTimeoutTextField.getText());
            val = Math.max(val, engineSupplierTimeoutSlider.getMinimum());
            val = Math.min(val, engineSupplierTimeoutSlider.getMaximum());
            engineSupplierTimeoutSlider.setValue(val);
            engineSupplier.setTimeout(val);
            engineSupplierTimeoutTextField.setText(String.valueOf(val));
        });

        bodySupplierTimeoutTextField.addActionListener(e -> {
            int val = Integer.parseInt(bodySupplierTimeoutTextField.getText());
            val = Math.max(val, bodySupplierTimeoutSlider.getMinimum());
            val = Math.min(val, bodySupplierTimeoutSlider.getMaximum());
            bodySupplierTimeoutSlider.setValue(val);
            bodySupplier.setTimeout(val);
            bodySupplierTimeoutTextField.setText(String.valueOf(val));
        });

        accessorySupplierTimeoutTextField.addActionListener(e -> {
            int val = Integer.parseInt(accessorySupplierTimeoutTextField.getText());
            val = Math.max(val, accessorySupplierTimeoutSlider.getMinimum());
            val = Math.min(val, accessorySupplierTimeoutSlider.getMaximum());
            accessorySupplierTimeoutSlider.setValue(val);
            accessorySuppliers.setTimeout(val);
            accessorySupplierTimeoutTextField.setText(String.valueOf(val));
        });

        dealerTimeoutTextField.addActionListener(e -> {
            int val = Integer.parseInt(dealerTimeoutTextField.getText());
            val = Math.max(val, dealerTimeoutSlider.getMinimum());
            val = Math.min(val, dealerTimeoutSlider.getMaximum());
            dealerTimeoutSlider.setValue(val);
            for (Dealer dealer : dealers) {
                dealer.setTimeout(val);
            }
            dealerTimeoutTextField.setText(String.valueOf(val));
        });
    }

    private void setWarehouseSizeListeners() {
        assembly.getEngineWarehouse().addObserver(() -> {
            engineWarehouseSizeLabel.setText(String.valueOf(assembly.getEngineWarehouse().getSize()));
        });
        assembly.getAccessoryWarehouse().addObserver(() -> {
            accessoryWarehouseSizeLabel.setText(String.valueOf(assembly.getAccessoryWarehouse().getSize()));
        });
        assembly.getBodyWarehouse().addObserver(() -> {
            bodyWarehouseSizeLabel.setText(String.valueOf(assembly.getBodyWarehouse().getSize()));
        });
        assembly.getCarWarehouse().addObserver(() -> {
            carWarehouseSizeLabel.setText(String.valueOf(assembly.getCarWarehouse().getSize()));
        });
    }

    private void setSupplierTimeoutSliders() {
        engineSupplierTimeoutSlider.addChangeListener(e -> {
            int val = engineSupplierTimeoutSlider.getValue();
            engineSupplierTimeoutTextField.setText(String.valueOf(val));
            engineSupplier.setTimeout(val);
        });

        bodySupplierTimeoutSlider.addChangeListener(e -> {
            int val = bodySupplierTimeoutSlider.getValue();
            bodySupplierTimeoutTextField.setText(String.valueOf(val));
            bodySupplier.setTimeout(val);
        });

        accessorySupplierTimeoutSlider.addChangeListener(e -> {
            int val = accessorySupplierTimeoutSlider.getValue();
            accessorySupplierTimeoutTextField.setText(String.valueOf(val));
            accessorySuppliers.setTimeout(val);
        });

        dealerTimeoutSlider.addChangeListener(e -> {
            int val = dealerTimeoutSlider.getValue();
            dealerTimeoutTextField.setText(String.valueOf(val));
            for (Dealer dealer : dealers) {
                dealer.setTimeout(val);
            }
        });
    }

}


