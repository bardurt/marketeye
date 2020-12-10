package client.awt.components.tabs;

import client.awt.components.tables.PriceGapRenderer;
import client.awt.components.tables.PriceGapTableModel;
import com.zygne.stockanalyzer.domain.model.PriceGap;
import com.zygne.stockanalyzer.domain.model.enums.TimeInterval;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PriceGapTab extends JPanel {

    private JTable table;
    private PriceGapTableModel tableModel;

    private List<TimeInterval> timeIntervalList = new ArrayList<>();

    public PriceGapTab() {
        setLayout(new BorderLayout());


        tableModel = new PriceGapTableModel();

        table = new JTable(tableModel);
        table.setDefaultRenderer(String.class, new PriceGapRenderer());

        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    public void addPriceGaps(List<PriceGap> data) {
        tableModel.clear();
        tableModel.addItems(data);
        tableModel.fireTableDataChanged();
        table.invalidate();
    }

    public void clear(){
        tableModel.clear();
        tableModel.fireTableDataChanged();
        table.invalidate();
    }
}
