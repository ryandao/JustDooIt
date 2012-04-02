package userinterface.elements;

import java.awt.Color;
import java.awt.Component;

import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import userinterface.CLILogic;
import userinterface.GraphicalInterface;

import commons.timeframes.TimeFrame;

/**
 * A TaskRow consist of a JLabel for the task Id, a JCheckBox for the task's status(done or not done),
 * a JLabel for the date of the Task and a JLabel for the content of the task(task description)
 * The idLabel is not clickable while the statusCheckBox, dateLabel and contentLabel are clickable.
 * Clicking on statusCheckBox will mark the corresponding task as done or not done.
 * Clicking on dateLabel sets the text of commandField to "reschedule [corresponding task Id] ".
 * Clicking on contentLabel will sets the text of commandField to "modify [corresponding task Id]". 
 * 
 */
public class TaskRow extends JPanel implements ItemListener, MouseListener{

	private static final long serialVersionUID = 1L;
	
	JLabel idLabel = new JLabel();
	JLabel statusLabel = new JLabel();
	JCheckBox statusCheckBox= new JCheckBox();
	JLabel dateLabel = new JLabel();
	JLabel contentLabel = new JLabel();
	JPanel panel = new JPanel();
	
	boolean done;	
	int taskId;
	Color highlight = new Color((float)0.20,(float)0.40,(float)1.00);
	
	public TaskRow(int id, boolean status, TimeFrame date, String content, Color color){
		panel.setBackground(color);
		done = status;
		
		idLabel.setText("("+Integer.toString(id)+")");
		taskId = id;
		if(status){
			statusCheckBox.setSelected(true);
		}
		else{
			statusCheckBox.setSelected(false);
		}
		dateLabel.setText(" " + date.toString());
		contentLabel.setText(": " + content);
				
		panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
		panel.add(Box.createRigidArea(new Dimension(20,0)));
		
		panel.add(idLabel);
		idLabel.setPreferredSize(new Dimension(30,0));
		idLabel.setForeground(Color.BLACK);
		
		panel.add(Box.createRigidArea(new Dimension(10,0)));
		
		panel.add(statusCheckBox);
		statusCheckBox.setBackground(color);
		
		panel.add(Box.createRigidArea(new Dimension(10,0)));
		
		panel.add(dateLabel);
		dateLabel.setForeground(Color.BLACK);
		
		panel.add(Box.createRigidArea(new Dimension(10,0)));
		panel.add(contentLabel);
		
		contentLabel.setForeground(Color.BLACK);
		panel.add(Box.createHorizontalGlue());
		
		statusCheckBox.addItemListener(this);
		dateLabel.addMouseListener(this);
		contentLabel.addMouseListener(this);
		
	}
	
	public void itemStateChanged(ItemEvent e) {
		if (!done){
			CLILogic.getInstance().executeUserInputCommand("done " + taskId);
			done = true;
		}
		else {
			CLILogic.getInstance().executeUserInputCommand("notdone " + taskId);
			done = false;
		}

	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		Component clickedComponent = e.getComponent();
		JLabel clickedLabel = (JLabel) clickedComponent;		
		String command = determineCommand(clickedLabel.getText(), taskId);
		GraphicalInterface.setCommandField(command);
	}
	
	public static String determineCommand(String clickedlbl, int id){
		char identifierChar = clickedlbl.charAt(0);
		String command = "";
		switch(identifierChar){
		case ' ':
			command = "reschedule " + id + " ";
			break;
		case ':':
			command = "modify "+ id + " ";
		}
		return command;
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		Component clickedComponent = e.getComponent();
		JLabel clickedLabel = (JLabel) clickedComponent;
		clickedLabel.setForeground(highlight);
	}

	@Override
	public void mouseExited(MouseEvent e) {
		Component clickedComponent = e.getComponent();
		JLabel clickedLabel = (JLabel) clickedComponent;
		clickedLabel.setForeground(Color.BLACK);
	}

	
	public JPanel getTaskRow(){
		return panel;
	}
	
	public JLabel getIdLabel(){
		return idLabel;
	}
	
	public JLabel getStatusLabel(){
		return statusLabel;
	}
	
	public JLabel getDateLabel(){
		return dateLabel;
	}
	
	public JLabel getContentLabel(){
		return contentLabel;
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		
	}

	

}
