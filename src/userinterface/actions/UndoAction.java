package userinterface.actions;

import java.util.EmptyStackException;

import userinterface.GraphicalInterface;
import userinterface.history.History;

import logic.Logic;


/**
 * An Action that unapply the last applied Action.
 *
 */
public class UndoAction extends UIAction {

	/** The history. */
	private final History _HISTORY;

	/**
	 * Sole constructor of UndoAction.
	 * 
	 * @param ui
	 *            The user interface.
	 * @param history
	 *            The History.
	 */
	public UndoAction(GraphicalInterface ui, Logic logic, History history) {
		super(ui, logic, false);
		_HISTORY = history;
	}

	@Override
	public void apply() {
		
		try {
			_HISTORY.undo();
		} catch (EmptyStackException e) {
			_UI.showFeedBackMessage("Nothing to Undo.");
		}
	}
}
