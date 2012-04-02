package userinterface.actions;

import userinterface.GraphicalInterface;
import logic.Logic;

/**
 * Abstract Action that works on a UserInterface.
 *  
 */
public abstract class UIAction extends Action {

	/** The user interface. */
	protected final GraphicalInterface _UI;
	
	/** The logic. */
	protected final Logic _LOGIC;

	public UIAction(GraphicalInterface ui, Logic logic, boolean hasToBeStored) {
		super(hasToBeStored);
		_UI = ui;
		_LOGIC = logic;
	}
}
