import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JApplet;

public class DobbelsteenMVC extends JApplet
{
	DobbelsteenModel model;             // Model
	TekstView tekstView;              	// View
	DobbelsteenView dobbelsteenView;  	// View
	StatisticsView statisticsView;    	// View
	DobbelsteenController controller;   // Controller
	
	public void init()
	{
<<<<<<< HEAD
		resize(250,200);
=======
		resize(350,400);
>>>>>>> 90ff733d02cefcb98bbfbe70def9ad8768f5005e
        
		// Maak het model
		model = new DobbelsteenModel();
        
        // Maak de controller en geef hem het model
		controller = new DobbelsteenController(model);
        controller.setBackground(Color.cyan);
        getContentPane().add(controller,BorderLayout.NORTH);
        
        // Maak de views
        dobbelsteenView = new DobbelsteenView(Color.red);
        dobbelsteenView.setBackground(Color.black);
        getContentPane().add(dobbelsteenView,BorderLayout.CENTER);
        tekstView = new TekstView();
        tekstView.setBackground(Color.green);
        getContentPane().add(tekstView,BorderLayout.SOUTH);
        statisticsView = new StatisticsView();
        statisticsView.setBackground(Color.yellow);
        getContentPane().add(statisticsView, BorderLayout.EAST);
        
        // Registreer de views bij het model
        model.addActionListener(tekstView);
        model.addActionListener(dobbelsteenView);
        model.addActionListener(statisticsView);
        
        // Eerste worp
        model.gooi();
	}
}
