package commons.datetypes;

import java.util.Calendar;
import java.util.Date;
import static utils.DateFormats.DATE_AND_TIME_FORMAT;
import static utils.DateFormats.TIME_FORMAT;

/**
 * A Precisely DateType considers the exact date & time
 */
public class Precisely extends DateType {

	public Precisely(Date date) {
		super(date);	
	}

	@Override
	public boolean before(Date date) {
		return _date.before(date);		
	}

	@Override
	public boolean after(Date date) {
		return _date.after(date);
	}

	@Override
	public boolean sameTime(Date date) {
		return (_date.compareTo(date) == 0);
	}

	@Override
	public boolean sameDay(Date date) {
		Calendar cal1 = convertDateToCalendar(_date);
		Calendar cal2 = convertDateToCalendar(date);
		
		return (cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
				cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)); 
	}

	@Override
	public boolean isPrecise() {	
		return true;
	}
	
	public String toString() {
		if(this.sameDay(new Date())) {
			return "Today, " + TIME_FORMAT.format(_date) ;
		}
		return DATE_AND_TIME_FORMAT.format(_date);
	}
}
