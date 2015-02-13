package view;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;








import multiformat.Calculator;
import controller.Buttons;

public class CalculatorFrame extends JFrame{

	private IOPanel ioPanel = new IOPanel();
	private Buttons buttons = new Buttons();
	public static CalculatorFrame calculatorFrame;
	public static final Calculator calculator = new Calculator();
	private CounterPanel counter = new CounterPanel();
	
	public CalculatorFrame(){
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		calculatorFrame = this;
		
		Container contentPane = this.getContentPane();
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.X_AXIS));
		// leftPanel
		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
		leftPanel.add(counter);
		leftPanel.add(ioPanel);
		
		contentPane.add(leftPanel);
		contentPane.add(buttons);
		
		//finish up frame
		this.setPreferredSize(new Dimension(300, 400));
		this.setVisible(true);
		this.pack();
		this.buttons.setOutput();
	}
	
	public IOPanel getIOPanel(){
		return ioPanel;
	}
	
	public CounterPanel getCounter(){
		return counter;
	}
	
	
}
