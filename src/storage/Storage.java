package storage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import commons.datetypes.DateType;
import commons.datetypes.OnTheDay;
import commons.datetypes.Precisely;
import commons.tasks.Task;
import commons.timeframes.Between;
import commons.timeframes.By;
import commons.timeframes.FixPoint;
import commons.timeframes.From;
import commons.timeframes.TimeFrame;
import commons.timeframes.Whenever;
import commons.exceptions.StorageException;
import commons.exceptions.TaskNotFoundException;
import static utils.logging.Logger.log;

/**
 * Storage class for Tasks.
 * 
 */
public class Storage {
	
	/** List of all Tasks. */
	private ArrayList<Task> _taskList = new ArrayList<Task>();
	
	/** Storage file. */
	private File _storageFile;
	
	/**
	 * Constructor specifying file path.
	 * 
	 * @param path Path of the file in the system.
	 * @throws StorageException  if file cannot be read or created.
	 */
	public Storage(String path) throws StorageException {
		this(new File(path));
	}
	
	/**
	 * Constructor specifying file.
	 * 
	 * @param file File that is to be read from and written to.
	 * @throws StorageException if file cannot be read or created.
	 */
	public Storage(File file) throws StorageException {
	  try {
	    file.createNewFile();
	    _storageFile = file;
	    readFile(_storageFile);
	  }
	  catch(IOException e) {				
	    log("Impossible to open " + file.getAbsolutePath() + ".");				
	    throw new StorageException("Impossible to open storage file.");
	  }
	}

	/**
	 * Returns the number of Task objects.
	 * 
	 * @return the number of Task objects.
	 */
	public int size() {
		return _taskList.size();
	}

	/**
	 * Adds a new Task.
	 * 
	 * @param newTask the task to be added.
	 * @throws StorageException if the file cannot be written.
	 */
	public void add(Task newTask) throws StorageException {		
		_taskList.add(newTask);
		try{
			writeFile(_storageFile);
		}
		catch(IOException e) {
			
			log("Impossible to save to file " + _storageFile.getAbsolutePath() + ".");
			
			throw new StorageException("Impossible to save to storage file.");
		}
	}

	/**
	 * Deletes a specific existing Task.
	 * 
	 * @param oldTask the Task to be deleted.
	 * @throws TaskNotFound if no Task corresponds to the one specified.
	 * @throws StorageException if the file can not be saved.
	 */
	public void delete(Task oldTask) throws TaskNotFoundException, StorageException {
		
		for(Iterator<Task> it = _taskList.iterator(); it.hasNext();) {
			Task task = it.next();
			
			if(task.getId() == oldTask.getId()) {
				it.remove();				
				try{
					writeFile(_storageFile);
				}
				catch(IOException e) {					
					log("Impossible to save to file " + _storageFile.getAbsolutePath() + ".");					
					throw new StorageException("Impossible to save to storage file.");
				}				
				return;
			}
		}
		throw new TaskNotFoundException("Task does not exist.");
	}
	
	/**
	 * Replaces a specific existing Task with a modified version.
	 * 
	 * @param existingTask the modified Task.
	 * @throws TaskNotFound if no Task corresponds to the one specified.
	 * @throws StorageException if the file can not be saved.
	 */
	public void modify(Task existingTask) throws StorageException, TaskNotFoundException {		
		int replacementID = existingTask.getId();		
		
		for (int i=0; i<_taskList.size(); i++) {
			if (_taskList.get(i).getId() == replacementID) {
				_taskList.set(i, existingTask);
				try {
					writeFile(_storageFile);
				} catch(IOException e) {					
					log("Impossible to save to file " + _storageFile.getAbsolutePath() + ".");					
					throw new StorageException("Impossible to save to storage file.");
				}
				return;
			}
		}		
		throw new TaskNotFoundException("Task does not exist.");
	}
	
	/**
	 * Returns a list consisting of a copy of all existing tasks.
	 * 
	 * @return a list consisting of a copy of all existing tasks.
	 */
	public List<Task> getAll() {
		ArrayList<Task> tasks = new ArrayList<Task>();
		
		for (Task task : _taskList) {			
			Task temp = task.clone();			
			tasks.add(temp);
		}
		return tasks;
	}
	
