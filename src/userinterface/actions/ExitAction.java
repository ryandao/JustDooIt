package userinterface.actions;

import userinterface.GraphicalInterface;
import logic.Logic;

/**
 * Action that exits the program
 * 
 */
public class ExitAction extends UIAction {

	/**
	 * Sole constructor of ExitAction.
	 * 
	 * @param ui
	 *            The user interface.
	 */
	public ExitAction(GraphicalInterface ui, Logic logic) {
		super(ui, logic, false);
	}

	@Override
	public void apply() {
		_UI.showFeedBackMessage("Goodbye !");
		System.exit(0);
	}

}
