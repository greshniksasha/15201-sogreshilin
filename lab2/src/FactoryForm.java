import com.sun.xml.internal.bind.v2.runtime.reflect.Accessor;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

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
    private JPanel enginePanel;
    private JPanel bodyPanel;
    private JPanel accessoryPanel;
    private JPanel dealerPanel;

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

//        CategoryDataset dataset = createDataset();
//        JFreeChart chart = createChart(dataset);
//        final ChartPanel chartPanel = new ChartPanel(chart);
//        chartPanel.setPreferredSize(new Dimension(250, 270));
//        setContentPane(chartPanel);

    }

    private JFreeChart createChart(CategoryDataset dataset) {
        JFreeChart chart = ChartFactory.createBarChart(
                "engine_warehouse",
                "time",
                "elements",
                dataset,
                PlotOrientation.VERTICAL,
                false,
                false,
                false
        );
        chart.setBackgroundPaint(Color.white);

        // get a reference to the plot for further customisation...
        final CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.lightGray);
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);

        // set the range axis to display integers only...
        final NumberAxis rangeAxis = (NumberAxis)plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

        // disable bar outlines...
        final BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setDrawBarOutline(false);

        // set up gradient paints for series...
        final GradientPaint gp0 = new GradientPaint(
                0.0f, 0.0f, Color.blue,
                0.0f, 0.0f, Color.lightGray
        );
        final GradientPaint gp1 = new GradientPaint(
                0.0f, 0.0f, Color.green,
                0.0f, 0.0f, Color.lightGray
        );
        final GradientPaint gp2 = new GradientPaint(
                0.0f, 0.0f, Color.red,
                0.0f, 0.0f, Color.lightGray
        );
        renderer.setSeriesPaint(0, gp0);
        renderer.setSeriesPaint(1, gp1);
        renderer.setSeriesPaint(2, gp2);

        final CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryLabelPositions(
                CategoryLabelPositions.createUpRotationLabelPositions(Math.PI / 6.0)
        );
        // OPTIONAL CUSTOMISATION COMPLETED.
        return chart;
    }

    private CategoryDataset createDataset() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(1, "First", "Category-1");
        dataset.addValue(2, "First", "Category-2");
        dataset.addValue(3, "First", "Category-3");
        dataset.addValue(4, "First", "Category-4");
        dataset.addValue(5, "First", "Category-5");
        return dataset;
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
            JSlider source = (JSlider)e.getSource();
            int val = engineSupplierTimeoutSlider.getValue();
            engineSupplierTimeoutTextField.setText(String.valueOf(val));
            if (!source.getValueIsAdjusting()) {
                engineSupplier.setTimeout(val);
            }
        });

        bodySupplierTimeoutSlider.addChangeListener(e -> {
            JSlider source = (JSlider)e.getSource();
            int val = bodySupplierTimeoutSlider.getValue();
            bodySupplierTimeoutTextField.setText(String.valueOf(val));
            if (!source.getValueIsAdjusting()) {
                bodySupplier.setTimeout(val);
            }
        });

        accessorySupplierTimeoutSlider.addChangeListener(e -> {
            JSlider source = (JSlider)e.getSource();
            int val = accessorySupplierTimeoutSlider.getValue();
            accessorySupplierTimeoutTextField.setText(String.valueOf(val));
            if (!source.getValueIsAdjusting()) {
                accessorySuppliers.setTimeout(val);
            }
        });

        dealerTimeoutSlider.addChangeListener(e -> {
            JSlider source = (JSlider)e.getSource();
            int val = dealerTimeoutSlider.getValue();
            dealerTimeoutTextField.setText(String.valueOf(val));
            if (!source.getValueIsAdjusting()) {
                for (Dealer dealer : dealers) {
                    dealer.setTimeout(val);
                }
            }
        });
    }

}


