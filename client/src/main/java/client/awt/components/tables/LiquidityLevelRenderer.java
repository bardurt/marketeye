package client.awt.components.tables;

import client.awt.components.Colors;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class LiquidityLevelRenderer extends JLabel implements TableCellRenderer {

    public LiquidityLevelRenderer() {
        super.setOpaque(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                   boolean hasFocus, int row, int column) {

        String percentileString = (String) table.getModel().getValueAt(row, 2);
        double percentile = -1;

        try {
            percentile = Double.parseDouble(percentileString);
        } catch (Exception e) {
        }

        setText((String) value);
        setForeground(Color.black);

        if (percentile >= 100) {
            setForeground(Color.WHITE);
            setBackground(Color.decode(Colors.COLOR_RED_DARK));
        } else if (percentile > 99) {
            setBackground(Color.decode(Colors.COLOR_RED));
        } else if (percentile > 98) {
            setBackground(Color.decode(Colors.COLOR_ORANGE));
        } else {
            setBackground(Color.WHITE);
        }

        return this;
    }

}