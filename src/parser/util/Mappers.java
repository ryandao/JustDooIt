package parser.util;

import static org.codehaus.jparsec.Parsers.constant;
import static org.codehaus.jparsec.Parsers.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.codehaus.jparsec.Parser;
import org.codehaus.jparsec.functors.Map;
import org.codehaus.jparsec.functors.Pair;


import commons.datetypes.DateType;
import commons.datetypes.OnTheDay;
import commons.datetypes.Precisely;
import commons.tasks.Task;
import commons.timeframes.Between;
import commons.timeframes.By;
import commons.timeframes.FixPoint;
import commons.timeframes.From;
import commons.timeframes.TimeFrame;

/**
 * Mappers contains useful Map objects.
 * 
 */
public class Mappers {
	
	/**
	 * A Map that takes a Date and returns a DateType, with precision to the day.
	 */
	public static final Map<Date, DateType> TO_ON_THE_DAY = new Map<Date, DateType>() {

		@Override
		public DateType map(Date from) {
			return new OnTheDay(from);
		}
		
	};
	
	/**
	 * A Map that takes a Date and returns a DateType, with precision to the second.
	 */
	public static final Map<Date, DateType> TO_PRECISELY = new Map<Date, DateType>() {

		@Override
		public DateType map(Date from) {
			return new Precisely(from);
		}
		
	};
	
	/**
	 * A Map that takes two Date objects, and combine them, the first giving the date, the second the time.
	 */
	public static final Map<Pair<Date, Date>, Date> COMBINE_DATE_TIME = new Map<Pair<Date, Date>, Date>() {

		@Override
		public Date map(Pair<Date, Date> from) {
			Calendar date = Calendar.getInstance();
			Calendar time = Calendar.getInstance();
			date.setTime(from.a);
			time.setTime(from.b);
			
			for(Integer field : Arrays.asList(Calendar.HOUR_OF_DAY, Calendar.MINUTE, Calendar.SECOND)) {
				date.set(field, time.get(field));	
			}
			
			return date.getTime();
		}
		
	};
	
	/**
	 * Maps an Integer to a Parser that returns that integer if it is within range, fails otherwise. 
	 *
	 * @param a The lower bound, inclusive.
	 * @param b The upper bound, inclusive.
	 * @return A Map that gives a Parser given an integer.
	 */
	public static Map<Integer, Parser<Integer>> inRange(final int a, final int b) {
		return new Map<Integer, Parser<Integer>>() {

			@Override
			public Parser<Integer> map(Integer from) {
				if(from < a || from > b) {
					return fail("not in range");
				}
				return constant(from);
			}
		
		};
	}
	
	/**
	 * Maps a Date to a new Date with a specified number of days later.
	 * 
	 * @param number The number of days to add to the Date.
	 * @return A Map that adds number days to a Date.
	 */
	public static Map<Date, Date> plusDays(int number) {
		final int NUMBER = number;
		
		return new Map<Date, Date>() {

			@Override
			public Date map(Date from) {
				return Utils.datePlusDays(from, NUMBER);
			}
			
		};
	}
	
	/**
	 * A Map that takes a Date and an Integer and return the next Date that has that Integer as weekday number.
	 */
	public static final Map<Pair<Date, Integer>, Date> FOLLOWING_WEEKDAY = new Map<Pair<Date, Integer>, Date>() {

		@Override
		public Date map(Pair<Date, Integer> from) {
			
			return Utils.nextDateWithWeekDay(from.a, from.b);
		}
		
	};
	
	/**
	 * A Map that takes an Integer and returns the next Date that has that Integer as weekday number.
	 */
	public static final Map<Integer, Date> NEXT_WEEKDAY = new Map<Integer, Date>() {
		
		@Override
		public Date map(Integer weekDay) {
			
			return Utils.nextDateWithWeekDay(new Date(), weekDay);
		}
	};
	
	/**
	 * A Map that takes an Integer and return the Date within the week that has that Integer as weekday number.
	 */
	public static final Map<Integer, Date> THIS_WEEKDAY = new Map<Integer, Date>() {

		@Override
		public Date map(Integer weekDay) {
			return Utils.sameWeekWithWeekDay(new Date(), weekDay);
		}
		
	};
	
