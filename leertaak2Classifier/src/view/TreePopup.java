package view;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import classifier.DecisionTree;
import classifier.Node;

public class TreePopup extends JFrame {
	final String title = "";
	static GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0];
	Toolkit tk = Toolkit.getDefaultToolkit();
	
	public TreePopup(){
		
	    
	    setResizable(false);
	    setUndecorated(true);
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    device.setFullScreenWindow(this);
	    //JPanel screen = new JPanel(); //Screen is a JPanel.
	    this.setPreferredSize(new Dimension((int)tk.getScreenSize().width,(int)tk.getScreenSize().getHeight()));
	    //screen.setSize(getWidth(), getHeight());
	    //add(screen);
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
		
		panel.add(label);
		
		label.setBounds((panelWidth/2)-(labelWidth/2)+insets.left, insets.top, size.width, size.height);
		
		
	}
}
