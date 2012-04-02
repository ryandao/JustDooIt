package parser.parsers;


import org.codehaus.jparsec.Parser;
import static org.codehaus.jparsec.Parsers.tuple;
import static org.codehaus.jparsec.Parsers.sequence;
import static org.codehaus.jparsec.Parsers.longest;
import static org.codehaus.jparsec.Scanners.stringCaseInsensitive;

import static parser.parsers.DateParsers.DATE;
import static parser.parsers.DateParsers.SHORT_DATE;
import static parser.parsers.DateParsers.WEEKDAY_DATE;
import static parser.parsers.Identifiers.DATE_TIME_SEPARATOR;
import static parser.parsers.Identifiers.WHITESPACE;
import static parser.parsers.TimeParsers.TIME;
import static parser.util.Mappers.COMBINE_DATE_TIME;
import static parser.util.Mappers.TO_ON_THE_DAY;
import static parser.util.Mappers.TO_PRECISELY;

import commons.datetypes.DateType;

/**
 * DateTypeParsers contains Parsers of DateType.
 * 
 */
public class DateTypeParsers {
	
	/** Matches a short date and time. */
	public static final Parser<DateType> SHORT_DATE_AND_TIME = 
		
		tuple(
			SHORT_DATE.followedBy(DATE_TIME_SEPARATOR), 
			TIME
		).map(COMBINE_DATE_TIME).map(TO_PRECISELY);
	
	/** Matches a weekday and time. */
	public static final Parser<DateType> WEEKDAY_AND_TIME = 
		
		tuple(
			WEEKDAY_DATE.followedBy(DATE_TIME_SEPARATOR), 
			TIME
		).map(COMBINE_DATE_TIME).map(TO_PRECISELY);
	
	/** Matches a date and returns a DateType. */
	public static final Parser<DateType> ONLY_DATE = DATE.map(TO_ON_THE_DAY);
	
	/** Matches a time and returns a DateType. */
	public static final Parser<DateType> ONLY_TIME = TIME.map(TO_PRECISELY);
	
	
	/** Matches a date and time, separated by 'from' and returns a DateType. */
	public static final Parser<DateType> DATE_FROM_TIME = 
		
		tuple(
			DATE.followedBy(sequence(
				WHITESPACE, 
				stringCaseInsensitive("from"), 
				WHITESPACE
			)),
			TIME
		).map(COMBINE_DATE_TIME).map(TO_PRECISELY);
	
	/** Matches a date and time and returns a DateType. */
	public static final Parser<DateType> DATE_AND_TIME = 
		
		tuple(
			DATE.followedBy(DATE_TIME_SEPARATOR), 
			TIME
		).map(COMBINE_DATE_TIME).map(TO_PRECISELY);
	
	@SuppressWarnings("unchecked")
	/** Matches any date and/or time, and returns a DateType.*/
	public static final Parser<DateType> DATETYPE = 
		
		longest(
			DATE_AND_TIME, 
			ONLY_DATE, 
			ONLY_TIME
		);
}
