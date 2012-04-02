package userinterface.actions;

/**
 * An Action defines basically two methods, apply and unapply. Unapply must do
 * the inverse modifications that apply does.
 *  
 */
abstract public class Action {
	
	private boolean _successful = true;
	
	private boolean _hasToBeStored;

	/**
	 * Sole constructor of Action.
	 */
	public Action(boolean hasToBeStored) {
		_hasToBeStored = hasToBeStored;
	}

	/**
	 * Performs any side-effect.
	 */
	abstract public void apply();

	/**
	 * Perform the inverse of apply's side-effect
	 */
	public void unapply() {
	};

	/**
	 * Defines whether this Action has to be stored in the history.
	 * 
	 * @return true if the Action has to be stored in the history, false
	 *         otherwise.
	 */
	public boolean hasToBeStored() {
		return _hasToBeStored;
	}
	
	/**
	 * Check that the Action has been successful.
	 * 
	 * @return true if the Action was successful, false otherwise.
	 */
	public boolean isSuccessful() {
		return _successful;
	}
	
	/**
	 * Declares that the Action has failed.
	 */
	public void fail() {
		_successful = false;
	}
}
