package client.awt.components.tables;

import com.zygne.stockanalyzer.domain.model.LiquiditySide;
import com.zygne.stockanalyzer.domain.utils.TimeHelper;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class LiquiditySideTableModel extends AbstractTableModel {

    private final List<LiquiditySide> data = new ArrayList<>();

    private final String[] columnNames = new String[]{
            "Range", "Vol", "Pctl", "Date"
    };

    private final Class[] columnClass = new Class[]{
            String.class, String.class, String.class, String.class, String.class, String.class
    };

    public void addItems(List<LiquiditySide> data) {
        this.data.addAll(data);
    }

    public void addItem(LiquiditySide liquiditySide) {
        this.data.add(liquiditySide);
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
            String range = String.format("%.2f", row.getStart()) + " - " + String.format("%.2f", row.getEnd());
            return range;
        } else if (1 == columnIndex) {
            return String.format("%,d", row.getVolume());
        } else if (2 == columnIndex) {
            return String.format("%.2f", row.percentile);
        } else if (3 == columnIndex) {
            return TimeHelper.getDateFromTimeStamp(row.getTimeStamp());
        }
        return null;
    }

    public TableRowSorter<LiquiditySideTableModel> getSorter(TableModel tableModel){
        TableRowSorter<LiquiditySideTableModel> sorter = new TableRowSorter<>((LiquiditySideTableModel) tableModel);
       // sorter.setComparator(3, new DateSorter());

        return sorter;
    }

    private static final class DateSorter implements Comparator<String> {
        @Override
        public int compare(String o1, String o2) {
            long date1 = Long.parseLong(o1);
            long date2 = Long.parseLong(o2);

            return Long.compare(date1, date2);
        }
    }
}