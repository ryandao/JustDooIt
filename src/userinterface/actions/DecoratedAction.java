package userinterface.actions;

import userinterface.history.History;

/**
 * A wrapper for Action in order to decorate them.
 * 
 */
public class DecoratedAction extends Action {

	/** The Action decorated */
	protected final Action _ACTION;
	
	/** The History in which to store the Action. */
	private final History _HISTORY;

	/**
	 * Sole constructor of DecoratedAction.
	 * 
	 * @param action
	 *            The Action to decorate.
	 */
	public DecoratedAction(Action action, History history) {
		super(action.hasToBeStored());
		_ACTION = action;
		_HISTORY = history;
	}

	@Override
	public void apply() {
		_ACTION.apply();
		
		if (_ACTION.hasToBeStored()) {
			_HISTORY.register(_ACTION);
		}
	}

	@Override
	public void unapply() {
		_ACTION.unapply();
	}
	
	@Override
	public boolean isSuccessful() {
		return _ACTION.isSuccessful();
	}
	
	@Override
	public void fail() {
		_ACTION.fail();
	}
}
