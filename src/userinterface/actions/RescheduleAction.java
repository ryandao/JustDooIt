package userinterface.actions;

import userinterface.CLILogic;
import userinterface.GraphicalInterface;
import commons.exceptions.ParserException;
import commons.exceptions.TodoException;
import commons.tasks.Task;
import commons.timeframes.TimeFrame;

import logic.Logic;


/**
 * Action that reschedules a task to a give time.
 *
 */
public class RescheduleAction extends UIAction {

	/** The rescheduled task. */
	private Task _task;
	
	/** The TimeFrame of the task before being rescheduled */
	private TimeFrame _timeFrame;
	
	/** The user input string */
	private final String _USERINPUT;
	
	/**
	 * Sole constructor of AddAction.
	 * 
	 * @param ui
	 *            The user interface.
	 * @param userInput
	 *            The user input containing the rescheduling info.
	 */
	public RescheduleAction(GraphicalInterface ui, Logic logic, String userInput) {
		super(ui, logic, true);
		_USERINPUT = userInput;		
		
		try {
			int id = CLILogic.getInputId(_USERINPUT);
			_task = logic.searchWithID(id);
			_timeFrame = _task.getTimeFrame();
		} catch (Exception e) {
			_UI.showFeedBackMessage(e.getMessage());
		}
	}

	@Override
	public void apply() {
		try {
	 		int id = _task.getId();	 		
	 		String newSchedule = CLILogic.getWithoutId(_USERINPUT);
			_task = _LOGIC.reschedule(id, newSchedule);
			
			_UI.showFeedBackMessage("Task has been rescheduled.");
			_UI.displayTask(_task, "Rescheduled task: ");
			
			if (_task.getTimeFrame().getType() != TimeFrame.WHENEVER && _task.getTimeFrame().getType() != TimeFrame.FROM){
				CLILogic.getInstance().displayTaskOn(_task);
			}
			
		} catch (IndexOutOfBoundsException e){
			this.fail();
			_UI.showFeedBackMessage(CLILogic.MESSAGE_RESCHEDULE_NO_SCHEDULE);
		} catch (NumberFormatException e){
			this.fail();
			_UI.showFeedBackMessage(CLILogic.MESSAGE_RESCHEDULE_INVALID_ID);
		} catch(ParserException e) {
			this.fail();
			_UI.showFeedBackMessage(CLILogic.MESSAGE_RESCHEDULE_WRONG_FORMAT);
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
		
		try {
			_task = _LOGIC.reschedule(id, _timeFrame.toString());
			_UI.showFeedBackMessage("Task rescheduled to previous time");
			_UI.displayTask(_task, "Rescheduled task: ");
			
			if (_task.getTimeFrame().getType() != TimeFrame.WHENEVER && _task.getTimeFrame().getType() != TimeFrame.FROM){
				CLILogic.getInstance().displayTaskOn(_task);
			}
		} catch(ParserException e) {
			this.fail();
			_UI.showFeedBackMessage(CLILogic.MESSAGE_RESCHEDULE_WRONG_FORMAT);
		} catch(TodoException e) {
			this.fail();
			_UI.showFeedBackMessage(e.getMessage());
		}
	}
}
