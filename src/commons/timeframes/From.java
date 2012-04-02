package commons.timeframes;

import java.util.Date;

import commons.datetypes.DateType;

/**
 * A TimeFrame that starts from a certain point of time 
 * 
 */
public class From extends TimeFrame {
	private DateType _startDate;
	
	public From(DateType startDate) {
		_startDate = startDate;
	}
	
	/**
	 * Get the start date of this TimeFrame
	 */
	public DateType getDate() {
		return _startDate;
	}

	@Override
	public boolean startsBefore(Date date) {		
		return (_startDate.before(date));
	}
	
	@Override
	public boolean startsAfter(Date date) {		
		return (_startDate.after(date));
	}

	@Override
	public boolean startsTheSameDay(Date date) {		
		return (_startDate.sameDay(date));
	}

	@Override
	public boolean endsAfter(Date date) { 
		return true;
	}
	
	@Override
	public boolean endsBefore(Date date) { 
		return false;
	}

	@Override
	public boolean endsOnTheSameDay(Date date) {		
		return false;
	}
	
	@Override
	public boolean superimpose(TimeFrame timeFrame) {
		return !timeFrame.endsBefore(_startDate.getDate());
	}

	@Override
	public int getType() {		
		return TimeFrame.FROM;
	}
	
	public String toString() {
		return "From " + _startDate.toString();
	}
}
