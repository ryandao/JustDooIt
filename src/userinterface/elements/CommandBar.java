package userinterface.elements;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * The CommandBar contains the CommandField for the user to input commands, the Enter button to
 * validate the commands entered. The Undo button to undo the last command or action done by the user.
 * The Redo button to redo the last command of action which has been undone.
 * 
 */
public class CommandBar extends JPanel{
	
	private static final long serialVersionUID = 1L;
	
	public CommandField commandField = new CommandField();
	
	Icon enterIcon = new ImageIcon("ressources/enterButton.gif");
	Icon undoIcon = new ImageIcon("ressources/undoButton.gif");
	Icon redoIcon = new ImageIcon("ressources/redoButton.gif");
	
	public JButton enterButton = new JButton(enterIcon);
	public JButton undoButton = new JButton(undoIcon);
	public JButton redoButton = new JButton(redoIcon);
	
	Dimension minSize = new Dimension(5, 30);
	Dimension prefSize = new Dimension(5,30);
	Dimension maxSize = new Dimension(Short.MAX_VALUE, 30);
	
	public CommandBar(){
		this.setBackground(Color.BLACK);
		this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		
		this.add(new Box.Filler(minSize, prefSize, maxSize));
		this.add(commandField);
		
		enterButton.setPreferredSize(new Dimension(enterIcon.getIconWidth(), enterIcon.getIconHeight()));
		enterButton.setMaximumSize(new Dimension(enterIcon.getIconWidth(), enterIcon.getIconHeight()));
		this.add(enterButton);
		
		undoButton.setPreferredSize(new Dimension(undoIcon.getIconWidth(),undoIcon.getIconHeight()));
		undoButton.setMaximumSize(new Dimension(undoIcon.getIconWidth(),undoIcon.getIconHeight()));
		this.add(undoButton);
		
		redoButton.setPreferredSize(new Dimension(redoIcon.getIconWidth(),redoIcon.getIconHeight()));
		redoButton.setMaximumSize(new Dimension(redoIcon.getIconWidth(),redoIcon.getIconHeight()));
		this.add(redoButton);
		
		this.add(Box.createHorizontalGlue());
		this.add(new Box.Filler(minSize, prefSize, maxSize));
		
		undoButton.setActionCommand("undo");
		redoButton.setActionCommand("redo");
			
	}
	
	public void setCommandField(String text){
		commandField.setText(text);
	}
	
}
