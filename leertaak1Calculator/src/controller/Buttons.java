package controller;

import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JOptionPane;

import multiformat.BinaryBase;
import multiformat.DecimalBase;
import multiformat.FixedPointFormat;
import multiformat.FloatingPointFormat;
import multiformat.FormatException;
import multiformat.HexBase;
import multiformat.NumberBaseException;
import multiformat.OctalBase;
import multiformat.RationalFormat;
import view.CalculatorFrame;


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
		set.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				try {
					CalculatorFrame.calculator.addOperand(CalculatorFrame.calculatorFrame.getIOPanel().getInput().getText().trim());
				} catch (FormatException e1) {
					new JOptionPane().showMessageDialog(CalculatorFrame.calculatorFrame, e1.getMessage(),"Error", JOptionPane.ERROR_MESSAGE);;
				} catch (NumberBaseException e1) {
					new JOptionPane().showMessageDialog(CalculatorFrame.calculatorFrame, e1.getMessage(),"Error", JOptionPane.ERROR_MESSAGE);;
				}
			}
			
		});
		//del TODO
		JButton del = new JButton("del");
		del.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				CalculatorFrame.calculator.delete();
			}
			
		});
		//bin TODO 
		JButton bin = new JButton("bin");
		bin.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				CalculatorFrame.calculator.setBase(new BinaryBase());;
			}
			
		});
		//oct
		JButton oct = new JButton("oct");
		oct.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				CalculatorFrame.calculator.setBase(new OctalBase());;
			}
			
		});
		//dec
		JButton dec = new JButton("dec");
		dec.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				CalculatorFrame.calculator.setBase(new DecimalBase());;
			}
			
		});
		//hex
		JButton hex = new JButton("hex");
		hex.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				CalculatorFrame.calculator.setBase(new HexBase());;
			}
			
		});
		//+
		JButton add = new JButton("+");
		add.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				CalculatorFrame.calculator.add();
			}
			
		});
		//-
		JButton minus = new JButton("-");
		minus.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				CalculatorFrame.calculator.subtract();;
			}
			
		});
		//*
		JButton mul = new JButton("*");
		mul.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				CalculatorFrame.calculator.multiply();;
			}
			
		});
		///
		JButton div = new JButton("/");
		div.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				CalculatorFrame.calculator.divide();
			}
			
		});
		//fixed
		JButton fixed = new JButton("fixed");
		fixed.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				CalculatorFrame.calculator.setFormat(new FixedPointFormat());
			}
			
		});
		//float
		JButton floating = new JButton("float");
		floating.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				CalculatorFrame.calculator.setFormat(new FloatingPointFormat());
			}
			
		});
		//rat
		JButton rat = new JButton("rat");
		rat.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				CalculatorFrame.calculator.setFormat(new RationalFormat());
			}
			
		});
		
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
