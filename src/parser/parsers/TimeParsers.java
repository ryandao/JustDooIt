package parser.parsers;

import java.util.Calendar;
import java.util.Date;

import org.codehaus.jparsec.Parser;
import org.codehaus.jparsec.functors.Map;
import org.codehaus.jparsec.functors.Pair;
import org.codehaus.jparsec.functors.Tuple3;

import static org.codehaus.jparsec.Parsers.*;

import static parser.parsers.Identifiers.*;

/**
 * TimeParsers contains various Parser objects of Date, with relation to the time.
 * 
 */
public class TimeParsers {
	
	/** Matches hour and minute, as numbers, and returns a Date, set to today, with specified hour and minute, seconds set to 0. */
	public static final Parser<Date> SHORT_TIME = 
		
		tuple(
			HOUR_INT.followedBy(TIME_SEPARATOR), 
			MINUTES
		).map(new Map<Pair<Integer, Integer>, Date>() {

		@Override
		public Date map(Pair<Integer, Integer> from) {
			Calendar calendar = Calendar.getInstance();
			
			calendar.set(Calendar.HOUR_OF_DAY, from.a);
			calendar.set(Calendar.MINUTE, from.b);
			calendar.set(Calendar.SECOND, 0);
			
			return calendar.getTime();
		}
		
	});
	
	/** Matches hour, minute and second as numbers, and returns a Date, set to today, with specified hour, minute and second. */
	public static final Parser<Date> COMPLETE_TIME = 
		
		tuple(
			SHORT_TIME.followedBy(TIME_SEPARATOR), 
			SECONDS
		).map(new Map<Pair<Date, Integer>, Date>() {

		@Override
		public Date map(Pair<Date, Integer> from) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(from.a);
			calendar.set(Calendar.SECOND, from.b);
			
			return calendar.getTime();
		}
		
	});
	
	/** Matches hour followed by AM and returns a Date, set to today, with specified hour and minute and second set to 0. */
	public static final Parser<Date> ONLY_HOUR_AM = SHORT_HOUR_INT.followedBy(AM).map(new Map<Integer, Date>() {

		@Override
		public Date map(Integer from) {
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.HOUR_OF_DAY, from % 12);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			
			return calendar.getTime();
		}
		
	});
	
	/** Matches hour and minute, as numbers, with AM, and returns a Date, set to today, with specified hour and minute, seconds set to 0. */
	public static final Parser<Date> SHORT_TIME_AM = 
		
		tuple(
			SHORT_HOUR_INT.followedBy(TIME_SEPARATOR), 
			MINUTES
		).followedBy(AM).map(new Map<Pair<Integer, Integer>, Date>() {

		@Override
		public Date map(Pair<Integer, Integer> from) {
			Calendar calendar = Calendar.getInstance();
			
			calendar.set(Calendar.HOUR_OF_DAY, from.a % 12);
			calendar.set(Calendar.MINUTE, from.b);
			calendar.set(Calendar.SECOND, 0);
			
			return calendar.getTime();
		}
		
	});
	
	/** Matches hour, minute and second as numbers, with AM, and returns a Date, set to today, with specified hour, minute and second. */
	public static final Parser<Date> COMPLETE_TIME_AM = 
		
		tuple(
			SHORT_HOUR_INT.followedBy(TIME_SEPARATOR),
			MINUTES.followedBy(TIME_SEPARATOR),
			SECONDS.followedBy(AM)
		).map(new Map<Tuple3<Integer, Integer, Integer>, Date>() {

		@Override
		public Date map(Tuple3<Integer, Integer, Integer> from) {
			Calendar calendar = Calendar.getInstance();
			
			calendar.set(Calendar.HOUR_OF_DAY, from.a % 12);
			calendar.set(Calendar.MINUTE, from.b);
			calendar.set(Calendar.SECOND, from.c);
			
			return calendar.getTime();
		}
		
	});
	
	/** Matches hour followed by PM and returns a Date, set to today, with specified hour and minute and second set to 0. */
	public static final Parser<Date> ONLY_HOUR_PM = SHORT_HOUR_INT.followedBy(PM).map(new Map<Integer, Date>() {

		@Override
		public Date map(Integer from) {
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.HOUR_OF_DAY, 12 + (from % 12));
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			
			return calendar.getTime();
		}
		
	});
	
	/** Matches hour and minute, as numbers, with PM, and returns a Date, set to today, with specified hour and minute, seconds set to 0. */
	public static final Parser<Date> SHORT_TIME_PM = 
		
		tuple(
			SHORT_HOUR_INT.followedBy(TIME_SEPARATOR), 
			MINUTES
		).followedBy(PM).map(new Map<Pair<Integer, Integer>, Date>() {

		@Override
		public Date map(Pair<Integer, Integer> from) {
			Calendar calendar = Calendar.getInstance();
			
			calendar.set(Calendar.HOUR_OF_DAY, 12 + (from.a % 12));
			calendar.set(Calendar.MINUTE, from.b);
			calendar.set(Calendar.SECOND, 0);
			
			return calendar.getTime();
		}
		
	});
	
	/** Matches hour, minute and second as numbers, with PM, and returns a Date, set to today, with specified hour, minute and second. */
	public static final Parser<Date> COMPLETE_TIME_PM = 
		
		tuple(
			SHORT_HOUR_INT.followedBy(TIME_SEPARATOR),
			MINUTES.followedBy(TIME_SEPARATOR),
			SECONDS.followedBy(PM)
		).map(new Map<Tuple3<Integer, Integer, Integer>, Date>() {

		@Override
		public Date map(Tuple3<Integer, Integer, Integer> from) {
			Calendar calendar = Calendar.getInstance();
			
			calendar.set(Calendar.HOUR_OF_DAY, 12 + (from.a % 12));
			calendar.set(Calendar.MINUTE, from.b);
			calendar.set(Calendar.SECOND, from.c);
			
			return calendar.getTime();
		}
		
	});
	
	/** Matches a noun for midday and returns a Date, set to today, with hour set to 12:00. */
	public static final Parser<Date> MIDDAY_TIME = 
		
		sequence(
			WHITESPACE, 
			MIDDAY, 
			WHITESPACE
		).map(new Map<String, Date>() {

		@Override
		public Date map(String from) {
			Calendar calendar = Calendar.getInstance();
			
			calendar.set(Calendar.HOUR_OF_DAY, 12);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			
			return calendar.getTime();
		}
		
	});
	
	/** Matches a noun for midnight and returns a Date, set to today, with hour set to 23:59:59. */
	public static final Parser<Date> MIDNIGHT_TIME = 
		
		sequence(
			WHITESPACE, 
			MIDNIGHT, 
			WHITESPACE
		).map(new Map<String, Date>() {

		@Override
		public Date map(String from) {
			Calendar calendar = Calendar.getInstance();
			
			calendar.set(Calendar.HOUR_OF_DAY, 23);
			calendar.set(Calendar.MINUTE, 59);
			calendar.set(Calendar.SECOND, 59);
			
			return calendar.getTime();
		}
		
	});
	
	/** Matches a time and returns a Date, set to today, with specified time. */
	public static final Parser<Date> TIME = 
		
		or(
			MIDDAY_TIME.or(MIDNIGHT_TIME),
			ONLY_HOUR_AM, 
			ONLY_HOUR_PM,
			SHORT_TIME_AM,
			SHORT_TIME_PM,
			COMPLETE_TIME_AM,
			COMPLETE_TIME_PM,
			COMPLETE_TIME, 
			SHORT_TIME
		);
}
