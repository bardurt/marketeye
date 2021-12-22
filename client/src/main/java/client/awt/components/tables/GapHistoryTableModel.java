package client.awt.components.tables;

import com.zygne.stockanalyzer.domain.model.GapHistory;
import com.zygne.stockanalyzer.domain.model.LiquidityLevel;

import javax.swing.table.AbstractTableModel;

public class GapHistoryTableModel extends AbstractTableModel {

    private GapHistory gapHistory;

    private final String[] columnNames = new String[]{
            "Gaps", "Bullish", "Avg Bullish Change", "Avg High", "Max High"
    };


    public void addItems(GapHistory gapHistory){
        this.gapHistory = gapHistory;
    }

    @Override
    public int getRowCount() {
        return 1;
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

        if(gapHistory == null){
            return "";
        }

        if (0 == columnIndex) {
            return ""+ gapHistory.getTotalGaps();
        } else if (1 == columnIndex) {
            return "" + gapHistory.getBullishGaps();
        } else if (2 == columnIndex) {
            return String.format("%.2f", gapHistory.getAvgBullishChange()) + '%';
        } else if (3 == columnIndex) {
            return String.format("%.2f", gapHistory.getAvgHighChange()) + '%';
        } else if (4 == columnIndex) {
            return String.format("%.2f", gapHistory.getMaxHighChange()) + '%';
        }
        return null;
    }
}
