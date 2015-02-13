package view;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class IOPanel extends JPanel {

	private JTextField input = new JTextField();
	private JTextField output = new JTextField();
	
	public IOPanel(){
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.add(input);
		output.setEditable(false);
		this.add(output);
	}
}
