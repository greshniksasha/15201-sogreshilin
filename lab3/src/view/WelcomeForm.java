package view;

import model.Client;
import model.ClientConfigs;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by Alexander on 11/06/2017.
 */
public class WelcomeForm  extends JFrame {
    private Boolean validIp = false;
    private Boolean validPort = false;
    private Boolean radioBChecked = false;
    private ClientConfigs configs;
    private JLabel stateL;
    private static final String CANCEL = "CANCEL";
    private static final String OK = "OK";
    private static final Logger log = LogManager.getLogger(WelcomeForm.class);

    private JRadioButton xmlRB;
    private JRadioButton objectsRB;
    private JButton okB;
    private JButton cancelB;
    private JTextField ipTF = new JTextField(10);
    private JTextField portTF = new JTextField(10);


    public WelcomeForm(ClientConfigs configs) throws HeadlessException {
        super("Chat");
        this.configs = configs;

        JPanel mainP = new JPanel(new BorderLayout());
        JPanel centerP = new JPanel(new GridLayout(4,2));
        JLabel ipL = new JLabel("Server IP");
        JLabel portL = new JLabel("Server Port");
        ipL.setHorizontalAlignment(SwingConstants.CENTER);
        portL.setHorizontalAlignment(SwingConstants.CENTER);

        stateL = new JLabel("enter configurations");

        setUpRadioButtons();
        setUpOkButton();
        setUpCancelButton();
        setDefaultValues();

        centerP.add(ipL);
        centerP.add(portL);
        centerP.add(ipTF);
        centerP.add(portTF);
        centerP.add(xmlRB);
        centerP.add(objectsRB);
        centerP.add(cancelB);
        centerP.add(okB);
        JPanel statusP = new JPanel(new BorderLayout());
        statusP.setBorder(new BevelBorder(BevelBorder.LOWERED));
        statusP.add(stateL, BorderLayout.WEST);
        mainP.add(centerP, BorderLayout.CENTER);
        mainP.add(statusP, BorderLayout.SOUTH);
        setContentPane(mainP);
        pack();
        setResizable(false);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        ipTF.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                if (ipTF.getText().isEmpty()) {
                    ipTF.setForeground(Color.black);
                }
            }
        });

        portTF.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                if (portTF.getText().isEmpty()) {
                    portTF.setForeground(Color.black);
                }
            }
        });
    }

    private void setUpRadioButtons() {
        xmlRB = new JRadioButton("xml");
        objectsRB = new JRadioButton("objects");
        ButtonGroup group = new ButtonGroup();
        group.add(xmlRB);
        group.add(objectsRB);
        xmlRB.setHorizontalAlignment(SwingConstants.CENTER);
        objectsRB.setHorizontalAlignment(SwingConstants.CENTER);

        if (configs.getType() != null) {
            radioBChecked = true;
            if (configs.getType().equals("obj")) {
                objectsRB.setSelected(true);
            } else if (configs.getType().equals("xml")) {
                xmlRB.setSelected(true);
            }
        }

        objectsRB.addActionListener((e) -> {
            radioBChecked = true;
            configs.setType("obj");
        });

        xmlRB.addActionListener((e) -> {
            radioBChecked = true;
            configs.setType("xml");
        });
    }

    private void setUpOkButton() {
        okB = new JButton(OK);
        okB.requestFocus();
        getRootPane().setDefaultButton(okB);
        okB.addActionListener((e) -> {
            ipTF.setForeground(setIp(ipTF.getText()) ? Color.black : Color.red);
            portTF.setForeground((setPort(portTF.getText())) ? Color.black : Color.red);

            if (validIp && validPort && radioBChecked) {
                log.info("configs : {}, {}, {}", configs.getType(),
                        configs.getIp(), configs.getPort());
                Client client = new Client(configs);
                client.setConnectionObserver(connected -> {
                    if (connected) {
                        new ClientForm(client).setVisible(true);
                        dispose();
                    } else {
                        this.setEnabled(true);
                        stateL.setText("enter configurations");
                        JOptionPane.showMessageDialog(this,
                                "Cannot connect to server\n" +
                                "Check IP address and TCP-port",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                });
                new Thread(() -> {client.connectToServer();}).start();
                this.setEnabled(false);
                stateL.setText("connecting to the server...");
            } else {
                String errorText = null;
                if (!radioBChecked) {
                    errorText = "Choose XML or OBJ version";
                } else if (!(validIp || validPort)) {
                    errorText = "IP and port do not exist";
                } else if (!validIp) {
                    errorText = "IP does not exist";
                } else if (!validPort) {
                    errorText = "TCP-port does not exist";
                }
                JOptionPane.showMessageDialog(this,
                        errorText,
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void setUpCancelButton() {
        cancelB = new JButton(CANCEL);
        cancelB.addActionListener((e) -> {
            dispose();
            System.exit(0);
        });
    }

    private void setDefaultValues() {
        if (configs.getPort() != null) {
            portTF.setText(String.valueOf(configs.getPort()));
        }
        portTF.setForeground((setPort(portTF.getText())) ? Color.black : Color.red);
        portTF.addActionListener((e) -> {
            portTF.setForeground((setPort(portTF.getText())) ? Color.black : Color.red);
        });

        ipTF.setText(configs.getIp());
        ipTF.setForeground(setIp(ipTF.getText()) ? Color.black : Color.red);
        ipTF.addActionListener((e) -> {
            ipTF.setForeground(setIp(ipTF.getText()) ? Color.black : Color.red);
        });
    }

    private Boolean setIp(String ip) {
        if (ip.toLowerCase().equals("localhost")) {
            configs.setIp(ip);
            return validIp = true;
        }
        if (ip.matches(IPADDRESS_PATTERN)) {
            configs.setIp(ip);
            return validIp = true;
        }
        return validIp = false;
    }

    private Boolean setPort(String port) {
        try {
            int value = Integer.parseInt(port);
            if (1023 < value && value < (2 << 15)) {
                configs.setPort(value);
                return validPort = true;
            } else {
                return validPort = false;
            }
        } catch (NumberFormatException ex) {
            return validPort = false;
        }
    }

    private static final String IPADDRESS_PATTERN =
            "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
            "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
            "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
            "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

}