	/**
	 * Update a file with any changes made from all task object.
	 * 
	 * @param storageFile the text file to write to.
	 * @throws FileNotFoundException if storageFile cannot be found.
	 */
	private void writeFile(File storageFile) throws FileNotFoundException {
		FileOutputStream outFileStream = new FileOutputStream(storageFile);
		PrintWriter outStream = new PrintWriter(outFileStream);

		for (Task temp : _taskList) {			
			TimeFrame tf = temp.getTimeFrame();
			int frameType = tf.getType();
			String dateformat = null;
			DateType datetype;
			
			switch(frameType) {
			// FixPoint
			case TimeFrame.FIXPOINT: 
				datetype = ((FixPoint) tf).getDate();
				dateformat = dateTypeToString(datetype);
				break;
			// By	
			case TimeFrame.BY:
				datetype = ((By) tf).getDate();
				dateformat = "_" + "," + dateTypeToString(datetype);
				break;
			// From
			case TimeFrame.FROM:
				datetype = ((From) tf).getDate();
				dateformat = dateTypeToString(datetype) + "," + "_";
				break;
			// Between
			case TimeFrame.BETWEEN:
				DateType date1 = ((Between) tf).getStart();
				DateType date2 = ((Between) tf).getEnd();
				dateformat = dateTypeToString(date1) + "," + dateTypeToString(date2);
				break;
			// Whenever
			case TimeFrame.WHENEVER: dateformat = "";
				break;
			default:
				// Should never reach here.
				assert(false);
			}
		
			// <status>	<date>	<content>
			outStream.println("<" + temp.getStatus() + ">" + "\t" + "<" + dateformat + ">" + "\t" + "<" + temp.getContent() + ">");
		}
		outStream.close();
	}
	
	/**
	 * Read strings from a file and convert them to Task objects.
	 * 
	 * @param storageFile a text file to read information to construct Task objects from.
	 * @throws IOException if storageFile cannot be read.
	 */
	private void readFile(File storageFile) throws IOException {
		FileReader fileReader = new FileReader(storageFile);
		BufferedReader bufReader = new BufferedReader(fileReader);
		
		String str = bufReader.readLine();
			
		while ((str != null)) {
			String[] tempHolder = str.split("\t");
			
			for(int i=0;i<tempHolder.length; i++) {
				String thingo = tempHolder[i];
				thingo = thingo.substring(1, (thingo.length())-1);
				tempHolder[i] = thingo;
			}
		
			// Get Status
			boolean status = false;
			if (tempHolder[0].compareTo("true") == 0) {
				status = true;
			}
						
			// Set Date
			TimeFrame timeframe;
			
			if (tempHolder[1].length() == 0) {
				// Whenever
				timeframe = new Whenever();
			} else {				
				String[] dates = tempHolder[1].split(",");
				
				if(dates.length == 1) {
					DateType datetype = stringToDateType(dates[0]);
					timeframe = new FixPoint(datetype);
				} else {
					if(dates[0].equals("_")) {
						// By
						DateType datetype = stringToDateType(dates[1]);
						timeframe = new By(datetype);
					} else {
						if(dates[1].equals("_")) {
							// From
							DateType datetype = stringToDateType(dates[0]);
							timeframe = new From(datetype);
						} else {
							// Between
							DateType date1 = stringToDateType(dates[0]);
							DateType date2 = stringToDateType(dates[1]);
							timeframe = new Between(date1, date2);
						}
					}
				}
			}	
				
			// Set Content
			String content = tempHolder[2];
			Task temp = new Task(content, timeframe, status);
			
			_taskList.add(temp);
			str = bufReader.readLine();
		}
	}
	
	/**
	 * Converts a String to a DateType object.
	 * 
	 * @param text The string holding the information of DateType.
	 * @return a DateType object.
	 */
	private static DateType stringToDateType(String text) {
		if(text.startsWith(".")) {
			text = text.substring(1);
			long timestamp = Long.parseLong(text);
			return new Precisely(new Date(timestamp));
		} else {
			long timestamp = Long.parseLong(text);
			return new OnTheDay(new Date(timestamp));
		}
	}
	
	/**
	 * Converts a DateType object to a String.
	 * 
	 * @param dateType An object holding information of the type of date and information of date.
	 * @return the String used to store that DateType.
	 */
	private static String dateTypeToString(DateType dateType) {
		String string = "";
		if(dateType.isPrecise()) {
			string += ".";
		}
		string += dateType.timestamp();
		return string;
	}
}
