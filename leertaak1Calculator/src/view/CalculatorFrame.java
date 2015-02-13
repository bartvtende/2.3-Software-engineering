package view;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;







import controller.Buttons;

public class CalculatorFrame extends JFrame{

	private Buttons buttons = new Buttons();
	private IOPanel ioPanel = new IOPanel();
	
	
	public CalculatorFrame(){
		Container contentPane = this.getContentPane();
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.X_AXIS));
 
		contentPane.add(ioPanel);
		contentPane.add(buttons);
		
		//finish up frame
		this.setVisible(true);
		this.pack();
	}
	
	
}
