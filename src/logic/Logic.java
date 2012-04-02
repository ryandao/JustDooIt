package logic;

import static utils.logging.Logger.log;

import java.io.File;
import java.util.*;

import parser.TodoParser;
import storage.Storage;
import commons.exceptions.*;
import commons.tasks.Task;
import commons.tasks.TaskComparator;
import commons.timeframes.TimeFrame;

/**
 * Logic component.
 * 
 */
public class Logic {
	private static final String TASK_NOT_FOUND = "The task is not found";
	private static final String INVALID_RANGE = "The range is invalid";
	
	private Storage storage;
	private TodoParser parser = new TodoParser();
	
	/** 
	 * Sole constructor.
	 * 
	 * @param storageFile The storage file.
	 * @throws StorageException if the file can not be read.
	 */
	public Logic(File storageFile) throws StorageException {
		storage = new Storage(storageFile);
	}
	
	/**
	 * Adds a Task to the manager.
	 * 
	 * @param text The text to parse and information of the Task.
	 * @return The Task added.
	 * @throws ParserException if the text is not valid input to create a Task.
	 * @throws StorageException if the file can not be saved.
	 */
	public Task add(String text) throws StorageException, ParserException {
		
		Task task = parser.getTask(text);
		storage.add(task);
		
		return task;
	}
	
	/**
	 * Adds a Task to the manager.
	 * 
	 * @param task The Task to add.
	 * @throws StorageException if the file can not be saved.
	 */
	public void add(Task task) throws StorageException {
		storage.add(task);
	}
	
	/**
	 * Gets the list of Task objects which have time clash with the given Task specified by id.
	 * 
	 * @param id The id of the given Task.
	 * @return The list of Task objects which clashes with the given task.
	 * @throws TaskNotFoundException if the Task with given id is not found.
	 */
	public List<Task> clashWith(int id) throws TaskNotFoundException {
			
		TimeFrame tf = searchWithID(id).getTimeFrame();
		List<Task> list = storage.getAll();
		List<Task> clashes = new ArrayList<Task>();
		
		for(Task task : list){
			if(task.getTimeFrame().getType() != 5 && task.getTimeFrame().superimpose(tf) && task.getId() != id){
				clashes.add(task);
				
			}
		}
		
		return clashes;
	}
	
	/**
	 * Searches for Task objects which contain the given information in content.
	 * The search is case insensitive.
	 * 
	 * @param text The String to search for in the Task contents.
	 * @return The list of Task objects containing text in content.
	 */
	public List<Task> search(String text) {
		text = text.toLowerCase();
		
		assert(!(text == ("")));
		
		List<Task> list = new ArrayList<Task>();
		List<Task> allTasks = storage.getAll();
		for(Task task : allTasks){
			if(task.getContent().toLowerCase().contains(text)) {
				list.add(task);
			}
		}
				
		return list;
	}
	
	/**
	 * Searches for the Task with a given id.
	 * 
	 * @param id The id of the Task to find.
	 * @return The Task with the given id.
	 * @throws TaskNotFoundException if the task with given id is not found.
	 */
	public Task searchWithID(int id) throws TaskNotFoundException {
		List<Task> list = storage.getAll();
		for(Task task : list){
			if(task.getId() == (id)){
				return task;
			}
		}
		
		log("Impossible to find task with id " + id + ".");
		
		throw new TaskNotFoundException(TASK_NOT_FOUND);
	}	
	
	/**
	 * Modifies the content of the Task with given id.
	 * 
	 * @param id The id of task to be modified.
	 * @param activity The new content after modification.
	 * @return the modified Task.
	 * @throws TaskNotFoundException if the task with given id is not found.
	 * @throws StorageException if the file can not be saved.
	 */
	public Task modify(int id, String activity) throws TaskNotFoundException, StorageException {
		Task modifyTask;
		
		assert(id >= 0);
		
		modifyTask = searchWithID(id);
		modifyTask.setContent(activity);
		storage.modify(modifyTask);
		return modifyTask;
	}

	/**
	 * Modifies the content of the Task with given id.
	 * 
	 * @param task The Task to be modified.
	 * @param activity The new content after modification.
	 * @return the modified Task.
	 * @throws TaskNotFoundException if the Task is not found in the file.
	 * @throws StorageException if the file can not be saved.
	 */
	public Task modify(Task task, String activity) throws StorageException, TaskNotFoundException {
		task.setContent(activity);
		storage.modify(task);
		return task;
	}
		
	/**
	 * Modifies the TimeFrame of the Task with given id.
	 * 
	 * @param id The id of task to be modified.
	 * @param time The text description of a TimeFrame.
	 * @return The rescheduled Task.
	 * @throws TaskNotFoundException if the Task with given id is not found.
	 * @throws StorageException if the file can not be saved.
	 * @throws ParserException if time is not a proper description of a TimeFrame.
	 */
	public Task reschedule(int id, String time) throws TaskNotFoundException, StorageException, ParserException {
		Task modifyTask;
		
		assert(id >= 0);
		assert(time != "");
		
		modifyTask = searchWithID(id);
		modifyTask.setTimeFrame(parser.getTimeFrame(time));
		storage.modify(modifyTask);
		return modifyTask;
	}
	
