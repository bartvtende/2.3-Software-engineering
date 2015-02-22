package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Toolkit;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import classifier.DecisionTree;
import classifier.Node;

public class TreePopup extends JFrame {
	final String title = "";
	static GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0];
	Toolkit tk = Toolkit.getDefaultToolkit();
	
	public TreePopup(DecisionTree tree){
		
	    Node root = tree.getRoot();
		
	    setResizable(false);
	    setUndecorated(true);
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    device.setFullScreenWindow(this);
	    JPanel screen = new JPanel(); //Screen is a JPanel.
	    this.setPreferredSize(new Dimension((int)tk.getScreenSize().width,(int)tk.getScreenSize().getHeight()));
	    screen.setSize(getWidth(), getHeight());
	    add(screen);
	    printTree(root, screen);
	    setVisible(true);
	    Container cp = this.getContentPane();
	    System.out.println(cp.getWidth()+"()"+cp.getHeight());
	}
	
	private void printMiddleOfPanel(Node node, JPanel panel){
		int panelWidth = panel.getWidth();
		JLabel label = new JLabel(node.getLabel());
		Dimension size = label.getPreferredSize();
		int labelWidth = label.getWidth();
		Insets insets = panel.getInsets();
		
		panel.add(label, BorderLayout.NORTH);
		
		//label.setBounds((panelWidth/2)-(labelWidth/2)+insets.left, insets.top, size.width, size.height);	
	}
	
	private void printTree(Node root, JPanel mainPanel){
		Node node = root;
		if(!node.isLeaf()){
			
			printMiddleOfPanel(root, mainPanel);
			JPanel p1 = new JPanel();
			JPanel p2 = new JPanel();
			Dimension pSize = new Dimension(mainPanel.getWidth()/2,mainPanel.getHeight()-50);
			
			System.out.println(mainPanel.getWidth()+"()"+pSize.width+"[]"+mainPanel.getHeight()+"()"+pSize.height);
			
			
			p1.setSize(pSize);
			p2.setSize(pSize);
			
			JPanel center = new JPanel();
			center.setLayout(new BoxLayout(center, BoxLayout.X_AXIS));
			
			center.add(p1, BorderLayout.CENTER);
			center.add(p2, BorderLayout.CENTER);
			
			mainPanel.add(center, BorderLayout.CENTER);
			
			printTree(node.follow((String) node.getArcs().keySet().toArray()[0]), p1);
			printTree(node.follow((String) node.getArcs().keySet().toArray()[1]), p2);
			
		}
	}
}
