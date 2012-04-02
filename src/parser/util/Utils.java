package parser.util;

import java.util.Calendar;
import java.util.Date;

/**
 * Utility class with useful methods for manipulating Date.
 * 
 */
public class Utils {
	
	/**
	 * Adds a number of day to the date. 
	 * 
	 * @param date   The date to add days.
	 * @param number The number of days to add.
	 * @return The Date number of days after date.
	 */
	public static Date datePlusDays(Date date, int number) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, number);
		return calendar.getTime();
	}
	
	/**
	 * Returns the next Date with a specified weekday.
	 * 
	 * @param date    The reference Date.
	 * @param weekDay The day of the week.
	 * @return The next Date after, or equal to date, with weekDay as week day.
	 */
	public static Date nextDateWithWeekDay(Date date, int weekDay) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int previousWeekDay = calendar.get(Calendar.DAY_OF_WEEK);
		int difference = (weekDay - previousWeekDay + 7) % 7;
		calendar.add(Calendar.DAY_OF_MONTH, difference);
		return calendar.getTime();
	}
	
	/**
	 * Returns the Date, within the week of the reference Date, with a specified weekday.
	 * 
	 * @param date    The reference Date.
	 * @param weekDay The day of the week.
	 * @return The next Date the same week as date, with weekDay as week day.
	 */
	public static Date sameWeekWithWeekDay(Date date, int weekDay) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int previousWeekDay = calendar.get(Calendar.DAY_OF_WEEK);
		int difference = weekDay - previousWeekDay;
		calendar.add(Calendar.DAY_OF_MONTH, difference);
		return calendar.getTime();
	}
}
