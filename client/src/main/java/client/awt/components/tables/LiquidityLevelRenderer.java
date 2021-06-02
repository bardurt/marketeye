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

        double percentile = -1;
        double rank = -1;

        try {
            percentile = Double.parseDouble((String) table.getModel().getValueAt(row, 2));
        } catch (Exception e) {
        }

        try {
            rank = Double.parseDouble((String) table.getModel().getValueAt(row, 3));
        } catch (Exception e) {
        }

        if(row % 2 == 0){
            setForeground(Color.black);
            setBackground(Color.WHITE);
        } else {
            setForeground(Color.black);
            setBackground(Color.decode("#EEEEEE"));
        }

        if(percentile > 99){
            setForeground(Color.black);
            setBackground(Color.decode("#80D2F8"));
        }

        if(rank == 1){
            setForeground(Color.black);
            setBackground(Color.decode("#FF7676"));
        }

        setText((String) value);

        return this;
    }

}