package client.awt.components.tables;

import com.zygne.stockanalyzer.domain.model.LiquidityLevel;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class LiquidityLevelTableModel extends AbstractTableModel {

    private final List<LiquidityLevel> data = new ArrayList<>();

    private final String[] columnNames = new String[]{
            "Price", "Vol", "Pctl"
    };

    private final Class[] columnClass = new Class[]{
            String.class, String.class, String.class
    };

    public void addItems(List<LiquidityLevel> data){
        this.data.addAll(data);
    }

    public void clear(){
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
        LiquidityLevel row = data.get(rowIndex);
        if (0 == columnIndex) {
            return String.format("%.2f", row.getPrice());
        } else if (1 == columnIndex) {
            return String.format("%,d", row.getVolume());
        } else if (2 == columnIndex) {
            return String.format("%.2f", row.getPercentile());
        }
        return null;
    }
}
