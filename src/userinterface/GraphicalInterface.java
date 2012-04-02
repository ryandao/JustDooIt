package userinterface;

import userinterface.elements.CommandBar;
import userinterface.elements.DisplayArea;
import userinterface.elements.FeedBackBar;
import userinterface.elements.MainWindow;
import userinterface.elements.TaskRow;
import userinterface.elements.TasksWindow;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

import commons.tasks.Task;
import commons.timeframes.TimeFrame;
	
public class GraphicalInterface {
	private static final Color WHITE = Color.WHITE;
	private static final Color GRAY_YELLOW = new Color((float)0.95,(float)0.95,(float)0.80);
	

	/** The command bar containing the input text field and the enter, undo and redo buttons. */
	private static CommandBar _commandBar= new CommandBar();
	
	/** displays feedback to the user */
	private static FeedBackBar _feedBackBar = new FeedBackBar();
	
	/** The output area used for displaying help file. */
	private DisplayArea _displayArea = new DisplayArea();

	/** The main window. */
	private MainWindow _mainWindow = new MainWindow();
	
	/** Window in which the clickable text are displayed. */
	private TasksWindow _tasksWindow = new TasksWindow();

	
	/**
	 * Sole constructor of GraphicalInterface.
	 * 
	 * @param userFile
	 *            The file we work on.
	 * @param commands.
	 *            The map of available commands.
	 */
	public GraphicalInterface() {
		
		_mainWindow.add(_commandBar);	
		_mainWindow.setTitle("JustDooIt");
		_mainWindow.setBackground(Color.BLACK);
		_mainWindow.add(_feedBackBar);
		_commandBar.commandField.setFocusable(true);
		JScrollPane scrollArea = new JScrollPane(_tasksWindow,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

		_mainWindow.add(scrollArea);
		
		setListeners();
		
	}
	
	/**
	 * Set the listeners for the Enter, Undo and Redo buttons.
	 */
	private void setListeners(){
				//Pressing the Enter key from keyboard
				_commandBar.commandField.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						executeAction(e.getActionCommand());
						_commandBar.setCommandField("");
					}
				});
				
				//Enter Button
				_commandBar.enterButton.setActionCommand("enter");
				_commandBar.enterButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						executeAction(_commandBar.commandField.getText());		
						_commandBar.setCommandField("");
					}
				});
				
				//UndoButton
				_commandBar.undoButton.setActionCommand("undo");
				_commandBar.undoButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						_commandBar.setCommandField("undo");
						executeAction(_commandBar.commandField.getText());	
						_commandBar.setCommandField("");
					}
				});
				
				//RedoButton
				_commandBar.redoButton.setActionCommand("redo");
				_commandBar.redoButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						_commandBar.setCommandField("redo");
						executeAction(_commandBar.commandField.getText());		
						_commandBar.setCommandField("");
					}
				});
	}
	
	/**
	 * Calls the executeUserInputCommand method
	 * @param userInput
	 */
	private void executeAction(String userInput) {
		_tasksWindow.repaint();
		CLILogic.getInstance().executeUserInputCommand(userInput);
	}
	
	/**
	 * Set the commandField to the String command.
	 * @param command
	 */
	public static void setCommandField(String command){
		_commandBar.setCommandField(command);
	}
	
	/**
	 * showFeedBackMessage set the text of the feedBackBar, the black bar under the command bar.
	 * @param message
	 */
	public void showFeedBackMessage(String message){
		_feedBackBar.setMessage(message);
	}
	
	/**
	 * Displays the String message in the taskWindow with the given String message
	 * @param message
	 */
	public void displayFeedBackMessage(String message) {	
		_tasksWindow.add(createMessageContainer(message));
		_tasksWindow.validate();
	}
	
	/**
	 * creates a container to contain a message JLabel used for display. Used to display the title of a returned list of tasks.
	 * @param message
	 * @return
	 */
	public Container createMessageContainer(String message){
		Container container = new Container();
		container.setLayout(new BoxLayout(container, BoxLayout.LINE_AXIS));
		JLabel text = new JLabel(message);
		
		container.add(Box.createRigidArea(new Dimension(20,0)));
		container.add(text);
		container.add(Box.createHorizontalGlue());
		
		return container;
	}
	
	/**
	 * Displays a single task, along with the String message as a description for the task in _tasksWindow.
	 * @param task
	 * @param message
	 */
	public void displayTask(Task task, String message){
		_tasksWindow.removeAll();
		_tasksWindow.repaint();
		if (message != null){
			_tasksWindow.add(createMessageContainer(message));
		}
		
		int id = task.getId();
		boolean status = task.getStatus();
		TimeFrame time = task.getTimeFrame();
		String content = task.getContent();
		TaskRow taskRow = new TaskRow(id, status, time, content, GRAY_YELLOW);
		
		_tasksWindow.add(taskRow.getTaskRow());
		_tasksWindow.validate();
		
	}
	
	/**
	 * Displays a list of tasks, along with the String message as a description for the task in _tasksWindow.
	 * The tasks from the list are stored in a task array, taskRowArray. The taskRows from the taskRowArray are
	 * then added to the _taskWindow one at a time.
	 * @param task
	 * 				List of tasks to be displayed
	 * @param message
	 * 				Description of the displayed list
	 */
	public void displayTaskList(List<Task> task, String message){
		
		if (message != null){
			Container container = createMessageContainer(message);
			//_tasksWindow.setFont(new Font(Font.MONOSPACED, Font.BOLD, 30));
			_tasksWindow.add(container);
		}
		
		int numberOfTasks = task.size();
		TaskRow[] taskRowArray = new TaskRow[numberOfTasks];
		
		for(int i = 0; i < numberOfTasks; i++){
			int id = task.get(i).getId();
			boolean status = task.get(i).getStatus();
			TimeFrame time = task.get(i).getTimeFrame();
			String content = task.get(i).getContent();
			if (i % 2 == 0){
				taskRowArray[i] = new TaskRow(id, status, time, content, GRAY_YELLOW);
			}
			else{
				taskRowArray[i] = new TaskRow(id, status, time, content, WHITE);
			}
			_tasksWindow.add(taskRowArray[i].getTaskRow());
		}
		_tasksWindow.validate();
	}
	
	/**
	 * Sets the text from the help file in a JTextArea, _displayArea. Then adds _displayArea to _tasksWindow.
	 * @param text
	 * 				The content of the help file.
	 */
	public void displayHelpFile(String text){
		_tasksWindow.removeAll();
		_tasksWindow.repaint();
			
		_displayArea.setText(text);
		_tasksWindow.add(_displayArea);
		_tasksWindow.validate();
	}
	
	/**
	 * Makes _mainWindow visible
	 */
	public void start() {
		_mainWindow.setVisible(true);
	}
	
	/**
	 * Removes all the components from the _taskWindow so that other components can be added without conflicting
	 */
	public void clear() {
		_tasksWindow.removeAll();
		_tasksWindow.repaint();
	}
}
