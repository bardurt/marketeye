package client.awt.components.tables;

import com.zygne.stockanalyzer.domain.model.PriceGap;
import com.zygne.stockanalyzer.domain.utils.TimeHelper;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class PriceGapTableModel extends AbstractTableModel {

    private final List<PriceGap> data = new ArrayList<>();

    private final String[] columnNames = new String[]{
            "Start", "End", "Date"
    };

    private final Class[] columnClass = new Class[]{
            String.class, String.class, String.class
    };

    public void addItems(List<PriceGap> data){
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
        PriceGap row = data.get(rowIndex);
        if (0 == columnIndex) {
            return String.format("%.2f", row.getStart());
        } else if (1 == columnIndex) {
            return String.format("%.2f", row.getEnd());
        } else if (2 == columnIndex) {
            return TimeHelper.getDateTimeFromTimeStamp(row.getTimeStamp());
        }
        return null;
    }
}