	/**
	 * A Map that add a week to a Date if it is today.
	 */
	public static final Map<Date, Date> NOT_TODAY = new Map<Date, Date>() {

		@Override
		public Date map(Date other) {
			Calendar now = Calendar.getInstance();
			Calendar then = Calendar.getInstance();
			then.setTime(other);
			
			if(now.get(Calendar.DAY_OF_YEAR) == then.get(Calendar.DAY_OF_YEAR) &&
					now.get(Calendar.YEAR) == then.get(Calendar.YEAR)) {
				then.add(Calendar.DAY_OF_MONTH, 7);
			}
			
			return then.getTime();
		}
		
	};
	
	/**
	 * A Map that takes an Integer and a Date and returns a Parser that returns that Date if the weekday correspond to the integer,
	 * fails otherwise.
	 */
	public static final Map<Pair<Integer, Date>, Parser<Date>> WEEKDAY_CHECK = new Map<Pair<Integer, Date>, Parser<Date>>() {

		@Override
		public Parser<Date> map(Pair<Integer, Date> from) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(from.b);
			
			if(calendar.get(Calendar.DAY_OF_WEEK) == from.a) {
				return constant(from.b);
			}
			
			return fail("weekday does not match with date");
		}
		
	};
	
	/**
	 * A Map that takes a DateType and returns a TimeFrame, By.
	 */
	public static final Map<DateType, TimeFrame> TO_BY = new Map<DateType, TimeFrame>() {

		@Override
		public TimeFrame map(DateType from) {
			return new By(from);
		}
		
	};
	
	/**
	 * A Map that takes a DateType and returns a TimeFrame, From.
	 */
	public static final Map<DateType, TimeFrame> TO_FROM = new Map<DateType, TimeFrame>() {

		@Override
		public TimeFrame map(DateType from) {
			return new From(from);
		}
		
	};
	
	/**
	 * A Map that takes a DateType and returns a TimeFrame, FixPoint.
	 */
	public static final Map<DateType, TimeFrame> TO_FIXPOINT = new Map<DateType, TimeFrame>() {

		@Override
		public TimeFrame map(DateType from) {
			return new FixPoint(from);
		}
		
	};
	
	/**
	 * A Map that takes two DateType objects and returns a TimeFrame, Between.
	 */
	public static final Map<Pair<DateType, DateType>, TimeFrame> TO_BETWEEN = new Map<Pair<DateType, DateType>, TimeFrame>() {

		@Override
		public TimeFrame map(Pair<DateType, DateType> from) {
			return new Between(from.a, from.b);
		}
		
	};
	
	public static final Map<Pair<DateType, DateType>, Pair<DateType, DateType>> TO_FUTURE_WITH_TIME_FIXED = 
		new Map<Pair<DateType, DateType>, Pair<DateType, DateType>>() {

			@Override
			public Pair<DateType, DateType> map(Pair<DateType, DateType> from) {
				
				Date refDate = from.a.getDate();
				Date refTime = from.b.getDate();
				
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(refDate);
				
				Calendar calendarDate = Calendar.getInstance();
				calendarDate.setTime(refDate);
				
				Calendar calendarTime = Calendar.getInstance();
				calendarTime.setTime(refTime);
				
				calendar.setTime(refDate);
				calendar.set(Calendar.HOUR_OF_DAY, calendarTime.get(Calendar.HOUR_OF_DAY));
				calendar.set(Calendar.MINUTE, calendarTime.get(Calendar.MINUTE));
				calendar.set(Calendar.SECOND, calendarTime.get(Calendar.SECOND));
				
				if(from.a.isPrecise() && !calendar.after(calendarDate)) {
					calendar.add(Calendar.DAY_OF_MONTH, 1);
				}
				
				Date date = calendar.getTime();
				
				DateType time = new Precisely(date);
				
				return new Pair<DateType, DateType>(from.a, time);
			}
		
	};
	
	public static final Map<Pair<DateType, DateType>, Pair<DateType, DateType>> TO_FUTURE_WITH_WEEKDAY_FIXED = 
		new Map<Pair<DateType, DateType>, Pair<DateType, DateType>>() {

			@Override
			public Pair<DateType, DateType> map(Pair<DateType, DateType> from) {
				
				Date startDate = from.a.getDate();
				Date endDate = from.b.getDate();
				
				Calendar startCalendar = Calendar.getInstance();
				startCalendar.setTime(startDate);
				
				Calendar endCalendar = Calendar.getInstance();
				endCalendar.setTime(endDate);
				
				if(!startDate.before(endDate)) {
					
					int difference = ((endCalendar.get(Calendar.DAY_OF_WEEK) - startCalendar.get(Calendar.DAY_OF_WEEK) + 6) % 7) + 1;
					
					endCalendar.set(Calendar.YEAR, startCalendar.get(Calendar.YEAR));
					endCalendar.set(Calendar.MONTH, startCalendar.get(Calendar.MONTH));
					endCalendar.set(Calendar.DAY_OF_MONTH, startCalendar.get(Calendar.DAY_OF_MONTH));
					endCalendar.add(Calendar.DAY_OF_MONTH, difference);
				}
				
				Date date = endCalendar.getTime();
				
				DateType end;
				
				if(from.b.isPrecise()) {
					end = new Precisely(date);
				}
				else {
					end = new OnTheDay(date);
				}
				
				return new Pair<DateType, DateType>(from.a, end);
			}
		
	};
	
	
	public static final Map<Pair<DateType, DateType>, Pair<DateType, DateType>> TO_FUTURE_WITH_DATE_FIXED = 
		new Map<Pair<DateType, DateType>, Pair<DateType, DateType>>() {

			@Override
			public Pair<DateType, DateType> map(Pair<DateType, DateType> from) {
				
				Date startDate = from.a.getDate();
				Date endDate = from.b.getDate();
				
				Calendar startCalendar = Calendar.getInstance();
				startCalendar.setTime(startDate);
				
				Calendar endCalendar = Calendar.getInstance();
				endCalendar.setTime(endDate);
				
				endCalendar.set(Calendar.YEAR, startCalendar.get(Calendar.YEAR));
				
				if(!endCalendar.after(startCalendar)) {
					
					endCalendar.add(Calendar.YEAR, 1);
				}
				
				Date date = endCalendar.getTime();
				
				DateType end;
				
				if(from.b.isPrecise()) {
					end = new Precisely(date);
				}
				else {
					end = new OnTheDay(date);
				}
				
				return new Pair<DateType, DateType>(from.a, end);
			}
		
	};
	
	public static final Map<Pair<DateType, DateType>, Parser<Pair<DateType, DateType>>> CHECK_ORDER 
		= new Map<Pair<DateType, DateType>, Parser<Pair<DateType, DateType>>>() {

			@Override
			public Parser<Pair<DateType, DateType>> map(
					Pair<DateType, DateType> from) {
				
				Date startDate = from.a.getDate();
				
				if(!from.a.isPrecise()) {
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(startDate);
					calendar.set(Calendar.HOUR_OF_DAY, 0);
					calendar.set(Calendar.MINUTE, 0);
					calendar.set(Calendar.SECOND, 0);
					startDate = calendar.getTime();
				}
				
				if(!from.b.after(startDate)) {
					return fail("Timeframe impossible.");
				}
				
				return constant(from);
			}
	};
	
	public static final Map<Pair<TimeFrame, String>, Task> TO_TASK = new Map<Pair<TimeFrame, String>, Task>() {

		@Override
		public Task map(Pair<TimeFrame, String> from) {
			return new Task(from.b, from.a);
		}
		
	};
	
	public static final Map<Pair<Integer, Integer>, Parser<List<Integer>>> TO_LIST_PARSER = new Map<Pair<Integer, Integer>, Parser<List<Integer>>>() {

		@Override
		public Parser<List<Integer>> map(Pair<Integer, Integer> from) {
			
			if(from.b < from.a) {
				return constant((List<Integer>)new ArrayList<Integer>(0));
			}
			
			if(from.b - from.a > 10000) {
				return fail("Range too wide.");
			}
			
			List<Integer> list = new ArrayList<Integer>(from.b - from.a + 1);
			for(int i = from.a; i <= from.b; i++) {
				list.add(i);
			}
			return constant(list);
		}
		
	};
}
