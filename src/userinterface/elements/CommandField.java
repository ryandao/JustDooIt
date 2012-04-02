package userinterface.elements;

import java.awt.Dimension;

import javax.swing.JTextField;

/**
 * JTextField in which the user can enter his command.
 * 
 */
public class CommandField extends JTextField {

	private static final long serialVersionUID = 1L;

	/**
	 * Sole constructor of CommandField.
	 */
	public CommandField() {
		super();
		setMinimumSize(new Dimension(200, 20));
		setPreferredSize(new Dimension(200,20));
		setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
	}
}
