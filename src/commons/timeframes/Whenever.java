package commons.timeframes;

import java.util.Date;

/**
 * A TimeFrame without any date & time
 * 
 */
public class Whenever extends TimeFrame {

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
		return true;
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
		return true;
	}

	@Override
	public int getType() {
		return TimeFrame.WHENEVER;
	}
	
	public String toString() {
		return "Whenever";
	}
}
