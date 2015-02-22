package view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.JLabel;

import classifier.DecisionTree;
import classifier.Node;
import controller.InputPanel;

public class Frame extends JFrame {

	private Container contentPane;
	private JLabel question = new JLabel();
	private InputPanel input;
	public static Frame frame;
	
	public Frame(DecisionTree tree){		
		frame = this;
		input = new InputPanel(tree.getRoot());
		
		
		contentPane = this.getContentPane();
		contentPane.setLayout(new BorderLayout());
		contentPane.add(question, BorderLayout.NORTH);
		contentPane.add(input, BorderLayout.CENTER);
		
		
	}
	
	private void removeCenter(){
		contentPane.remove(((BorderLayout)contentPane.getLayout()).getLayoutComponent(BorderLayout.CENTER));
	}
	
	public void replaceCenter(Component comp){
		
		removeCenter();
		contentPane.add(comp, BorderLayout.CENTER);
		this.revalidate();
	}
	
	public JLabel getQuestion(){
		return question;
	}
}
