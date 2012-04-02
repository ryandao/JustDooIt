package userinterface.actions;

import java.util.List;

import commons.tasks.Task;

import userinterface.CLILogic;
import userinterface.GraphicalInterface;

import logic.Logic;


/**
 * Action that searches for tasks in the task list.
 *
 */
public class SearchAction extends UIAction {
		
	/** The user input string. */
	private final String _USERINPUT;
	
	/**
	 * The sole constructor of SearchAction.
	 * 
	 * @param ui
	 *            The user interface.
	 * @param userInput
	 *            The user input string (i.e the search query).
	 */
	public SearchAction(GraphicalInterface ui, Logic logic, String userInput) {
		super(ui, logic, false);
		_USERINPUT = userInput;
	}

	@Override
	public void apply() {
		
		_UI.clear();
		
		if(_USERINPUT.split("\\s+")[0].isEmpty()){
			_UI.showFeedBackMessage(CLILogic.MESSAGE_SEARCH_NO_KEYWORD);
		}
		else{
			List<Task> tasks = _LOGIC.search(_USERINPUT);
			
			if(tasks.isEmpty()) {
				_UI.showFeedBackMessage(CLILogic.MESSAGE_SEARCHED + _USERINPUT);
				_UI.displayFeedBackMessage(CLILogic.MESSAGE_SEARCHED_NO_RESULT);
			}
			else {
				_UI.showFeedBackMessage(CLILogic.MESSAGE_SEARCH_RESULT + _USERINPUT);
				_UI.displayTaskList(tasks, CLILogic.MESSAGE_SEARCHED);
			}
		}
	}

	
}