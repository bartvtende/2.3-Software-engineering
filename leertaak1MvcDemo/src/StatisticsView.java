import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;


public class StatisticsView extends JPanel implements ActionListener {

	private JTextField totalThrows;
	private JTextArea historyThrows;
    private JScrollPane scrollPanel;
    DobbelsteenModel d;
	
	public StatisticsView()
	{
		totalThrows = new JTextField();
        historyThrows = new JTextArea(8,8);
        scrollPanel = new JScrollPane(historyThrows);
        scrollPanel.setPreferredSize(new Dimension(150, 150));
	    this.setLayout(new GridLayout(2,1));
	    this.add(totalThrows);
	    this.add(scrollPanel);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
	    d = (DobbelsteenModel) e.getSource();
	    ArrayList<String> throwHistory = d.getWaarden();
	    
	    String amountOfThrows = Integer.toString(throwHistory.size());
	    totalThrows.setText(amountOfThrows + " worpen");
	    
	    String text = "";
	    int index = 0;
	    for(String item : throwHistory) {
	    	index++;
	    	text += index + ": " + item + " keer \n";
	    }
		historyThrows.setText(text);
	}

}
