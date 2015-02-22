package view;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class Pick extends JPanel {
	private JLabel text = new JLabel();
	
	public Pick(String pick){
		text.setText("U heeft gekozen voor "+ pick);
		
		this.add(text);
		System.out.println("Picked");
	}
}
