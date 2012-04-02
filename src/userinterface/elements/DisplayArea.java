package userinterface.elements;

import java.awt.Font;
import java.awt.Insets;

import javax.swing.JTextArea;

/**
 * JTextArea which is used to display the help file's text.
 * 
 */
public class DisplayArea extends JTextArea {

	private static final long serialVersionUID = 1L;

	/**
	 * Sole constructor of DisplayArea.
	 */
	public DisplayArea() {
		super();
		setEditable(false);
		setLineWrap(false);
		setWrapStyleWord(false);
		setOpaque(false);
		setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
		setMargin(new Insets(20, 20, 20, 20));
		setRows(3);
	}
}
