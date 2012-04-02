package commons.exceptions;

public class TaskNotFoundException extends TodoException {

	private static final long serialVersionUID = 1L;
	
	public TaskNotFoundException(String string) {
		super(string);
	}
}
