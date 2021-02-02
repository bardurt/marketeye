package client.awt.components.views;

import com.zygne.stockanalyzer.domain.model.Fundamentals;

import javax.swing.*;
import java.awt.*;

public class FundamentalsView extends JPanel {


    private JTextArea textFieldFloat;

    public FundamentalsView() {
        setLayout(new GridLayout());

        textFieldFloat = new JTextArea("-");
        textFieldFloat.setEditable(false);

        add(textFieldFloat);

    }

    public void populateFrom(Fundamentals fundamentals){

        String details = "Fundamentals\n";
        details += "Float : " + String.format("%,d", fundamentals.getSharesFloat()) + "\n";
        details += "Short : " + String.format("%,d", fundamentals.getSharesShort()) + "\n";
        details += "Short Percentage : " + String.format("%.2f", fundamentals.getShortPercentage()) + "%" + "\n";
        details += "52 W High : " + String.format("%.2f", fundamentals.getHigh()) + "\n";
        details += "52 W Low : " + String.format("%.2f", fundamentals.getLow());

        textFieldFloat.setText(details);
    }

    public void clear(){
        textFieldFloat.setText("");
    }
}
