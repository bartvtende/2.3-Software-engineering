package view;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class CounterPanel extends JPanel {

	private int counter = 0;
	private JLabel label = new JLabel("Aantal Berekeningen: "); 
	private JTextField display = new JTextField();
	
	public CounterPanel(){
		display.setEditable(false);
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.add(label);
		this.add(display);
		updateDisplay();
	}
	public void counterUp(int amount){
		counter = counter + amount;
	}
	
	public void updateDisplay(){
		getDisplay().setText(Integer.toString(counter));
	}
	
	public JTextField getDisplay() {
		return display;
	}
	
}
