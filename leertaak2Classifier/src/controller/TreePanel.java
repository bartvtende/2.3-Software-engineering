package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import view.TreeView;

public class TreePanel extends JPanel {

	private JButton button = new JButton("Toggle TreeView");
	private TreeView view = new TreeView();

	public TreePanel() {
		this.add(button);
		
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// Toggle TreeView or something
			}
		});
	}

}
