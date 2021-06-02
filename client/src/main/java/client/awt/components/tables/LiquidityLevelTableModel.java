package client.awt.components.tables;

import com.zygne.stockanalyzer.domain.model.LiquidityLevel;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class LiquidityLevelTableModel extends AbstractTableModel {

    private final List<LiquidityLevel> data = new ArrayList<>();

    private final String[] columnNames = new String[]{
            "Price", "Vol", "Pct", "Rank","Hits"
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
    public Class<?> getColumnClass(int column) {
        return LiquidityLevel.class;
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
        } else if (3 == columnIndex) {
            return String.format("%d", row.getRank());
        } else if (4 == columnIndex) {
            return String.format("%d", row.getHits());
        }
        return null;
    }

    public TableRowSorter<LiquidityLevelTableModel> getSorter(TableModel tableModel){
        TableRowSorter<LiquidityLevelTableModel> sorter = new TableRowSorter<>((LiquidityLevelTableModel) tableModel);
        sorter.setComparator(0, new LiquidityLevelTableModel.PriceSorter());
        sorter.setComparator(2, new LiquidityLevelTableModel.PriceSorter());
        sorter.setComparator(3, new HitsSorter());

        return sorter;
    }

    private static final class PriceSorter implements Comparator<String> {
        @Override
        public int compare(String o1, String o2) {
            double price1 = Double.parseDouble(o1);
            double price2 = Double.parseDouble(o2);

            return Double.compare(price1, price2);
        }
    }

    private static final class HitsSorter implements Comparator<String> {
        @Override
        public int compare(String o1, String o2) {
            int hit1 = Integer.parseInt(o1);
            int hit2 = Integer.parseInt(o2);

            return Integer.compare(hit1, hit2);
        }
    }

}
