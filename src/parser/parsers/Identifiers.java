package parser.parsers;

import java.text.DateFormatSymbols;

import static org.codehaus.jparsec.Parsers.or;
import static org.codehaus.jparsec.Parsers.longest;
import static org.codehaus.jparsec.Parsers.sequence;
import static org.codehaus.jparsec.Parsers.tuple;
import static org.codehaus.jparsec.Scanners.INTEGER;
import static org.codehaus.jparsec.Scanners.stringCaseInsensitive;
import static org.codehaus.jparsec.Scanners.isChar;
import static org.codehaus.jparsec.Scanners.string;
import static org.codehaus.jparsec.functors.Maps.TO_INTEGER;
import static org.codehaus.jparsec.pattern.CharPredicates.IS_DIGIT;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jparsec.Parser;
import org.codehaus.jparsec.functors.Unary;

import static parser.util.Mappers.TO_LIST_PARSER;
import static parser.util.Mappers.inRange;

/**
 * Identifiers contains various low level parsers.
 * 
 */
public class Identifiers {
	
	/** Matches a single digit. */
	public static final Parser<Void> DIGIT = isChar(IS_DIGIT);
	
	/** Matches an integer. */
	public static final Parser<Integer> INT = INTEGER.map(TO_INTEGER);
	
	/** Matches many white spaces. */
	public static final Parser<String> WHITESPACE = isChar(' ').many().source();
	
	/** Matches ',' between many white spaces. */
	public static final Parser<String> COMMA = isChar(',').between(WHITESPACE, WHITESPACE).source();
	
	/** Matches ':' between many white spaces. */
	public static final Parser<String> COLON = sequence(WHITESPACE, isChar(':'), WHITESPACE);
	
	/** Matches '.' between many white spaces. */
	public static final Parser<String> DOT = sequence(WHITESPACE, isChar('.'), WHITESPACE);
	
	/** Matches '..' between many white spaces. */
	public static final Parser<String> DOUBLE_DOTS = sequence(WHITESPACE, string(".."), WHITESPACE);
	
	/** Matches '/' between many white spaces. */
	public static final Parser<String> SLASH = sequence(WHITESPACE, isChar('/'), WHITESPACE);
	
	/** Matches '/' between many white spaces. */
	public static final Parser<String> BAR = sequence(WHITESPACE, isChar('|'), WHITESPACE);
	
	/** Matches '-' between many white spaces. */
	public static final Parser<String> DASH = sequence(WHITESPACE, isChar('-'), WHITESPACE);
	
	/** Matches 'am' between many white spaces. */
	public static final Parser<String> AM = sequence(WHITESPACE, stringCaseInsensitive("am"), WHITESPACE);
	
	/** Matches 'pm' between many white spaces. */
	public static final Parser<String> PM = sequence(WHITESPACE, stringCaseInsensitive("pm"), WHITESPACE);
	
	/** Matches a date separator. */
	public static final Parser<String> DATE_SEPARATOR = or(DOT, SLASH, isChar(' ').followedBy(WHITESPACE).source());
	
	/** Matches a time separator. */
	public static final Parser<String> TIME_SEPARATOR = or(DOT, COLON);
	
	/** Matches 'at'. */
	public static final Parser<Void> PREFIX_AT = stringCaseInsensitive("at");
	
	/** Matches 'by' and synonyms. */
	public static final Parser<Void> PREFIX_BY = 
		
		or(
			stringCaseInsensitive("by"), 
			stringCaseInsensitive("before"), 
			stringCaseInsensitive("until"),
			stringCaseInsensitive("till"),
			stringCaseInsensitive("to")
		);
	
	/** Matches a date-time separator. */
	public static final Parser<String> DATE_TIME_SEPARATOR = 
		
		or(
			sequence(WHITESPACE, PREFIX_AT, WHITESPACE), 
			COMMA, 
			DASH, 
			WHITESPACE
		);
	
	/** Matches 'from' and synonyms. */
	public static final Parser<Void> PREFIX_FROM = 
		
		or(
			stringCaseInsensitive("from"), 
			stringCaseInsensitive("after")
		);
	
	/** Matches a range in form of the '3 .. 5'. */
	public static final Parser<List<Integer>> INT_RANGE = 
		
		tuple(
			INT,
			sequence(
				DOUBLE_DOTS, 
				INT
			)
		).next(TO_LIST_PARSER);
	
