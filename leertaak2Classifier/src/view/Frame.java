package view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.JLabel;

import classifier.DecisionTree;
import controller.InputPanel;
import controller.TreePanel;

public class Frame extends JFrame {

	private Container contentPane;
	private JLabel question = new JLabel();
	private InputPanel input;
	private TreePanel treePanel;
	public static Frame frame;

	public Frame(DecisionTree tree) {
		frame = this;
		treePanel = new TreePanel();
		input = new InputPanel(tree.getRoot());

		contentPane = this.getContentPane();
		contentPane.setLayout(new BorderLayout());
		contentPane.add(treePanel, BorderLayout.NORTH);
		contentPane.add(question, BorderLayout.CENTER);
		contentPane.add(input, BorderLayout.SOUTH);
	}

	private void removeCenter() {
		contentPane.remove(((BorderLayout) contentPane.getLayout())
				.getLayoutComponent(BorderLayout.SOUTH));
	}

	public void replaceCenter(Component comp) {
		removeCenter();
		contentPane.add(comp, BorderLayout.SOUTH);
		this.revalidate();
	}

	public JLabel getQuestion() {
		return question;
	}
}
