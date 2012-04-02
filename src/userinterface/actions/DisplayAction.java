package userinterface.actions;

import java.util.List;

import commons.exceptions.TodoException;
import commons.tasks.Task;

import userinterface.CLILogic;
import userinterface.GraphicalInterface;

import logic.Logic;

/**
 * Action that displays the task list.
 * 
 */
public class DisplayAction extends UIAction {
	
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
	public DisplayAction(GraphicalInterface ui, Logic logic, String userInput) {
		super(ui, logic, false);
		_USERINPUT = userInput;
	}

	@Override
	public void apply() {
		List<Task> tasks;		
		try {
			tasks = _LOGIC.within(_USERINPUT);		
			
			_UI.clear();
			
			if(tasks.isEmpty()) {
				_UI.showFeedBackMessage(CLILogic.MESSAGE_DISPLAYED + _USERINPUT);
				_UI.displayFeedBackMessage(CLILogic.MESSAGE_DISPLAT_NO_RESULT);
			}
			else {
				String message = CLILogic.MESSAGE_DISPLAY_RESULTS.replace("%1s", Integer.toString(tasks.size()));
				if(!_USERINPUT.equals("")) {
					_UI.showFeedBackMessage(CLILogic.MESSAGE_DISPLAYED + _USERINPUT);
				}
				else {
					_UI.showFeedBackMessage("Displaying all tasks.");
				}
				_UI.displayTaskList(tasks, message);
			}
		} catch (TodoException e) {
			_UI.showFeedBackMessage(e.getMessage());
		}
	}
}