	/** Matches a list of integers, separated by commas. */
	public static final Parser<List<Integer>> INT_LIST = INT.sepBy(COMMA);
	
	/** Matches a list of integers, separated by comma or in form of a range. */
	public static final Parser<List<Integer>> IDS = or(INT_RANGE, INT_LIST);
	
	/** Matches an integer between 0 and 12. */
	public static final Parser<Integer> SHORT_HOUR_INT = INT.next(inRange(0, 12));
	
	/** Matches an integer between 0 and 24. */
	public static final Parser<Integer> HOUR_INT = INT.next(inRange(0, 24));
	
	/** Matches an integer between 0 and 59. */
	public static final Parser<Integer> MINUTES = INT.next(inRange(0, 59));
	
	/** Matches an integer between 0 and 59. */
	public static final Parser<Integer> SECONDS = MINUTES;
		
	/** Matches an integer between 1 and 31. */
	public static final Parser<Integer> DAY_INT = INT.next(inRange(1, 31));
	
	/** Matches an integer between 1 and 12. */
	public static final Parser<Integer> MONTH_INT = INT.next(inRange(1, 12));
	
	/** Matches a string for a month and returns the number of the month. */
	public static final Parser<Integer> MONTH = longest(monthParsersList());
	
	/** Matches a string for a week day and returns the number of that day. */
	public static final Parser<Integer> WEEKDAY = longest(weekDaysParsersList());
	
	/** Matches a string for tomorrow. */
	public static final Parser<Void> TOMORROW = stringCaseInsensitive("tomorrow");
	
	/** Matches a string for today. */
	public static final Parser<Void> TODAY = stringCaseInsensitive("today");
	
	/** Matches a string for yesterday. */
	public static final Parser<Void> YESTERDAY = stringCaseInsensitive("yesterday");
	
	/** Matches a string for midday. */
	public static final Parser<Void> MIDDAY = 
		
		or(
			stringCaseInsensitive("noon"),
			stringCaseInsensitive("midday")
		);
	
	/** Matches a string for midnight. */
	public static final Parser<Void> MIDNIGHT = stringCaseInsensitive("midnight");
	
	/** Matches a year in two decimals. */
	public static final Parser<Integer> SHORT_YEAR = DIGIT.times(2).source().map(TO_INTEGER).map(new Unary<Integer>() {

		@Override
		public Integer map(Integer from) {
			return from + 2000;
		}
		
	}).notFollowedBy(
		or(
			TIME_SEPARATOR, 
			AM, 
			PM
		)
	);

	/** Matches a year in four decimals. */
	public static final Parser<Integer> LONG_YEAR = DIGIT.times(4).source().map(TO_INTEGER);
	
	/** Matches a year. */
	public static final Parser<Integer> YEAR =  or(LONG_YEAR, SHORT_YEAR);
	
	
	/**
	 * Returns a list of Parsers of week days.
	 * 
	 * @return The list of Parsers of all days, including short versions.
	 */
	private static final List<Parser<Integer>> weekDaysParsersList() {
		ArrayList<Parser<Integer>> parsers = new ArrayList<Parser<Integer>>();
		
		String[] days = new DateFormatSymbols().getWeekdays();
		String[] shortDays = new DateFormatSymbols().getShortWeekdays();
		
		for(int i = 0; i < days.length; i++) {
			if(!days[i].equals("")) {
				parsers.add(stringCaseInsensitive(days[i]).retn(i));
			}
			if(!shortDays[i].equals("")) {
				parsers.add(stringCaseInsensitive(shortDays[i]).retn(i));
			}
		}
		
		return parsers;
	}
	
	/**
	 * Returns a list of Parsers of months.
	 * 
	 * @return The list of Parsers of all months, including short versions.
	 */
	private static final List<Parser<Integer>> monthParsersList() {
		ArrayList<Parser<Integer>> parsers = new ArrayList<Parser<Integer>>();
		
		String[] months = new DateFormatSymbols().getMonths();
		String[] shortMonths = new DateFormatSymbols().getShortMonths();
		
		
		for(int i = 0; i < months.length; i++) {
			
			if(!months[i].equals("")) {
				parsers.add(stringCaseInsensitive(months[i]).retn(i + 1));
			}
			if(!shortMonths[i].equals("")) {
				parsers.add(stringCaseInsensitive(shortMonths[i]).retn(i + 1));
			}
		}
		
		return parsers;
	}
}
