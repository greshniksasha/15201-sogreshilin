package view.panel;

import model.Assembly;
import model.BlockingQueue;
import model.Factory;
import model.ThreadPool;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * Created by Alexander on 26/05/2017.
 */
public class AssemblyPanel extends JPanel {

    public static final String WORKERS_LABEL = "Workers";
    public static final String TASKS_TODO_LABEL = "Tasks in queue";


    public AssemblyPanel(Factory factory) {
        ThreadPool pool = factory.getAssembly().getPool();
        BlockingQueue poolQueue = pool.getQueue();
        int capacity = pool.getCapacity();

        setBorder(new TitledBorder("Assembly"));
        setLayout(new GridLayout(2,1));

        JLabel workers = new JLabel(String.valueOf(capacity));
        workers.setHorizontalAlignment(SwingConstants.CENTER);
        JPanel workerPanel = new JPanel(new GridLayout(1,2));
        workerPanel.add(new JLabel(WORKERS_LABEL));
        workerPanel.add(workers);
        workerPanel.setBorder(new EtchedBorder());

        JLabel tasksInQueueLabel = new JLabel("0");
        tasksInQueueLabel.setHorizontalAlignment(SwingConstants.CENTER);
        poolQueue.addSizeObserver((size) -> tasksInQueueLabel.setText(String.valueOf(size)));
        JPanel taskPanel = new JPanel(new GridLayout(1,2));
        taskPanel.add(new JLabel(TASKS_TODO_LABEL));
        taskPanel.add(tasksInQueueLabel);
        taskPanel.setBorder(new EtchedBorder());

        add(workerPanel);
        add(taskPanel);

    }

}
