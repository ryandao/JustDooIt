package commons.timeframes;

import java.util.Date;

import commons.datetypes.DateType;

/**
 * A TimeFrame that must end by a certain point of time.
 * 
 */
public class By extends TimeFrame {
	private DateType _endDate; 
	
	/**
	 * Get the end date of this TimeFrame
	 */
	public By(DateType endDate) {
		_endDate = endDate;
	}
	
	public DateType getDate() {
		return _endDate; 
	}

	@Override
	public boolean startsBefore(Date date) {
		return true;
	}
	
	@Override
	public boolean startsAfter(Date date) {
		return false;
	}

	@Override
	public boolean startsTheSameDay(Date date) {
		return false;
	}

	@Override
	public boolean endsAfter(Date date) {
		return (_endDate.after(date));
	}
	
	@Override
	public boolean endsBefore(Date date) {
		return (_endDate.before(date));
	}

	@Override
	public boolean endsOnTheSameDay(Date date) {
		return (_endDate.sameDay(date));	
	}
	
	@Override
	public boolean superimpose(TimeFrame timeFrame) {
		return !timeFrame.startsAfter(_endDate.getDate());
	}

	@Override
	public int getType() {
		return TimeFrame.BY;
	}
	
	public String toString() {
		return "By " + _endDate.toString();
	}
}
