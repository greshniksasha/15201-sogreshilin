package view.panel;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;

/**
 * Created by Alexander on 23/05/2017.
 */
public class TitlesPanel extends JPanel {
    final static int COLUMNS_COUNT = 4;

    public TitlesPanel() {
        setLayout(new GridLayout(1,COLUMNS_COUNT));
        JLabel[] title = {new JLabel("<html><b>Transactions</b></html>"),
                new JLabel("<html><b>Warehouse</b></html>"),
                new JLabel("<html><b>Size</b></html>"),
                new JLabel("<html><b>Capacity</b></html>")};
        for (int i = 0; i < COLUMNS_COUNT; ++i) {
            title[i].setBorder(new EtchedBorder());
            title[i].setHorizontalAlignment(SwingConstants.CENTER);
            add(title[i]);
        }
    }
}
