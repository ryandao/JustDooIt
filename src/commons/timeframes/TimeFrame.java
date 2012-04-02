package commons.timeframes;
import java.util.*;

public abstract class TimeFrame {
	
	public static final int FIXPOINT = 1;
	public static final int BY = 2;
	public static final int FROM = 3;
	public static final int BETWEEN = 4;
	public static final int WHENEVER = 5;
	
	/**
	 * Check if this TimeFrame starts before a certain date
	 *   
	 * @param date 
	 *   The date to check against
	 */
	public abstract boolean startsBefore(Date date);
	
	/**
	 * Check if this TimeFrame starts after a certain date
	 *   
	 * @param date 
	 *   The date to check against
	 */
	public abstract boolean startsAfter(Date date);
	
	/**
	 * Check if this TimeFrame starts on the same day with a certain date
	 * 
	 * @param date
	 *   The date to check against
	 */
	public abstract boolean startsTheSameDay(Date date);
	
	/**
	 * Check if this TimeFrame ends after a certain date
	 * 
	 * @param date
	 *   The date to check against
	 */
	public abstract boolean endsAfter(Date date);
	
	/**
	 * Check if this TimeFrame ends before a certain date
	 * 
	 * @param date
	 *   The date to check against
	 */
	public abstract boolean endsBefore(Date date);
	
	/**
	 * Check if this TimeFrame ends on the same day with a certain date
	 * 
	 * @param date
	 *   The date to check against
	 */
	public abstract boolean endsOnTheSameDay(Date date);
	
	/**
	 * Check if a given TimeFrame is within this TimeFrame
	 */
	public abstract boolean superimpose(TimeFrame timeFrame);
	
	/**
	 * Get the correct TimeFrame type
	 *   1: FixPoint
	 *   2: By
	 *   3: From
	 *   4: Between
	 *   5L Whenever
	 */
	public abstract int getType();
}