	/**
	 * Modifies the TimeFrame of the Task with given id.
	 * 
	 * @param id The id of task to be modified.
	 * @param schedule The TimeFrame to reschedule the Task to.
	 * @return The rescheduled Task.
	 * @throws TaskNotFoundException if the Task with given id is not found.
	 * @throws StorageException if the file can not be saved.
	 */
	public Task reschedule(int id, TimeFrame schedule) throws TaskNotFoundException, StorageException {
		Task modifyTask = searchWithID(id);
		modifyTask.setTimeFrame(schedule);
		storage.modify(modifyTask);
		
		return modifyTask;
	}
	
	/**
	 * Deletes a Task.
	 * 
	 * @param id The id of the Task to delete.
	 * @return the deleted Task.
	 * @throws TaskNotFoundException if the Task can not be found.
	 * @throws StorageException if the file can not be saved.
	 */
	public Task delete(int id) throws TaskNotFoundException, StorageException {
		Task deleteTask;
		
		assert(id >= 0);
		
		deleteTask = searchWithID(id);
		storage.delete(deleteTask);
		return deleteTask;
	}
	
	/**
	 * Gives a list of missed Task objects.
	 * 
	 * @param day on the date which the the task missed are required.
	 * @return The list of task which are missed on the day.
	 */
	public List<Task> getMissedTasks(Date day) {
		List<Task> list = new ArrayList<Task>();
		List<Task> allTasks = storage.getAll();
		
		for(Task task : allTasks){
			if(!task.getTimeFrame().endsAfter(day) && 
					!task.getTimeFrame().endsOnTheSameDay(day) && 
					!task.getStatus()){
				list.add(task);
			}
		}
		
		Collections.sort(list, new TaskComparator());
		
		return list;
	}
	
	/**
	 * Gives a list of Task objects which ends on the same day.
	 * 
	 * @param day The date to get the must do Task object for.
	 * @return The list of Task objects which are to be done on the day.
	 */
	public List<Task> getMustDoTasks(Date day) {
		List<Task> list = new ArrayList<Task>();
		
		List<Task> allTasks = storage.getAll();
		for(Task task : allTasks){
			if(task.getTimeFrame().endsOnTheSameDay(day)){
				list.add(task);
			}
		}
		
		Collections.sort(list, new TaskComparator());
		
		return list;
	}
	
	/**
	 * Gives a list of should do Task objects which starts before or 
	 * starts the same day and ends after, but not the same day.
	 * 
	 * @param day the reference date.
	 * @return The list of Task objects which are possible to do on that day.
	 */
	public List<Task> getShouldDoTasks(Date day) {
		List<Task> list = new ArrayList<Task>();
		List<Task> allTasks = storage.getAll();
		
		for(Task task : allTasks){
			TimeFrame timeFrame = task.getTimeFrame();
			if(!task.getStatus() && timeFrame.endsAfter(day) && !timeFrame.endsOnTheSameDay(day) &&
					(timeFrame.startsBefore(day) || timeFrame.startsTheSameDay(day))){
				list.add(task);
			}
		}
		
		Collections.sort(list, new TaskComparator());
		
		return list;
	}
		
	/**
	 * Gives a list of Task objects within the given id range or TimeFrame
	 * 
	 * @param text The range of id or TimeFrame.
	 * @return The list of Task objects within the given TimeFrame or id.
	 * @throws ParserException if text is an invalid range of id or TimeFrame.
	 */
	public List<Task> within(String text) throws ParserException {

		List<Task> allTasks = storage.getAll();
		
		if(text.isEmpty()) {
			return allTasks;
		}
		
		try{
			List<Task> list = new ArrayList<Task>();
			
			List<Integer> idList = parser.getIds(text);
			
			for(Task task : allTasks) {
				if(idList.contains(task.getId())) {
					list.add(task);
				}
			}
			
			Collections.sort(list, new TaskComparator());
			
			return list;
			
		}catch(Exception ex1){
			
			try{
				List<Task> list = new ArrayList<Task>();
				
				TimeFrame withinTimeFrame = parser.getTimeFrame(text);
				
				for(Task task : allTasks){
					if(withinTimeFrame.superimpose(task.getTimeFrame())){
						list.add(task);
					}
				}
				
				Collections.sort(list, new TaskComparator());
				
				return list;
				
			}catch(Exception ex2){
				
				log("Impossible to get a result for a corresponding list of IDs or TimeFrame.");
				
				throw new ParserException(INVALID_RANGE);
			}
		}				
	}
		
		
	/**
	 * Modifies the status of a Task.
	 * 
	 * @param id The id of the Task.
	 * @param status true if the Task is done, false otherwise.
	 * @return The modified Task.
	 * @throws TaskNotFoundException if the Task can not be found.
	 * @throws StorageException if the file can not be saved.
	 */
	public Task setStatus(int id, boolean status) throws TaskNotFoundException, StorageException {
		
		assert(id >= 0);
		
		Task modifyTask = searchWithID(id);
		modifyTask.setStatus(status);
		storage.modify(modifyTask);
		return modifyTask;
	}
	
	/**
	 * Toggles the status of a Task.
	 * 
	 * @param id The id of the Task.
	 * @return The modified Task.
	 * @throws TaskNotFoundException if the Task can not be found.
	 * @throws StorageException if the file can not be saved.
	 */
	public Task toggle(int id) throws TaskNotFoundException, StorageException{
		boolean modifyStatus;
		Task modifyTask;
		
		assert(id >= 0);
		
		modifyTask = searchWithID(id);
		modifyStatus = !(modifyTask.getStatus());
		modifyTask.setStatus(modifyStatus);
		storage.modify(modifyTask);
		return modifyTask;
	}

}