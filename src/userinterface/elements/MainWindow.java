package userinterface.elements;

import java.awt.ComponentOrientation;
import java.awt.Container;

import javax.swing.BoxLayout;
import javax.swing.JFrame;

/**
 * The main window which contains all the other Components.
 * 
 */
public class MainWindow extends JFrame {

	private static final long serialVersionUID = 1L;

	/**
	 * Sole constructor of MainWindow.
	 */
	public MainWindow() {
		super();
		setSize(835, 400);
		setLocationRelativeTo(null); // center the window in the middle of the
										// screen
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container pane = getContentPane();
		pane.setLayout(new BoxLayout(pane, BoxLayout.PAGE_AXIS));
		pane.applyComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
	}

}
