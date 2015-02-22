package view;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import classifier.DecisionTree;

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
}
