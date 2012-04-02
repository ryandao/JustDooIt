package userinterface.actions;

import commons.exceptions.TodoException;
import commons.tasks.Task;

import userinterface.CLILogic;
import userinterface.GraphicalInterface;
import logic.Logic;

/**
 * Action that marks a task as not done.
 *
 */
public class NotDoneAction extends UIAction {
	
	/** The user input string */
	private String _USERINPUT;
	
	/**
	 * Sole constructor of DisplayAction.
	 * 
	 * @param ui
	 *            The user interface.
	 * @param userFile
	 *            The user file.
	 */
	public NotDoneAction(GraphicalInterface ui, Logic logic, String userInput) {
		super(ui, logic, false);
		_USERINPUT = userInput;
	}

	@Override
	public void apply() {
		try {
			int id = CLILogic.getInputId(_USERINPUT);
			markTaskDone(id, false);			
			
		} catch(NumberFormatException e) {
			_UI.showFeedBackMessage(CLILogic.MESSAGE_NOT_DONE_WRONG_FORMAT);
		}
	}
	
	public void markTaskDone(int taskId, boolean done){
		try{
			String message;
			Task task = _LOGIC.searchWithID(taskId);
			if(task.getStatus() == false){			
				message = CLILogic.MESSAGE_ALREADY_NOT_DONE.replace("%1s", Integer.toString(taskId));
				_UI.showFeedBackMessage(message);
			}
			else{
				_LOGIC.setStatus(taskId, done);
				message = CLILogic.MESSAGE_NOT_DONE.replace("%1s", Integer.toString(taskId));				
				
			}
			_UI.showFeedBackMessage(message);			
		} catch (TodoException e) {
			_UI.showFeedBackMessage(e.getMessage());
		}
	}
}
