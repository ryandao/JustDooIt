package userinterface.actions;

import userinterface.CLILogic;
import userinterface.GraphicalInterface;
import commons.exceptions.TodoException;
import commons.tasks.Task;

import logic.Logic;

/**
 * Action that modifies the content of a task.
 *
 */
public class ModifyAction extends UIAction {

	/** The task before being modified. */
	private Task _task;
	
	/** The task content before being modified */
	private String _content;
	
	/** The user input string */
	private final String _USERINPUT;
	
	/**
	 * Sole constructor of AddAction.
	 * 
	 * @param ui
	 *            The user interface.
	 * @param userInput
	 *            The user input containing the modification info.
	 */
	public ModifyAction(GraphicalInterface ui, Logic logic, String userInput) {
		super(ui, logic, true);
		_USERINPUT = userInput;	
		
		try {
			int id = CLILogic.getInputId(_USERINPUT);
			_task = logic.searchWithID(id);
			_content = _task.getContent();
		} catch (Exception e){
			this.fail();
			_UI.showFeedBackMessage(CLILogic.MESSAGE_MODIFY_WRONG_FORMAT);
		}
	}
	
	@Override
	public void apply() {
		try {						
			String activity = CLILogic.getWithoutId(_USERINPUT);
			_task = _LOGIC.modify(_task.getId(), activity);
			
			_UI.showFeedBackMessage("Task has been modified.");
			_UI.displayTask(_task, "Modified task: ");
			
		} catch(NumberFormatException e){
			this.fail();
			_UI.showFeedBackMessage(CLILogic.MESSAGE_MODIFY_WRONG_ID_FORMAT);
		} catch (IndexOutOfBoundsException e){
			this.fail();
			_UI.showFeedBackMessage(CLILogic.MESSAGE_MODIFY_NO_ACTIVITY);
		} catch(TodoException e) {
			this.fail();
			_UI.showFeedBackMessage(e.getMessage());
		}
	}

	@Override
	public void unapply() {
		
		if(_task == null) {
			return;
		}
		
		int id = _task.getId();	
		
		_UI.showFeedBackMessage("Task has been restored to its previous state.");	
		try {
			_LOGIC.modify(id, _content);
		} catch (TodoException e) {
			this.fail();
			_UI.showFeedBackMessage(e.getMessage());
		}
		_UI.displayTask(_task, "Restored task :");
	}
}
