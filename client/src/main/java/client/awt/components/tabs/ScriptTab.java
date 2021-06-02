package client.awt.components.tabs;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ScriptTab extends JPanel {

    private Callback callback;

    private Checkbox checkboxResistance;
    private Checkbox checkboxSides;
    private Checkbox checkBoxGaps;
    private JButton btnCreateScript;
    private TextArea textAreaScript;

    public ScriptTab(){
        setLayout(new BorderLayout());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.NORTHWEST;

        JPanel controlPanel = new JPanel();

        JPanel panelResistance = new JPanel();


        panelResistance.add(new JLabel("Supply"));

        checkboxResistance = new Checkbox();
        panelResistance.add(checkboxResistance);

        controlPanel.add(panelResistance);

        JPanel panelSides = new JPanel();
        panelSides.add(new JLabel("Sides"));

        checkboxSides = new Checkbox();
        panelSides.add(checkboxSides);

        controlPanel.add(panelSides);

        JPanel paneGaps = new JPanel();
        paneGaps.add(new JLabel("Gaps"), constraints);

        checkBoxGaps = new Checkbox();
        paneGaps.add(checkBoxGaps);

        controlPanel.add(paneGaps);

        btnCreateScript = new JButton("Create Script");
        btnCreateScript.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                prepareScript();
            }
        });

        controlPanel.add(btnCreateScript, constraints);

        add(controlPanel, BorderLayout.NORTH);

        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 3;
        constraints.weightx = 1;
        constraints.weighty = 1;

        textAreaScript = new TextArea();
        add(textAreaScript, BorderLayout.CENTER);

    }

    private void prepareScript(){
        if(callback != null){
            boolean resistance = checkboxResistance.getState();
            boolean sides = checkboxSides.getState();
            boolean gaps = checkBoxGaps.getState();
            callback.generateScript(resistance, sides, gaps);
        }
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public void addScript(String script){
        textAreaScript.setText(script);
    }

    public interface Callback{
        void generateScript(boolean resistance, boolean sides, boolean gaps);
    }
}
