package commons.timeframes;

import java.util.Date;

import commons.datetypes.DateType;

/**
 * A TimeFrame set in one fixed point of time
 * 
 */
public class FixPoint extends TimeFrame {
	private DateType _fixPoint;

	public FixPoint(DateType fixPoint) {
		_fixPoint = fixPoint;		
	}
	
	/**
	 * Get the date of this TimeFrame 
	 */
	public DateType getDate() {
		return _fixPoint;
	}

	@Override
	public boolean startsBefore(Date date) {		
		return (_fixPoint.before(date));
	}
	
	@Override
	public boolean startsAfter(Date date) {		
		return endsAfter(date);
	}

	@Override
	public boolean startsTheSameDay(Date date) {
		return (_fixPoint.sameDay(date));
	}

	@Override
	public boolean endsAfter(Date date) {		
		return (_fixPoint.after(date));
	}
	
	@Override
	public boolean endsBefore(Date date) {		
		return startsBefore(date);
	}

	@Override
	public boolean endsOnTheSameDay(Date date) { 
		return (_fixPoint.sameDay(date));
	}	
	
	@Override
	public boolean superimpose(TimeFrame timeFrame) {
		return !timeFrame.startsAfter(_fixPoint.getDate()) && !timeFrame.endsBefore(_fixPoint.getDate());
	}

	@Override
	public int getType() {	
		return TimeFrame.FIXPOINT;
	}
	
	public String toString() {
		return _fixPoint.toString();
	}
}
