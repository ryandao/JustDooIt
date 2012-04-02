package commons.datetypes;

import java.util.*;

/**
 * Abstract class for all the date types available for parser 
 */
public abstract class DateType {
	protected Date _date;
	
	/* Constructors */	
	public DateType() { }
	
	public DateType(Date date) {
		_date = date;
	}
	
	/**
	 * Check if this date is before a certain date
	 * 
	 * @param date
	 *   The date to check against
	 */
	public abstract boolean before(Date date);
	
	/**
	 * Check if this date is after a certain date
	 * 
	 * @param date
	 *   The date to check against	
	 */
	public abstract boolean after(Date date);
	
	/**
	 * Check if this date has the same time with a certain date
	 * 
	 * @param date
	 *   The date to check against
	 */
	public abstract boolean sameTime(Date date);
	
	/**
	 * Check if this date is on the same day with a certain date
	 * 
	 *  @param date
	 *   The date to check against
	 */
	public abstract boolean sameDay(Date date);
	
	/**
	 * Check if this date is a precise date
	 */
	public abstract boolean isPrecise();
	
	/**
	 * Get the date of this object
	 */
	public Date getDate() {
		return _date;
	}
	
	/**
	 * Convert this date to timestamp
	 */
	public long timestamp() {
		return _date.getTime();
	}
	
	/**
	 * Convert a Date object to Calendar object
	 *
	 * @param date
	 *   The Date object to convert to Calendar
	 */
	protected static Calendar convertDateToCalendar(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal;
	}
}