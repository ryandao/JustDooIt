package utils;

import java.text.SimpleDateFormat;

/**
 * Utility class. Contains date formats.
 *
 */
public class DateFormats {
	
	/** Format for complete date and time. */
	public static final SimpleDateFormat DATE_AND_TIME_FORMAT = new SimpleDateFormat("E d MMM yy, h:mm a");
	
	/** Format for date only. */
	public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("E d MMM yy");
	
	/** Format for time only. */
	public static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("h:mm a");
	
}
