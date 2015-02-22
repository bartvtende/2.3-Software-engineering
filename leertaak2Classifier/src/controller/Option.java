package controller;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import view.Frame;
import view.Pick;

public class Option extends JPanel {

	private JButton button = new JButton("Kies");
	private JLabel optionText = new JLabel();
	
	public Option(String text){
		optionText.setText(text);
		this.setLayout(new BorderLayout());
		this.add(optionText, BorderLayout.CENTER);
		this.add(button, BorderLayout.EAST);
	}
	
	private void buttonSetup(){
		button.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				Frame.frame.replaceCenter(new Pick(optionText.getText()));
			}
		});
	}
	
	
}
