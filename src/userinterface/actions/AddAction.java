package userinterface.actions;

import commons.exceptions.ParserException;
import commons.exceptions.TodoException;
import commons.tasks.Task;
import commons.timeframes.TimeFrame;

import userinterface.CLILogic;
import userinterface.GraphicalInterface;
import logic.Logic;


/**
 * Action that adds a task to the task list.
 * 
 */
public class AddAction extends UIAction {

	/** The added task. */
	private Task _task;
	
	/** The user input string */
	private final String _USERINPUT;		
	
	/**
	 * Sole constructor of AddAction.
	 * 
	 * @param ui
	 *            The user interface.
	 * @param userInput
	 *            The user input containing the task info to add.
	 */
	public AddAction(GraphicalInterface ui, Logic logic, String userInput) {
		super(ui, logic, true);
		_USERINPUT = userInput;		
	}

	@Override
	public void apply() {		
		try {						
			_task = _LOGIC.add(_USERINPUT);						
			
			_UI.showFeedBackMessage("New task added.");
			
			_UI.clear();
			
			_UI.displayTask(_task, "Added Task:");
			
			if (_task.getTimeFrame().getType() != TimeFrame.WHENEVER && _task.getTimeFrame().getType() != TimeFrame.FROM){
				CLILogic.getInstance().displayTaskOn(_task);
			}
			
		} catch (ParserException e) {
			_UI.showFeedBackMessage(CLILogic.MESSAGE_ADD_WRONG_FORMAT);
			this.fail();
		} catch (TodoException e) {
			this.fail();
			_UI.showFeedBackMessage(e.getMessage());
		}
	}

	@Override
	public void unapply() {
		
		if(_task == null) {
			return;
		}
		
		int task_id = _task.getId();
		
		_UI.clear();
		
		try {
			_LOGIC.delete(task_id);
		} catch (TodoException e) {
			this.fail();
			_UI.showFeedBackMessage(e.getMessage());
		}
		
		_UI.showFeedBackMessage("Removed task: \"" + _task.getContent() + "\"");
	}
}
