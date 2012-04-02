package userinterface.elements;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Used to display feed back message to the user.
 * 
 */
public class FeedBackBar extends JPanel {
	
	private static final long serialVersionUID = 1L;

	JLabel messageLabel = new JLabel("Hello, welcome to JustDooIt :)");
	JTextField messagetextField = new JTextField("Hello, welcome to JustDooIt :)");
	
	public FeedBackBar(){
		this.setBackground(Color.BLACK);
		messageLabel.setForeground(Color.WHITE);
		this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		this.add(Box.createRigidArea(new Dimension(20,15)));
		this.add(messageLabel);
		this.add(Box.createHorizontalGlue());
		
	}
	
	public void setMessage(String text){
		messageLabel.setText(text);
	}

	public String getMessage(){
		return messageLabel.getText();
	}
}
