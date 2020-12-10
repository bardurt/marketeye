package client.awt.components.tables;

import client.awt.components.Colors;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class PriceGapRenderer extends JLabel implements TableCellRenderer {

    public PriceGapRenderer() {
        super.setOpaque(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                   boolean hasFocus, int row, int column) {

        setText((String) value);

        setForeground(Color.BLACK);
        setBackground(Color.WHITE);
        return this;

    }
}
