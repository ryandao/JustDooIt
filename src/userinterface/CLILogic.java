package userinterface;

import java.io.File;
import java.util.List;

import userinterface.actions.*;

import commons.exceptions.StorageException;
import commons.exceptions.TaskNotFoundException;
import commons.tasks.Task;
import userinterface.history.History;
import static utils.logging.Logger.log;
import logic.Logic;

public class CLILogic {
	public static final String MESSAGE_WELCOME = "Hi! Welcome to JustDooIt! :)";
	public static final String MESSAGE_TODO = "Scheduled tasks for today :";
	
	public static final String MESSAGE_ADDED = "Task \"%1s\" to be done %2s, has been added.";
	public static final String MESSAGE_ADD_WRONG_FORMAT = "Task was not added because no task desciption has been provided.";
	public static final String MESSAGE_TASKS_ON_DAY = "Tasks to do around this time : ";
	public static final String MESSAGE_NO_OTHER_TASK = "There are no other tasks happening around the time of this task.";
	
	public static final String MESSAGE_DISPLAYED = "Displaying tasks for ";
	public static final String MESSAGE_DISPLAY_RESULTS = "Found %1s matching results :";
	public static final String MESSAGE_DISPLAT_NO_RESULT = "No matches found.";
	public static final String MESSAGE_DISPLAY_NO_SEARCHITEM = "No search item has been provided to display the related Tasks.";
	
	public static final String MESSAGE_SEARCHED = "Searched for Keyword : ";
	public static final String MESSAGE_SEARCH_RESULT = "Results corresponding to Keyword : ";
	public static final String MESSAGE_SEARCHED_NO_RESULT = "No results for the searched keyword : ";
	public static final String MESSAGE_SEARCH_NO_KEYWORD = "No search Keyword has been provided.";
	
	public static final String MESSAGE_MODIFIED = "Activity \"%1s\" has been modified to \"%2s\".";
	public static final String MESSAGE_MODIFY_NO_ACTIVITY = "No new task description has been provided.";
	public static final String MESSAGE_MODIFY_WRONG_FORMAT = "Please provide both a valid task Id and a new task description to modify a task.";
	public static final String MESSAGE_MODIFY_WRONG_ID_FORMAT = "Please enter an integer for the Task ID of the Task to be modified.";
	
	public static final String MESSAGE_RESCHEDULED = "Task \"%1s\" to be initially done %2s has been rescheduled to %3s.";
	public static final String MESSAGE_RESCHEDULE_NO_SCHEDULE = "No new schedule has been provided for the task";
	public static final String MESSAGE_RESCHEDULE_WRONG_FORMAT = "Task cannot be rescheduled. Please provide both the Task ID of the task and new schedule.";
	public static final String MESSAGE_RESCHEDULE_INVALID_ID = "Please enter a valid or existing Task Id corresponding to the Task to be rescheduled.";
	
	public static final String MESSAGE_DELETED =  "Task (%1s) deleted.";
	public static final String MESSAGE_DELETE_NO_ID = "Task cannot be deleted because no id has been provided.";
	public static final String MESSAGE_DELETE_WRONG_FORMAT = "Task cannot be deleted because Task Id provided is not an integer.";
		
	public static final String MESSAGE_DONE = "The task (%1s) done.";
	public static final String MESSAGE_ALREADY_DONE = "Task (%1s) has already been marked as done previously.";
	public static final String MESSAGE_DONE_WRONG_FORMAT = "Wrong format provided. Please enter the task's id after the done command.";
	
	public static final String MESSAGE_NOT_DONE = "Task (%1s) has been marked as not done.";
	public static final String MESSAGE_ALREADY_NOT_DONE = "Task (%1s) has already been marked as not done previously.";
	public static final String MESSAGE_NOT_DONE_WRONG_FORMAT = "Wrong format provided. Please enter the task's id after the notdone command.";
	
	public static final String MESSAGE_HELP = "Listed below are JustDooIt commands.";
	public static final String MESSAGE_INVALID = "The input command was not recognised. Listed below are JustDooIt commands.";
	public static final String MESSAGE_HELP_FILE_NOT_FOUND = "JustDooIt's help file could not be found.";
	public static final String MESSAGE_CANNOT_INSTANTIATE = "Can not instantiate logic component.";
	public static final String MESSAGE_NO_FUTUR_TASKS = "No tasks are scheduled for later.";
	
