package userinterface.actions;

import commons.exceptions.TodoException;
import commons.tasks.Task;
import commons.timeframes.TimeFrame;

import userinterface.CLILogic;
import userinterface.GraphicalInterface;
import logic.Logic;


/**
 * Action that deletes a task from the task list.
 * 
 */
public class DeleteAction extends UIAction {
	
	private final String _USERINPUT;		

	/** The deleted task object. */
	private Task _task = null;		
	
	/**
	 * Sole constructor of DeleteAction.
	 * 
	 * @param ui
	 *            The user interface.
	 * @param task_id
	 *            The id of the task to delete.
	 */
	public DeleteAction(GraphicalInterface ui, Logic logic, String userInput) {
		super(ui, logic, true);
		_USERINPUT = userInput;
	}

	@Override
	public void apply() {
		try{
			_UI.clear();
			int task_id = CLILogic.getInputId(_USERINPUT);
			_task = _LOGIC.delete(task_id);
			String message = CLILogic.MESSAGE_DELETED.replace("%1s", Integer.toString(task_id));
			_UI.showFeedBackMessage(message);
			_UI.displayFeedBackMessage("\"" + _task.getContent() + "\" has been deleted.");
		} catch (NumberFormatException e){
			this.fail();
			_UI.showFeedBackMessage(CLILogic.MESSAGE_DELETE_WRONG_FORMAT);
		} catch (IndexOutOfBoundsException e) {
			this.fail();
			_UI.showFeedBackMessage(CLILogic.MESSAGE_DELETE_NO_ID);
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
		
		try {
			_LOGIC.add(_task);
			_UI.showFeedBackMessage("Readded task: " + _task.getContent() + " to be done " + _task.getTimeFrame().toString());
			if (_task.getTimeFrame().getType() != TimeFrame.WHENEVER && _task.getTimeFrame().getType() != TimeFrame.FROM){
				_UI.displayTask(_task, "Retrieved Task: ");
			}
			CLILogic.getInstance().displayTaskOn(_task);
		} catch(TodoException e) {
			this.fail();
			_UI.showFeedBackMessage(e.getMessage());
		}
	}

}
