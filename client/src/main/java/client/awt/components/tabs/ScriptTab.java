package client.awt.components.tabs;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ScriptTab extends JPanel {

    private Callback callback;

    private Checkbox checkboxResistance;
    private Checkbox checkboxSides;
    private JButton btnCreateScript;
    private TextArea textAreaScript;

    public ScriptTab(){
        setLayout(new BorderLayout());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.NORTHWEST;

        JPanel panel1 = new JPanel(new GridBagLayout());

        constraints.gridx = 0;
        constraints.gridy = 0;
        panel1.add(new JLabel("Resistance"), constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        checkboxResistance = new Checkbox();
        panel1.add(checkboxResistance, constraints);

        constraints.gridx = 2;
        constraints.gridy = 0;
        panel1.add(new JLabel("Sides"), constraints);

        constraints.gridx = 2;
        constraints.gridy = 1;
        checkboxSides = new Checkbox();
        panel1.add(checkboxSides, constraints);

        btnCreateScript = new JButton("Create Script");
        btnCreateScript.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                prepareScript();
            }
        });

        constraints.gridx = 0;
        constraints.gridy = 3;
        panel1.add(btnCreateScript, constraints);

        add(panel1, BorderLayout.NORTH);

        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 2;
        constraints.weightx = 1;
        constraints.weighty = 1;

        textAreaScript = new TextArea();
        add(textAreaScript, BorderLayout.CENTER);

    }

    private void prepareScript(){
        if(callback != null){
            boolean resistance = checkboxResistance.getState();
            boolean sides = checkboxSides.getState();
            callback.generateScript(resistance, sides);
        }
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public void addScript(String script){
        textAreaScript.setText(script);
    }

    public interface Callback{
        void generateScript(boolean resistance, boolean sides);
    }
}
