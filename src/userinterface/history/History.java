package userinterface.history;

import userinterface.actions.Action;

/**
 * An history of the actions performed.
 * 
 */
public class History {

	/** The data structure used in order to store the actions. */
	private StackZipper<Action> _actions = new StackZipper<Action>();

	/**
	 * Sole constructor of History.
	 */
	public History() {
	}

	/**
	 * Register the Action in the History.
	 */
	public void register(Action action) {
		_actions.push(action);
	}

	/**
	 * Unapply the Action at the head of the history.
	 */
	public void undo() {
		Action action;
		do {
			action = _actions.pop();
		} while(!action.isSuccessful());
		action.unapply();
	}

	/**
	 * Apply the last unapplied Action.
	 */
	public void redo() {
		Action action;
		do {
			action = _actions.retrieve();
		} while(!action.isSuccessful());
		action.apply();
	}
}
