package client.awt.components.tabs;

import com.zygne.stockanalyzer.domain.model.Settings;
import com.zygne.stockanalyzer.domain.model.enums.DataProvider;
import javafx.scene.control.RadioButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SettingsTab  extends JPanel {

    private Callback callback;
    private TextArea textAreaInfo;
    private JRadioButton rbIb;
    private JRadioButton rbAv;
    private JRadioButton rbYahoo;

    public SettingsTab(){
        setLayout(new BorderLayout());

        JPanel providerPanel = new JPanel(new GridBagLayout());

        GridBagConstraints gridBagConstraints = new GridBagConstraints();

        rbIb = new JRadioButton("Interactive Brokers");
        rbAv = new JRadioButton("Alpha Vantage");
        rbYahoo = new JRadioButton("Yahoo Finance");

        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(rbIb);
        buttonGroup.add(rbAv);
        buttonGroup.add(rbYahoo);

        rbIb.setSelected(true);

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        providerPanel.add(rbIb, gridBagConstraints);

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        providerPanel.add(rbAv, gridBagConstraints);

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        providerPanel.add(rbYahoo, gridBagConstraints);

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;

        JButton btnSelectProvider = new JButton("Set");
        providerPanel.add(btnSelectProvider, gridBagConstraints);
        btnSelectProvider.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                selectProvider();
            }
        });

        textAreaInfo = new TextArea("");
        textAreaInfo.setEditable(false);

        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.add(textAreaInfo);

        add(providerPanel, BorderLayout.NORTH);
        add(infoPanel, BorderLayout.SOUTH);

    }

    public void setSettings(Settings settings){
        textAreaInfo.setText(settings.toString());

        if(settings.getDataProvider() == DataProvider.ALPHA_VANTAGE){
            rbAv.setSelected(true);
        } else if (settings.getDataProvider() == DataProvider.INTERACTIVE_BROKERS) {
            rbIb.setSelected(true);
        }  else if (settings.getDataProvider() == DataProvider.YAHOO_FINANCE) {
            rbYahoo.setSelected(true);
        }

    }

    public void setCallback(Callback callback){
        this.callback = callback;
    }

    private void selectProvider(){

        if(callback == null){
            return;
        }

        if(rbIb.isSelected()){
            callback.onProviderSelected(DataProvider.INTERACTIVE_BROKERS);
        } else if (rbAv.isSelected()){
            callback.onProviderSelected(DataProvider.ALPHA_VANTAGE);
        } else {
            callback.onProviderSelected(DataProvider.YAHOO_FINANCE);
        }

    }

    public interface Callback {
        void onProviderSelected(DataProvider dataProvider);
    }

}