package view;

import model.Client;
import model.ClientConfigs;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Created by Alexander on 11/06/2017.
 */
public class WelcomeForm  extends JFrame {
    private static final String IPADDRESS_PATTERN =
                    "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

    private Boolean validIp = false;
    private Boolean validPort = false;
    private Boolean radioBChecked = false;
    private ClientConfigs configs;
    private JLabel stateL;

    public WelcomeForm(ClientConfigs configs) throws HeadlessException {
        super("Chat");
        this.configs = configs;

        JPanel mainP = new JPanel(new BorderLayout());
        JPanel centerP = new JPanel(new GridLayout(4,2));
        JLabel ipL = new JLabel("Server IP");
        JLabel portL = new JLabel("Server Port");
        JTextField ipTF = new JTextField(10);
        JTextField portTF = new JTextField();
        JRadioButton xmlRB = new JRadioButton("xml");
        JRadioButton objectsRB = new JRadioButton("objects");
        ButtonGroup group = new ButtonGroup();
        group.add(xmlRB);
        group.add(objectsRB);
        JButton cancelB = new JButton("CANCEL");
        JButton okB = new JButton("OK");
        stateL = new JLabel("enter configurations");

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

        ipL.setHorizontalAlignment(SwingConstants.CENTER);
        portL.setHorizontalAlignment(SwingConstants.CENTER);
        xmlRB.setHorizontalAlignment(SwingConstants.CENTER);
        objectsRB.setHorizontalAlignment(SwingConstants.CENTER);

        objectsRB.addChangeListener((e) -> {
            radioBChecked = true;
            configs.setType("obj");
        });

        xmlRB.addChangeListener((e) -> {
            radioBChecked = true;
            configs.setType("xml");
        });

        if (configs.getType() != null) {
            radioBChecked = true;
            if (configs.getType().equals("obj")) {
                objectsRB.setSelected(true);
            } else {
                xmlRB.setSelected(true);
            }
        }

        okB.requestFocusInWindow();
        getRootPane().setDefaultButton(okB);
        setContentPane(mainP);
        pack();
        setResizable(false);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

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

        okB.addActionListener((e) -> {
            ipTF.setForeground(setIp(ipTF.getText()) ? Color.black : Color.red);
            portTF.setForeground((setPort(portTF.getText())) ? Color.black : Color.red);
            if (validIp && validPort && radioBChecked) {
                Client client = new Client(configs);
                client.setConnectionObserver(connected -> {
                    if (connected) {
                        new ClientForm(client).setVisible(true);
                        dispose();
                    } else {
                        this.setEnabled(true);
                        stateL.setText("enter configurations");
                        JOptionPane.showMessageDialog(this,
                                "Cannot connect to server\nCheck IP address and TCP-port",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                });
                new Thread(() -> {
                    client.connectToServer();
                }).start();
                this.setEnabled(false);
                stateL.setText("connecting to the server...");
            } else {
                if (!radioBChecked) {
                    JOptionPane.showMessageDialog(this, "Choose XML or OBJ version");
                }
                if (!(validIp || validPort)) {
                    JOptionPane.showMessageDialog(this, "IP and port do not exist");
                } else if (!validIp) {
                    JOptionPane.showMessageDialog(this, "IP does not exist");
                } else if (!validPort) {
                    JOptionPane.showMessageDialog(this, "TCP-port does not exist");
                }
            }
        });

        cancelB.addActionListener((e) -> {
                dispose();
                System.exit(0);
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
            System.out.println("value : " + value);
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
}
