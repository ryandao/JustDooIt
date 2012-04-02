package parser.parsers;

import java.util.Calendar;
import java.util.Date;

import org.codehaus.jparsec.Parser;
import org.codehaus.jparsec.functors.Map;
import org.codehaus.jparsec.functors.Pair;
import org.codehaus.jparsec.functors.Tuple3;

import static org.codehaus.jparsec.Scanners.stringCaseInsensitive;

import static org.codehaus.jparsec.Parsers.*;

import static parser.parsers.Identifiers.*;
import static parser.util.Mappers.NEXT_WEEKDAY;
import static parser.util.Mappers.THIS_WEEKDAY;
import static parser.util.Mappers.WEEKDAY_CHECK;
import static parser.util.Mappers.plusDays;

/**
 * DateParsers contains various Parsers of Date, with precision to the day.
 * 
 */
public class DateParsers {
	
	/** Matches today and returns a Date. */
	public static final Parser<Date> TODAY_DATE = TODAY.retn(new Date());
	
	/** Matches tomorrow and returns a Date. */
	public static final Parser<Date> TOMORROW_DATE = TOMORROW.retn(new Date()).map(plusDays(1));
	
	/** Matches yesterday and returns a Date. */
	public static final Parser<Date> YESTERDAY_DATE = YESTERDAY.retn(new Date()).map(plusDays(-1));
	
	/** Matches a weekday and returns a Date. */
	public static final Parser<Date> WEEKDAY_DATE = WEEKDAY.map(NEXT_WEEKDAY);
	
	/** Matches 'next weekday' and returns a Date. */
	public static final Parser<Date> NEXT_WEEKDAY_DATE = 
		
		sequence(
			stringCaseInsensitive("next"), 
			WHITESPACE, 
			WEEKDAY
		).map(THIS_WEEKDAY).map(plusDays(7));
	
	/** Matches 'this weekday' and returns a Date. */
	public static final Parser<Date> THIS_WEEKDAY_DATE = 
		
		sequence(
			stringCaseInsensitive("this"), 
			WHITESPACE, 
			WEEKDAY
		).map(THIS_WEEKDAY);
	
	/** Matches a weekday next week and returns a Date. */
	public static final Parser<Date> WEEKDAY_NEXT_WEEK_DATE = 
		
		WEEKDAY.map(THIS_WEEKDAY).map(plusDays(7)).followedBy(
			
			sequence(
				WHITESPACE, 
				stringCaseInsensitive("next"), 
				WHITESPACE, 
				stringCaseInsensitive("week")
			)
		);
	
	/** Matches a short date, without the year, and returns a Date. */
	public static final Parser<Date> SHORT_DATE = 
		
		tuple(
			DAY_INT.followedBy(DATE_SEPARATOR), 
			or(
				MONTH_INT, 
				MONTH
			)
		).map(new Map<Pair<Integer, Integer>, Date>(){

		@Override
		public Date map(Pair<Integer, Integer> from) {
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.MONTH, from.b - 1);
			calendar.set(Calendar.DAY_OF_MONTH, from.a);
			return calendar.getTime();
		}
		 
	});
	
	/** Matches a short date, without the year, in the future, and returns a Date. */
	public static final Parser<Date> SHORT_DATE_FUTURE = 
		
		tuple(
			DAY_INT.followedBy(DATE_SEPARATOR), 
			or(
				MONTH_INT, 
				MONTH
			)
		).map(new Map<Pair<Integer, Integer>, Date>(){

		@Override
		public Date map(Pair<Integer, Integer> from) {
			Calendar calendar = Calendar.getInstance();
			Calendar now = Calendar.getInstance();
			calendar.set(Calendar.MONTH, from.b - 1);
			calendar.set(Calendar.DAY_OF_MONTH, from.a);
			
			if(now.after(calendar)) {
				calendar.add(Calendar.YEAR, 1);
				
				// Because of 29 February, which is mapped to 1 march if next year is a leap year.
				calendar.set(Calendar.MONTH, from.b - 1);
				calendar.set(Calendar.DAY_OF_MONTH, from.a);
			}
			
			return calendar.getTime();
		}
		 
	});
	
	/** Matches a complete date and returns a Date. */
	public static final Parser<Date> COMPLETE_DATE = 
		
		tuple(
			DAY_INT.followedBy(DATE_SEPARATOR), 
			or(
				MONTH_INT, 
				MONTH
			).followedBy(DATE_SEPARATOR), 
			YEAR
		).map(new Map<Tuple3<Integer, Integer, Integer>, Date>() {

		@Override
		public Date map(Tuple3<Integer, Integer, Integer> from) {
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.YEAR, from.c);
			calendar.set(Calendar.MONTH, from.b - 1);
			calendar.set(Calendar.DAY_OF_MONTH, from.a);
			return calendar.getTime();
		}
	});
	
	/** Matches weekday and date, returns a Date if the are compatible. */
	public static final Parser<Date> WEEKDAY_SPECIFIED_DATE = 
		
		tuple(
			WEEKDAY.followedBy(
				or(
					COMMA, 
					WHITESPACE
				)
			), 
			or(
				COMPLETE_DATE, 
				SHORT_DATE_FUTURE
			)
		).next(WEEKDAY_CHECK);
	
	@SuppressWarnings("unchecked")
	/** Matches any format of date and returns a Date. */
	public static final Parser<Date> DATE = 
		
		longest(
			COMPLETE_DATE, 
			SHORT_DATE_FUTURE, 
			TODAY_DATE, 
			TOMORROW_DATE, 
			YESTERDAY_DATE, 
			WEEKDAY_SPECIFIED_DATE, 
			NEXT_WEEKDAY_DATE, 
			THIS_WEEKDAY_DATE, 
			WEEKDAY_NEXT_WEEK_DATE, 
			WEEKDAY_DATE
		);
}
