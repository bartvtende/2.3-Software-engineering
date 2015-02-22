package controller;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

import view.Frame;
import view.Pick;
import classifier.Node;

public class InputPanel extends JPanel {

	private static boolean picked=false;
	
	private static Node node;
	public InputPanel(Node tree){
		node = tree;
		if(node.isLeaf()){
			picked =true;
			System.out.println(node.isLeaf());
			Frame.frame.replaceCenter(new Pick(node.getLabel()));
			Frame.frame.getQuestion().setText("");
		}
		else{
			Frame.frame.getQuestion().setText("Heeft de auto "+node.getLabel()+"?");
			System.out.println(node.isLeaf());
			createButtons();
		}
	}
	/*
	private void buttonSetup(){
		ja.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO add body
			}
			
		});
		
		nee.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO add body
				
			}});
		
	}
	*/
	private void createButtons(){
		System.out.println("createButtons");
		for(final String key:node.getArcs().keySet()){
			String buttonString;
			if(key.equals("1")){
				buttonString = "Ja";
			}
			else if(key.equals("0")){
				buttonString = "Nee";
			}
			else{
				buttonString = key;
			}
			JButton button = new JButton(buttonString);
			button.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					node = node.follow(key);
					InputPanel input = new InputPanel(node);
					if(!picked){
					Frame.frame.replaceCenter(input);
					}
				}});
			this.add(button);
			this.add(Box.createRigidArea(new Dimension(5,0)));
		}
	}
}
