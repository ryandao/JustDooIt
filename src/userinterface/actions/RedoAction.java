package userinterface.actions;

import java.util.EmptyStackException;

import userinterface.GraphicalInterface;
import userinterface.history.History;

import logic.Logic;

/**
 * An Action that re-apply the last undone Action.
 * 
 */
public class RedoAction extends UIAction {

	/** The history. */
	private final History _HISTORY;

	/**
	 * Sole constructor of RedoAction.
	 * 
	 * @param ui
	 *            The user interface.
	 * @param history
	 *            The History.
	 */
	public RedoAction(GraphicalInterface ui, Logic logic, History history) {
		super(ui, logic, false);
		_HISTORY = history;
	}

	@Override
	public void apply() {
		try {
			_HISTORY.redo();
		} catch (EmptyStackException e) {
			_UI.showFeedBackMessage("Nothing to Redo.");
		}
	}

}
