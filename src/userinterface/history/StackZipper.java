package userinterface.history;

import java.util.Stack;

/**
 * A generic data structure to model a stack with an highlighted element. It is
 * implemented with two stacks, the main one acting as a normal stack, the
 * second one to collect the consecutive item pop'd out, in order to be able to
 * push them again in the first stack, using the retrieve method.
 * 
 * @param <A>
 *            The type of the elements to store in this data structure.
 */
public class StackZipper<A> extends Stack<A> {

	private static final long serialVersionUID = 1L;

	/** The Stack of previously highlighted elements */
	private Stack<A> _previousHeads = new Stack<A>();

	/**
	 * Sole constructor of StackZipper.
	 */
	public StackZipper() {
		super();
	}

	@Override
	/**
	 * Pop an element out of the top of the Stack, put it in the
	 * Stack of previously highlighted elements.
	 * 
	 * @return The element pop'd.
	 */
	public A pop() {
		A element = super.pop();
		_previousHeads.push(element);
		return element;
	}

	@Override
	/**
	 * Push an element at the top of the Stack. Clear the Stack of all
	 * previously highlighted elements.
	 * 
	 * @return The element pushed.
	 */
	public A push(A element) {
		super.push(element);
		_previousHeads = new Stack<A>();
		return element;
	}

	/**
	 * Push the element on the top of the previously highlighted elements on the
	 * top of the Stack.
	 * 
	 * @return The element moved to the top of the Stack.
	 */
	public A retrieve() {
		A element = _previousHeads.pop();
		super.push(element);
		return element;
	}
}
