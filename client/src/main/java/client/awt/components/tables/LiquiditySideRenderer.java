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

        String side = (String) table.getModel().getValueAt(row, 4);

        String strength  = (String) table.getModel().getValueAt(row, 5);

        boolean buy = side.equalsIgnoreCase("buy");
        boolean strong = strength.equalsIgnoreCase("strong");

        setText((String) value);

        setForeground(Color.BLACK);
        setBackground(Color.WHITE);
        if(buy){
            if(strong){
                setBackground(Color.decode(Colors.COLOR_GREEN_DARK));
            } else {
                setBackground(Color.decode(Colors.COLOR_GREEN));
            }
        } else {
            if(strong){
                setBackground(Color.decode(Colors.COLOR_RED));
            } else {
                setBackground(Color.decode(Colors.COLOR_ORANGE));
            }
        }

        return this;

    }
}
