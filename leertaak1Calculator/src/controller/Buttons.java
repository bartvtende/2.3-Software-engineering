package controller;

import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

public class Buttons extends JPanel {

	public Buttons(){
		
		this.setLayout(new GridLayout(0,1));
		
		//set/delpanel
		JPanel setDelPanel = new JPanel();
		setDelPanel.setLayout(new GridLayout(1,2));
		
		//Centerpanel
		JPanel center = new JPanel();
		center.setLayout(new GridLayout(1,2));
		
		//NumberBasePanel
		JPanel numberBasePanel = new JPanel();
		numberBasePanel.setLayout(new GridLayout(0,1));
		
		//operatorPanel
		JPanel operatorPanel = new JPanel();
		operatorPanel.setLayout(new GridLayout(0,1));
		
		//formatPanel
		JPanel formatPanel = new JPanel();
		formatPanel.setLayout(new GridLayout(0,1));
		
		//set
		JButton set = new JButton("set");
		//del
		JButton del = new JButton("del");
		//bin
		JButton bin = new JButton("bin");
		//oct
		JButton oct = new JButton("oct");
		//dec
		JButton dec = new JButton("dec");
		//hex
		JButton hex = new JButton("hex");
		//+
		JButton add = new JButton("+");
		//-
		JButton minus = new JButton("-");
		//*
		JButton mul = new JButton("*");
		///
		JButton div = new JButton("/");
		//fixed
		JButton fixed = new JButton("fixed");
		//float
		JButton floating = new JButton("float");
		//rat
		JButton rat = new JButton("rat");
		
		//fill setDelPanel
		setDelPanel.add(set);
		setDelPanel.add(del);
		
		//fill NumberBasePanel
		numberBasePanel.add(bin);
		numberBasePanel.add(oct);
		numberBasePanel.add(dec);
		numberBasePanel.add(hex);
		
		//fill operatorPanel
		operatorPanel.add(add);
		operatorPanel.add(minus);
		operatorPanel.add(mul);
		operatorPanel.add(div);
		
		//fill centerPanel
		center.add(numberBasePanel);
		center.add(operatorPanel);
		
		//fill formatPanel
		formatPanel.add(fixed);
		formatPanel.add(floating);
		formatPanel.add(rat);
		
		//fill this
		this.add(setDelPanel);
		this.add(center);
		this.add(formatPanel);
		
	}
}