	public static final String HELP_FILE_NAME = "ressources/help.txt";
	public static final String TODO_FILE_NAME = "todo.tasks";
		
			
	enum COMMAND_TYPE {ADD, SEARCH, DISPLAY, MODIFY, RESCHEDULE, DELETE, HELP, EXIT, INVALID, TODO, DONE, NOT_DONE, UNDO, REDO};
	
	private Logic _logic;
	
	private History _history = new History();
	
	private GraphicalInterface _cli = new GraphicalInterface();
	
	private static CLILogic _instance = null;
	
	private CLILogic() {
		try {
			_logic = new Logic(new File(TODO_FILE_NAME));
		} catch (StorageException e) {
			log("Can not instantiate logic component.");
			System.exit(1);
		}
	}
	
	public static CLILogic getInstance() {
		if(_instance == null) {
			_instance = new CLILogic();
		}
		
		return _instance;
	}
	
	/**
	 * Starts the graphical interface and will execute the user's input until the
	 * exit command pas been entered or close button pressed.
	 */
	public void executeUntilExit(){		
		
		_cli.start();
		
		_cli.showFeedBackMessage(MESSAGE_WELCOME);
		
		TodoAction todo = new TodoAction(_cli, _logic);
		todo.apply();
		
	}
	
	/**
	 * Checks for null input command. If so the todo list is displayed.
	 * If any other command is entered, the type of command will first be determined, and
	 * the returned COMMAND_TYPE will be used to execute the command the user input.
	 */
	public void executeUserInputCommand(String userInput) {
		if(userInput.equals("")){
			_cli.showFeedBackMessage(MESSAGE_TODO);
			TodoAction todo = new TodoAction(_cli, _logic);
			todo.apply();
		}
		else {
			COMMAND_TYPE command = determineCommandType(getFirstWord(userInput));
			executeCommand(command, getRest(userInput));
		}
	}
	
	/**
	 * Gets the first word of the String the user input
	 * @param userInput
	 * 				The whole String the user input
	 * @return
	 * 				The first word of the String
	 */
	private static String getFirstWord(String userInput) {
		String firstWord = userInput.split("\\s+")[0];
		return firstWord;
	}
	
	/**
	 * Gets everything behind the first word
	 * @param userInput
	 * 				The whole String the user input
	 * @return
	 * 				Everything behind the first word. Returns "" if only one word has been input by the user.
	 */
	private static String getRest(String userInput) {
		if(userInput.contains(" ")) {
			int i = userInput.indexOf(" ");
			return userInput.substring(i).trim();
		}
		else {
			return "";
		}
	}
	
	/**
	 * Determines the type of command that the user input
	 * @param firstWord
	 * 				First word of the String input by the user. It correspond to a JustDooIt command.
	 * 				It can either be a full word command like "add" or its corresponding shortcut like "a"
	 * @return
	 * 				The enum COMMAND_TYPE
	 */
	private static COMMAND_TYPE determineCommandType(String firstWord) {
		if (firstWord.equalsIgnoreCase("add") || firstWord.equalsIgnoreCase("a"))
			return COMMAND_TYPE.ADD;
		else if (firstWord.equalsIgnoreCase("search") || firstWord.equalsIgnoreCase("s"))
			return COMMAND_TYPE.SEARCH;
		else if (firstWord.equalsIgnoreCase("display") || firstWord.equalsIgnoreCase("d"))
			return COMMAND_TYPE.DISPLAY;
		else if (firstWord.equalsIgnoreCase("modify") || firstWord.equalsIgnoreCase("m"))
			return COMMAND_TYPE.MODIFY;
		else if (firstWord.equalsIgnoreCase("reschedule") || firstWord.equalsIgnoreCase("r"))
			return COMMAND_TYPE.RESCHEDULE;
		else if (firstWord.equalsIgnoreCase("delete") || firstWord.equalsIgnoreCase("k"))
			return COMMAND_TYPE.DELETE;
		else if (firstWord.equalsIgnoreCase("help") || firstWord.equalsIgnoreCase("h"))
			return COMMAND_TYPE.HELP;
		else if (firstWord.equalsIgnoreCase("exit") || firstWord.equalsIgnoreCase("e"))
			return COMMAND_TYPE.EXIT;
		else if (firstWord.equalsIgnoreCase("todo") || firstWord.equalsIgnoreCase("t"))
			return COMMAND_TYPE.TODO;
		else if (firstWord.equalsIgnoreCase("done") || firstWord.equalsIgnoreCase("x"))
			return COMMAND_TYPE.DONE;
		else if (firstWord.equalsIgnoreCase("notdone") || firstWord.equalsIgnoreCase("o"))
			return COMMAND_TYPE.NOT_DONE;
		else if (firstWord.equalsIgnoreCase("undo") || firstWord.equalsIgnoreCase("<"))
			return COMMAND_TYPE.UNDO;
		else if (firstWord.equalsIgnoreCase("redo") || firstWord.equalsIgnoreCase(">"))
			return COMMAND_TYPE.REDO;
		else
			return COMMAND_TYPE.INVALID;
	}
	
