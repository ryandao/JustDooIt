package userinterface.actions;

import java.util.Date;
import java.util.List;

import logic.Logic;

import commons.tasks.Task;

import userinterface.CLILogic;
import userinterface.GraphicalInterface;


/**
 * Action that displays the to do list.
 *
 */
public class TodoAction extends UIAction {
	
	/**
	 * Sole constructor of DisplayAction.
	 * 
	 * @param ui
	 *            The user interface.
	 */
	public TodoAction(GraphicalInterface ui, Logic logic) {
		super(ui, logic, false);		
	}

	@Override
	public void apply() {
		
		_UI.clear();
		
		Date today = new Date();
		List<Task> mustDos = _LOGIC.getMustDoTasks(today);
		List<Task> shouldDos = _LOGIC.getShouldDoTasks(today);
		List<Task> missed = _LOGIC.getMissedTasks(today);
		_UI.showFeedBackMessage(CLILogic.MESSAGE_TODO);
		
		if(! mustDos.isEmpty()) {
			_UI.displayTaskList(mustDos, "MUST DO : ");
		}
		
		if(!shouldDos.isEmpty()) {
			_UI.displayTaskList(shouldDos, "SHOULD DO : ");
		}
		
		if(!missed.isEmpty()) {
			_UI.displayTaskList(missed, "MISSED : ");
		}
		
		if (mustDos.isEmpty() && shouldDos.isEmpty()) {
			
			_UI.displayFeedBackMessage(CLILogic.MESSAGE_NO_FUTUR_TASKS);
		}
			
	}
}
