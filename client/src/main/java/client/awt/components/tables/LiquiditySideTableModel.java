package client.awt.components.tables;

import com.zygne.stockanalyzer.domain.model.LiquiditySide;
import com.zygne.stockanalyzer.domain.utils.TimeHelper;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class LiquiditySideTableModel extends AbstractTableModel {

    private final List<LiquiditySide> data = new ArrayList<>();

    private final String[] columnNames = new String[]{
            "Start", "End", "Vol","Pctl", "Side", "Strength", "Date"
    };

    private final Class[] columnClass = new Class[]{
            String.class, String.class, String.class, String.class, String.class, String.class, String.class
    };

    public void addItems(List<LiquiditySide> data) {
        this.data.addAll(data);
    }

    public void clear() {
        this.data.clear();
    }

    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return columnClass[columnIndex];
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        LiquiditySide row = data.get(rowIndex);
        if (0 == columnIndex) {
            return String.format("%.2f", row.getStart());
        } else if (1 == columnIndex) {
            return String.format("%.2f", row.getEnd());
        } else if (2 == columnIndex) {
            return String.format("%,d", row.getVolume());
        } else if (3 == columnIndex) {
            return String.format("%.2f", row.percentile);
        } else if (4 == columnIndex) {
            return row.getSide();
        } else if (5 == columnIndex) {
            return row.getStrength();
        } else if (6 == columnIndex) {
            return TimeHelper.getDateTimeFromTimeStamp(row.getTimeStamp());
        }
        return null;
    }
}