	/**
	 * Execute the corresponding command
	 * @param command
	 * 				COMMAND_TYPE command input by the user
	 * @param userText
	 * 				String following the command word which is a String representation of task id, task content or timeframe.
	 */
	private void executeCommand(COMMAND_TYPE command, String userText) {
		Action action = getAction(command, userText);
		DecoratedAction decoratedAction = new DecoratedAction(action, _history);
		decoratedAction.apply();
	}
	
	
	/**
	 * Get the corresponding action of the command
	 */
	private Action getAction(COMMAND_TYPE command, String userText) {
		switch (command) {
			case ADD:
				return new AddAction(_cli, _logic, userText);			
			case SEARCH:
				return new SearchAction(_cli, _logic, userText);			
			case DISPLAY:
				return new DisplayAction(_cli, _logic, userText);			
			case MODIFY:
				return new ModifyAction(_cli, _logic, userText);						
			case RESCHEDULE:
				return new RescheduleAction(_cli, _logic, userText);						
			case DELETE:
				return new DeleteAction(_cli, _logic, userText);			
			case HELP:
				_cli.showFeedBackMessage(MESSAGE_HELP);
				return new HelpAction(_cli, _logic);			
			case EXIT:		
				return new ExitAction(_cli, _logic);			
			case TODO:
				_cli.showFeedBackMessage(MESSAGE_TODO);
				return new TodoAction(_cli, _logic);			
			case DONE:
				return new DoneAction(_cli, _logic, userText);
			case NOT_DONE:
				return new NotDoneAction(_cli, _logic, userText);
			case UNDO:
				return new UndoAction(_cli, _logic, _history);	
			case REDO:
				return new RedoAction(_cli, _logic, _history);	
			case INVALID:
				_cli.showFeedBackMessage(MESSAGE_INVALID);
				return new HelpAction(_cli,_logic);
			//	break;
		}
		
		return null;
	}
	
	/**
	 * Extracts the id of the task from the user input String.
	 * @param userInput
	 * 				String following the first command word
	 * @return
	 * 				task Id
	 * @throws NumberFormatException
	 */
	public static int getInputId(String userInput) throws NumberFormatException {
		userInput = userInput.trim();
		if(userInput.contains(" ")) {
			int index = userInput.indexOf(" ");
			return Integer.parseInt(userInput.substring(0, index));
		}
		else {
			return Integer.parseInt(userInput);
		}
	}
	
	/**
	 * Gets the String behind the id
	 * @param userInput
	 * 				String following the first command word
	 * @return
	 * 				String behind the id
	 * @throws IndexOutOfBoundsException
	 */
	public static String getWithoutId(String userInput) throws IndexOutOfBoundsException {
		userInput = userInput.trim();
		int index = userInput.indexOf(" ");
		return userInput.substring(index).trim();
	}
	
	/**
	 * Methods will displays if there are any other tasks happening around the same time as the
	 * task parsed in as parameter. This makes it easier for the user to see any possible clashes
	 * thus allowing the user to reschedule his task accordingly
	 * @param task
	 * 				task which has been just added, modified or rescheduled by the user
	 * @throws TaskNotFoundException 
	 */
	public void displayTaskOn(Task task) throws TaskNotFoundException {
		List<Task> tasksList = _logic.clashWith(task.getId());
		
		if(tasksList.size() <= 1) {
			
			_cli.displayFeedBackMessage(MESSAGE_NO_OTHER_TASK);
		}
		else {
			//cli.displayTask(task, message);
			_cli.displayTaskList(tasksList, MESSAGE_TASKS_ON_DAY);
		}	
	}
}