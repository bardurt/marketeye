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

        String details = "";
        details += "Float : " + String.format("%,d", fundamentals.getSharesFloat()) + ", ";
        details += "Short : " + String.format("%,d", fundamentals.getSharesShort()) + ", ";
        details += "Short Percentage : " + String.format("%.2f", fundamentals.getShortPercentage()) + "%" + ", ";
        details += "52 W High : " + String.format("%.2f", fundamentals.getHigh()) + ", ";
        details += "52 W Low : " + String.format("%.2f", fundamentals.getLow()) + ", ";
        details += "Avg vol : " + String.format("%,d", fundamentals.getAvgVol());

        textFieldFloat.setText(details);
    }

    public void clear(){
        textFieldFloat.setText("");
    }
}
