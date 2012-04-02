package userinterface.elements;

import java.awt.Color;
import java.awt.ComponentOrientation;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

/**
 * TasksWindow contains the taskRows to be displayed as well as messageLabels and 
 * the displayArea when the help file is displayed.
 * 
 */
public class TasksWindow extends JPanel{
	
	private static final long serialVersionUID = 1L;

	public TasksWindow(){
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		setBackground(Color.WHITE);
		setVisible(true);
		
	}

}
