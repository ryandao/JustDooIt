package commons.datetypes;

import java.util.Calendar;
import java.util.Date;
import static utils.DateFormats.DATE_FORMAT;

/**
 * An OnTheDay DateType only considers date & time in day unit  
 */
public class OnTheDay extends DateType {

	public OnTheDay(Date date) {
		super(date);
	}

	@Override
	public boolean before(Date date) {
		return (_date.before(date) && ! sameDay(date));
	}

	@Override
	public boolean after(Date date) {
		return (_date.after(date) && ! sameDay(date));
	}

	@Override
	public boolean sameTime(Date date) {
		return sameDay(date);
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
		return false;
	}
	
	public String toString() {
		
		if(this.sameDay(new Date())) {
			return "Today";
		}
		
		return DATE_FORMAT.format(_date);
	}
}