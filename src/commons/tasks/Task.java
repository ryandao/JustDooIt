package commons.tasks;

import commons.timeframes.TimeFrame;

/**
 * Implement properties and methods for a task
 */
public class Task implements Cloneable{
	private int _id;                   // the task id
	private static int _maxId = 0;     // the current maximum task id
	private String _content;           // content of the task
	private TimeFrame _timeFrame;      // stores information about the timeline of the task 
	private boolean _status = false;   // whether the task is done
	// private priority
	
	/* Constructors */
	public Task(String content, TimeFrame timeFrame) {
		this(content, timeFrame, _maxId);
	}
	
	public Task(String content, TimeFrame timeFrame, int id) {
		_id = id;
		_content = content;
		_timeFrame = timeFrame;
		
		if(id >= _maxId) {
			_maxId = id + 1;
		}
	}
	
	public Task(String content, TimeFrame timeFrame, boolean status) {
		this(content, timeFrame, _maxId, status);
	}
	
	private Task(String content, TimeFrame timeFrame, int id, boolean status) {
		this(content, timeFrame, id);
		_status = status;
	}
	
	/* Accessors */
	public int getId() {
		return _id;
	}
	
	public int getMaxId() {
		return _maxId;
	}
	
	public String getContent() {
		return _content;
	}
	
	public TimeFrame getTimeFrame() {
		return _timeFrame;
	}
	
	public boolean getStatus() {
		return _status;
	}		
	
	/* Mutators */
	public void setId(int id) {
		_id = id;
	}
	
	public void setMaxId(int maxId) {
		_maxId = maxId;
	}
	
	public void setContent(String content) {
		_content = content;
	}
	
	public void setTimeFrame(TimeFrame timeFrame) {
		_timeFrame = timeFrame;
	}
	
	public void setStatus(boolean status) {
		_status = status;
	}
	
	/**
	 * Clone this Task object
	 */
	public Task clone() {
		return new Task(_content, _timeFrame, _id, _status);
	}
	
	public String toString() {
		String status = _status ? "X" : " ";
		return "(" + _id + ") [" + status + "] " + _timeFrame.toString() + " : " + _content;
	}
}
