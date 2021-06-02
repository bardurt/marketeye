package client.awt.components.tables;

import client.awt.components.Colors;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class LiquiditySideRenderer extends JLabel implements TableCellRenderer {


    public LiquiditySideRenderer() {
        super.setOpaque(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                   boolean hasFocus, int row, int column) {

        if(row % 2 == 0){
            setForeground(Color.black);
            setBackground(Color.WHITE);
        } else {
            setForeground(Color.black);
            setBackground(Color.decode("#EEEEEE"));
        }

        setText((String) value);

        return this;

    }
}
