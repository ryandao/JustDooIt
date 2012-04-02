package parser.parsers;

import java.util.Date;

import org.codehaus.jparsec.Parser;
import org.codehaus.jparsec.functors.Map;
import org.codehaus.jparsec.functors.Pair;

import static org.codehaus.jparsec.Scanners.stringCaseInsensitive;
import static org.codehaus.jparsec.Parsers.sequence;
import static org.codehaus.jparsec.Parsers.longest;
import static org.codehaus.jparsec.Parsers.tuple;
import static org.codehaus.jparsec.Parsers.or;

import static parser.parsers.DateParsers.SHORT_DATE;
import static parser.parsers.DateParsers.WEEKDAY_DATE;
import static parser.parsers.DateTypeParsers.DATETYPE;
import static parser.parsers.DateTypeParsers.DATE_FROM_TIME;
import static parser.parsers.DateTypeParsers.SHORT_DATE_AND_TIME;
import static parser.parsers.DateTypeParsers.WEEKDAY_AND_TIME;
import static parser.parsers.Identifiers.DASH;
import static parser.parsers.Identifiers.PREFIX_AT;
import static parser.parsers.Identifiers.PREFIX_BY;
import static parser.parsers.Identifiers.PREFIX_FROM;
import static parser.parsers.Identifiers.WHITESPACE;
import static parser.parsers.TimeParsers.TIME;
import static parser.util.Mappers.CHECK_ORDER;
import static parser.util.Mappers.TO_BETWEEN;
import static parser.util.Mappers.TO_BY;
import static parser.util.Mappers.TO_FIXPOINT;
import static parser.util.Mappers.TO_FROM;
import static parser.util.Mappers.TO_FUTURE_WITH_DATE_FIXED;
import static parser.util.Mappers.TO_FUTURE_WITH_TIME_FIXED;
import static parser.util.Mappers.TO_FUTURE_WITH_WEEKDAY_FIXED;
import static parser.util.Mappers.TO_ON_THE_DAY;
import static parser.util.Mappers.TO_PRECISELY;

import commons.datetypes.DateType;
import commons.datetypes.OnTheDay;
import commons.timeframes.TimeFrame;
import commons.timeframes.Whenever;

/**
 * TimeFrameParsers contains various Parser objects of TimeFrame.
 * 
 */
public class TimeFrameParsers {
	
	/** Matches a fixed point time frame and returns that FixPoint. */
	public static final Parser<TimeFrame> FIXPOINT = 
		
		sequence(
			PREFIX_AT.optional(),
			WHITESPACE,
			DATETYPE
		).map(TO_FIXPOINT);
	
	/** Matches a by time frame and returns that By. */
	public static final Parser<TimeFrame> BY = 
		
		sequence(
			PREFIX_BY, 
			WHITESPACE, 
			DATETYPE
		).map(TO_BY);
	
	/** Matches a from time frame and returns that From. */
	public static final Parser<TimeFrame> FROM = 
		
		sequence(
			PREFIX_FROM, 
			WHITESPACE, 
			DATETYPE
		).map(TO_FROM);
	
	/** Matches a whenever time frame and returns that Whenever. */
	public static final Parser<TimeFrame> WHENEVER = stringCaseInsensitive("whenever").optional().retn((TimeFrame)(new Whenever()));
	
	/** Matches a between time frame of the form "date from time". */
	public static final Parser<TimeFrame> BETWEEN_DATE_FROM_TIME = DATE_FROM_TIME.map(new Map<DateType, Pair<DateType, DateType>>() {

		@Override
		public Pair<DateType, DateType> map(DateType from) {
			
			Date date = from.getDate();
			DateType day = new OnTheDay(date);
			
			return new Pair<DateType, DateType>(from, day);
		}
		
	}).map(TO_BETWEEN);
	
	/** Matches a between time frame of the form "date from time to time". */
	public static final Parser<TimeFrame> BETWEEN_DATE_FROM_TIME_TO_TIME = 
		
		tuple(
			DATE_FROM_TIME, 
			sequence(
					WHITESPACE, 
					PREFIX_BY.source().or(DASH), 
					WHITESPACE, TIME.map(TO_PRECISELY)
				)
		).map(TO_FUTURE_WITH_TIME_FIXED).map(TO_BETWEEN);
	
	/** Matches a between time frame with specified end date, but not end date year, and returns that Between. */
	public static final Parser<TimeFrame> BETWEEN_FUTURE_DATE = 
		
		tuple(
			sequence(
					PREFIX_FROM.followedBy(WHITESPACE).optional(), 
					DATETYPE
				), 
			sequence(
					WHITESPACE, 
					PREFIX_BY.source().or(DASH), 
					WHITESPACE, 
					or(SHORT_DATE_AND_TIME, SHORT_DATE.map(TO_ON_THE_DAY))
				)
		).map(TO_FUTURE_WITH_DATE_FIXED).map(TO_BETWEEN);
	
	/** Matches a between time frame with specified end weekday, and returns that Between. */
	public static final Parser<TimeFrame> BETWEEN_FUTURE_WEEKDAY = 
		
		tuple(
			sequence(
				PREFIX_FROM.followedBy(WHITESPACE).optional(), 
				DATETYPE
			), 
			sequence(
				WHITESPACE, 
				PREFIX_BY.source().or(DASH), 
				WHITESPACE, 
				or(
					WEEKDAY_AND_TIME, 
					WEEKDAY_DATE.map(TO_ON_THE_DAY)
				)
			)
		).map(TO_FUTURE_WITH_WEEKDAY_FIXED).map(TO_BETWEEN);
	
	/** Matches a between time frame with specified end time, and returns that Between. */
	public static final Parser<TimeFrame> BETWEEN_FUTURE_TIME = 
		
		tuple(
			sequence(
				PREFIX_FROM.followedBy(WHITESPACE).optional(), 
				DATETYPE
			), 
			sequence(
				WHITESPACE, 
				PREFIX_BY.source().or(DASH), 
				WHITESPACE, 
				TIME.map(TO_PRECISELY)
			)
		).map(TO_FUTURE_WITH_TIME_FIXED).map(TO_BETWEEN);
	
	/** Matches any other between time frame and returns that Between. */
	public static final Parser<TimeFrame> BETWEEN = 
		
		tuple(
			sequence(
				PREFIX_FROM.followedBy(WHITESPACE).optional(), 
				DATETYPE
			), 
			sequence(
				WHITESPACE, 
				PREFIX_BY.source().or(DASH), 
				WHITESPACE, 
				DATETYPE
			)
		).next(CHECK_ORDER).map(TO_BETWEEN);
	
	/** Matches any time frame and returns that TimeFrame. */
	@SuppressWarnings("unchecked") public static final Parser<TimeFrame> TIMEFRAME = 
		
		longest(
			BY, 
			FROM, 
			BETWEEN_DATE_FROM_TIME, 
			BETWEEN_DATE_FROM_TIME_TO_TIME, 
			BETWEEN_FUTURE_TIME, 
			BETWEEN_FUTURE_DATE, 
			BETWEEN_FUTURE_WEEKDAY, 
			BETWEEN, 
			WHENEVER, 
			FIXPOINT
		);

}
