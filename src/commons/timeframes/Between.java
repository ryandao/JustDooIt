package commons.timeframes;

import java.util.Date;

import commons.datetypes.DateType;

import static utils.DateFormats.TIME_FORMAT;
import static utils.DateFormats.DATE_FORMAT;

/**
 * A TimeFrame set between 2 fixed points of time 
 */
public class Between extends TimeFrame {
	private DateType _start;
	private DateType _end;
		
	public Between(DateType start, DateType end) {
		_start = start;
		_end = end;
	}
	
	/**
	 * Get the start date of this TimeFrame
	 */
	public DateType getStart() {
		return _start; 
	}
	
	/**
	 * Get the end date of this TimeFrame	 
	 */
	public DateType getEnd() {
		return _end;
	}
	
	@Override
	public boolean startsBefore(Date date) {
		return (_start.before(date));
	}
	
	@Override
	public boolean startsAfter(Date date) {
		return (_start.after(date));
	}

	@Override
	public boolean startsTheSameDay(Date date) {
		return (_start.sameDay(date));
	}

	@Override
	public boolean endsAfter(Date date) {
		return (_end.after(date));
	}
	
	@Override 
	public boolean endsBefore(Date date) {
		return (_end.before(date));
	}

	@Override
	public boolean endsOnTheSameDay(Date date) {
		return (_end.sameDay(date));
	}
	
	@Override
	public boolean superimpose(TimeFrame timeFrame) {
		return !timeFrame.endsBefore(_start.getDate()) &&
			!timeFrame.startsAfter(_end.getDate());
	}	
	
	@Override
	public int getType() {		
		return TimeFrame.BETWEEN;
	}
	
	public String toString() {
		if(_start.sameDay(_end.getDate())) {
			if(_start.isPrecise() && !_end.isPrecise()) {
				return _end.toString() + " from " + TIME_FORMAT.format(_start.getDate());
			}
			
			if(!_start.isPrecise() && _end.isPrecise()) {
				return _start.toString() + " by " + TIME_FORMAT.format(_end.getDate());
			}
			
			if(_start.isPrecise() && _end.isPrecise()) {
				
				String day = "";
				
				if(_start.sameDay(new Date())) {
					day = "Today";
				}
				else {
					day = DATE_FORMAT.format(_start.getDate());
				}
				
				return day + 
					" from " + TIME_FORMAT.format(_start.getDate()) +
					" to " + TIME_FORMAT.format(_end.getDate());
			}
		}
		return _start.toString() + " to " + _end.toString();
	}
}