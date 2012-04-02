
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import org.codehaus.jparsec.Parser;
import org.junit.Test;

import commons.datetypes.DateType;
import commons.tasks.Task;

import parser.TodoParser;
import parser.parsers.DateParsers;
import parser.parsers.DateTypeParsers;
import parser.parsers.Identifiers;
import parser.parsers.TimeParsers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class ParsersTest {
	
	/**
	 * Tests the parsing of Task.
	 */
	@Test public void testTask() {
		TodoParser tester = new TodoParser();
		
		try {
			Task task = tester.getTask("12 november 11, 8:30 pm birthday party !");
			assertEquals(task.getContent(), "birthday party !");
			
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.DAY_OF_MONTH, 12);
			calendar.set(Calendar.MONTH, Calendar.NOVEMBER);
			calendar.set(Calendar.YEAR, 2011);
			
			assertTrue(task.getTimeFrame().endsOnTheSameDay(calendar.getTime()));
			assertTrue(task.getTimeFrame().startsTheSameDay(calendar.getTime()));
		} catch(Exception e) {
			fail();
		}
		
		try {
			Task task = tester.getTask("before 12/11/11 16:15, remind friends to bring me presents.");
			assertEquals(task.getContent(), "remind friends to bring me presents.");
			
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.DAY_OF_MONTH, 12);
			calendar.set(Calendar.MONTH, Calendar.NOVEMBER);
			calendar.set(Calendar.YEAR, 2011);
			
			assertTrue(task.getTimeFrame().endsOnTheSameDay(calendar.getTime()));
			assertTrue(task.getTimeFrame().startsBefore(new Date()));
		} catch(Exception e) {
			fail();
		}
		
		try {
			Task task = tester.getTask("13/11/11, 3am to 7am, clean everything up.");
			assertEquals(task.getContent(), "clean everything up.");
			
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.DAY_OF_MONTH, 13);
			calendar.set(Calendar.MONTH, Calendar.NOVEMBER);
			calendar.set(Calendar.YEAR, 2011);
			
			assertTrue(task.getTimeFrame().endsOnTheSameDay(calendar.getTime()));
			assertTrue(task.getTimeFrame().startsTheSameDay(calendar.getTime()));
		} catch(Exception e) {
			fail();
		}
		
		try {
			Task task = tester.getTask("1 dec 11 from 9am to 12pm Set Theory Exam.");
			assertEquals(task.getContent(), "Set Theory Exam.");
			
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.DAY_OF_MONTH, 1);
			calendar.set(Calendar.MONTH, Calendar.DECEMBER);
			calendar.set(Calendar.YEAR, 2011);
			
			assertTrue(task.getTimeFrame().endsOnTheSameDay(calendar.getTime()));
			assertTrue(task.getTimeFrame().startsTheSameDay(calendar.getTime()));
		} catch(Exception e) {
			fail();
		}
		
		try {
			Task task = tester.getTask("12 nov 11 9pm to 3am be at your party.");
			assertEquals(task.getContent(), "be at your party.");
			
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.DAY_OF_MONTH, 12);
			calendar.set(Calendar.MONTH, Calendar.NOVEMBER);
			calendar.set(Calendar.YEAR, 2011);
			
			assertFalse(task.getTimeFrame().endsOnTheSameDay(calendar.getTime()));
			assertTrue(task.getTimeFrame().startsTheSameDay(calendar.getTime()));
		} catch(Exception e) {
			fail();
		}
		
		try {
			int year = Calendar.getInstance().get(Calendar.YEAR) + 3;
			
			Task task = tester.getTask("23 oct " + year + " to 25 oct do stuff");
			
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.DAY_OF_MONTH, 25);
			calendar.set(Calendar.MONTH, Calendar.OCTOBER);
			calendar.set(Calendar.YEAR, year);
			
			assertTrue(task.getTimeFrame().endsOnTheSameDay(calendar.getTime()));
			
		} catch(Exception e) {
			fail();
		}
		
		try {
			int year = Calendar.getInstance().get(Calendar.YEAR) + 3;
			
			Task task = tester.getTask("by 25 oct " + year + " do stuff");
			
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.DAY_OF_MONTH, 25);
			calendar.set(Calendar.MONTH, Calendar.OCTOBER);
			calendar.set(Calendar.YEAR, year);
			
			assertTrue(task.getTimeFrame().endsOnTheSameDay(calendar.getTime()));
			
		} catch(Exception e) {
			fail();
		}
		
		// Impossible time frame
		
		try {
			tester.getTask("from 12 nov 11, 21:00 to 12 nov 11, 20:59:00 | go back in time !");
			fail();
		} catch(Exception e) {
			
		}
		
		// Impossible time frame
		
		try {
			tester.getTask("from 12 nov 11 to 12 nov 08 | go back in time, much more !");
			fail();
		} catch(Exception e) {
			
		}
		
		// Incorrect date format
		
		try {
			tester.getTask("13 | do stuff");
			fail();
		} catch(Exception e) {
			
		}
		
		// Incorrect weekday specified
		
		try {
			tester.getTask("mon 29 oct 2011 | do stuff");
			fail();
		} catch(Exception e) {
			
		}
	}
	
	/**
	 * Tests the parsing of list of ids.
	 */
	@Test public void testIdList() {
		TodoParser tester = new TodoParser();
		
		try {
			assertEquals(tester.getIds(" 10  "), Arrays.asList(10));
		} catch(Exception e) {
			fail();
		}
		
		
		
		try {
			assertEquals(tester.getIds(" 5, 3, 6"), Arrays.asList(5, 3, 6));
		} catch(Exception e) {
			fail();
		}
		
		// Trailing comma
		
		try {
			tester.getIds(" 5, 3, 6, ");
			fail();
		} catch(Exception e) {
			
		}
		
		// White space between
		
		try {
			tester.getIds("5, 6, 9 10");
			fail();
		} catch(Exception e) {
			
		}
	}
	
	/**
	 * Tests the parsing of range of ids.
	 */
	@Test public void testIdRange() {
		TodoParser tester = new TodoParser();
		
		try {
			assertEquals(tester.getIds("3..2"), Arrays.asList());
		} catch (Exception e) {
			fail();
		}
		
		try {
			assertEquals(tester.getIds("2 ..2"), Arrays.asList(2));
		} catch (Exception e) {
			fail();
		}
		
		try {
			assertEquals(tester.getIds("1.. 3"), Arrays.asList(1, 2, 3));
		} catch (Exception e) {
			fail();
		}
		
		// List too long
		
		try {
			tester.getIds("1.. 300000");
			fail();
		} catch (Exception e) {
			
		}
	}
	
	/**
	 * Tests the parsing of months, as nouns.
	 */
	@Test public void testMonthName() {
		Parser<Integer> tester = Identifiers.MONTH;
		
		try {
			tester.parse("");
			fail();
		} catch(Exception e) {
			
		}
		
		
		try {
			assertEquals(tester.parse("jan"), new Integer(1));
		} catch(Exception e) {
			fail();
		}
		
		try {
			assertEquals(tester.parse("december"), new Integer(12));
		} catch(Exception e) {
			fail();
		}
		
		try {
			assertEquals(tester.parse("jun"), new Integer(6));
		} catch(Exception e) {
			fail();
		}
	}
	
	/**
	 * Tests the parsing of weekdays, as nouns.
	 */
	@Test public void testWeekDay() {
		Parser<Integer> tester = Identifiers.WEEKDAY;
		
		try {
			assertEquals(tester.parse("mon"), new Integer(Calendar.MONDAY));
		} catch(Exception e) {
			fail();
		}
		
		try {
			assertEquals(tester.parse("sunDay"), new Integer(Calendar.SUNDAY));
		} catch(Exception e) {
			fail();
		}
		
		try {
			assertEquals(tester.parse("thu"), new Integer(Calendar.THURSDAY));
		} catch(Exception e) {
			fail();
		}
		
		try {
			tester.parse("");
			fail();
		} catch(Exception e) {

		}
	}
	
	/**
	 * Tests the parsing of dates of the form DD/MM or DD/MM/YY or DD/MM/YYYY.
	 */
	@Test public void testDate() {
		Parser<Date> tester = DateParsers.DATE;
		
		try{
			Date date = tester.parse("12 nov 2011");
			
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			
			assertEquals(calendar.get(Calendar.YEAR), 2011);
			assertEquals(calendar.get(Calendar.MONTH), Calendar.NOVEMBER);
			assertEquals(calendar.get(Calendar.DAY_OF_MONTH), 12);
			
		} catch(Exception e) {
			fail();
		}
		
		try{
			Date date = tester.parse("13 11");
			
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			
			assertEquals(calendar.get(Calendar.MONTH), Calendar.NOVEMBER);
			assertEquals(calendar.get(Calendar.DAY_OF_MONTH), 13);
			
		} catch(Exception e) {
			fail();
		}
		
		try{
			Date date = tester.parse("13 nov");
			
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			
			assertEquals(calendar.get(Calendar.MONTH), Calendar.NOVEMBER);
			assertEquals(calendar.get(Calendar.DAY_OF_MONTH), 13);
			
		} catch(Exception e) {
			fail();
		}
		
		try{
			Date date = tester.parse("13/12/11");
			
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			
			assertEquals(calendar.get(Calendar.YEAR), 2011);
			assertEquals(calendar.get(Calendar.MONTH), Calendar.DECEMBER);
			assertEquals(calendar.get(Calendar.DAY_OF_MONTH), 13);
			
		} catch(Exception e) {
			fail();
		}
		
		try{
			Date date = tester.parse("1.02");
			
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			
			assertEquals(calendar.get(Calendar.MONTH), Calendar.FEBRUARY);
			assertEquals(calendar.get(Calendar.DAY_OF_MONTH), 1);
			
		} catch(Exception e) {
			fail();
		}
		
		// Invalid month
		
		try{
			tester.parse("3 13");
			fail();
		} catch(Exception e) {

		}
		
		// Invalid day
		
		try{
			tester.parse("0/12");
			fail();
		} catch(Exception e) {

		}
		
		// Invalid month
		
		try{
			tester.parse("21/0/12");
			fail();
		} catch(Exception e) {

		}
		
		// Invalid format
		
		try{
			tester.parse("11");
			fail();
		} catch(Exception e) {

		}
	}
	
	/**
	 * Tests the combination of weekday and date.
	 */
	@Test public void testWeekDayDate() {
		Parser<Date> tester = DateParsers.DATE;
		
		try{
			Date date = tester.parse("fri 4 nov 2011");
			
			Calendar tested = Calendar.getInstance();
			tested.setTime(date);
			
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.MONTH, Calendar.NOVEMBER);
			calendar.set(Calendar.YEAR, 2011);
			calendar.set(Calendar.DAY_OF_MONTH, 4);
			
			assertEquals(calendar.get(Calendar.DAY_OF_WEEK), Calendar.FRIDAY);
			assertEquals(tested.get(Calendar.DAY_OF_YEAR), calendar.get(Calendar.DAY_OF_YEAR));
			assertEquals(tested.get(Calendar.YEAR), calendar.get(Calendar.YEAR));
			
		} catch(Exception e) {
			fail();
		}
		
		
		// 4 Nov 2011 is a Friday, not a monday.
		
		try{
			tester.parse("mon 4 nov 2011");
			fail();
		} catch(Exception e) {

		}
		
	}
	
	/**
	 * Tests the parsing of time, with hour, minutes and seconds specified, followed by AM.
	 */
	@Test public void testCompleteTimeAM() {
		Parser<Date> tester = TimeParsers.COMPLETE_TIME_AM;
		Calendar calendar = Calendar.getInstance();
		try{
			Date date = tester.parse("12:30:00am");
			calendar.setTime(date);
			assertEquals(calendar.get(Calendar.HOUR_OF_DAY), 0);
			assertEquals(calendar.get(Calendar.MINUTE), 30);
			assertEquals(calendar.get(Calendar.SECOND), 0);
			
		} catch(Exception e) {
			fail();
		}
		
		try{
			Date date = tester.parse("0.0.0am");
			calendar.setTime(date);
			assertEquals(calendar.get(Calendar.HOUR_OF_DAY), 0);
			assertEquals(calendar.get(Calendar.MINUTE), 0);
			assertEquals(calendar.get(Calendar.SECOND), 0);
		} catch(Exception e) {
			fail();
		}
		
		try{
			Date date = tester.parse("6.59.59am");
			calendar.setTime(date);
			assertEquals(calendar.get(Calendar.HOUR_OF_DAY), 6);
			assertEquals(calendar.get(Calendar.MINUTE), 59);
			assertEquals(calendar.get(Calendar.SECOND), 59);
			
		} catch(Exception e) {
			fail();
		}
		
		// Time not existing
		
		try{
			tester.parse("13.00.00am");
			fail();	
		} catch(Exception e) {
			
		}
	}
	
	/**
	 * Tests the parsing of time, with hour, minutes and seconds specified, followed by PM.
	 */
	@Test public void testCompleteTimePM() {
		Parser<Date> tester = TimeParsers.COMPLETE_TIME_PM;
		Calendar calendar = Calendar.getInstance();
		try{
			Date date = tester.parse("12:30:00pm");
			calendar.setTime(date);
			assertEquals(calendar.get(Calendar.HOUR_OF_DAY), 12);
			assertEquals(calendar.get(Calendar.MINUTE), 30);
			assertEquals(calendar.get(Calendar.SECOND), 0);
			
		} catch(Exception e) {
			fail();
		}
		
		try{
			Date date = tester.parse("0.0.0pm");
			calendar.setTime(date);
			assertEquals(calendar.get(Calendar.HOUR_OF_DAY), 12);
			assertEquals(calendar.get(Calendar.MINUTE), 0);
			assertEquals(calendar.get(Calendar.SECOND), 0);
		} catch(Exception e) {
			fail();
		}
		
		try{
			Date date = tester.parse("6.59.59pm");
			calendar.setTime(date);
			assertEquals(calendar.get(Calendar.HOUR_OF_DAY), 18);
			assertEquals(calendar.get(Calendar.MINUTE), 59);
			assertEquals(calendar.get(Calendar.SECOND), 59);
			
		} catch(Exception e) {
			fail();
		}
		
		// Time not existing
		
		try{
			tester.parse("13.59.59pm");
			fail();	
		} catch(Exception e) {
			
		}
	}
	
	/**
	 * Tests the parsing of time, with hour and minutes specified, followed by AM.
	 */
	@Test public void shortTimeAM() {
		Parser<Date> tester = TimeParsers.SHORT_TIME_AM;
		Calendar calendar = Calendar.getInstance();
		
		try{
			Date date = tester.parse("12:30am");
			calendar.setTime(date);
			assertEquals(calendar.get(Calendar.HOUR_OF_DAY), 0);
			assertEquals(calendar.get(Calendar.MINUTE), 30);
			assertEquals(calendar.get(Calendar.SECOND), 0);
			
		} catch(Exception e) {
			fail();
		}
		
		try{
			Date date = tester.parse("7:59am");
			calendar.setTime(date);
			assertEquals(calendar.get(Calendar.HOUR_OF_DAY), 7);
			assertEquals(calendar.get(Calendar.MINUTE), 59);
			assertEquals(calendar.get(Calendar.SECOND), 0);
			
		} catch(Exception e) {
			fail();
		}
		
		try{
			Date date = tester.parse("0:0 am");
			calendar.setTime(date);
			assertEquals(calendar.get(Calendar.HOUR_OF_DAY), 0);
			assertEquals(calendar.get(Calendar.MINUTE), 0);
			assertEquals(calendar.get(Calendar.SECOND), 0);
			
		} catch(Exception e) {
			fail();
		}
		
		try{
			tester.parse("13.59am");
			fail();	
		} catch(Exception e) {
			
		}
	}
	
	/**
	 * Tests the parsing of time, with hour and minutes specified, followed by PM.
	 */
	@Test public void shortTimePM() {
		Parser<Date> tester = TimeParsers.SHORT_TIME_PM;
		Calendar calendar = Calendar.getInstance();
		
		try{
			Date date = tester.parse("12:30pm");
			calendar.setTime(date);
			assertEquals(calendar.get(Calendar.HOUR_OF_DAY), 12);
			assertEquals(calendar.get(Calendar.MINUTE), 30);
			assertEquals(calendar.get(Calendar.SECOND), 0);
			
		} catch(Exception e) {
			fail();
		}
		
		try{
			Date date = tester.parse("7:59pm");
			calendar.setTime(date);
			assertEquals(calendar.get(Calendar.HOUR_OF_DAY), 19);
			assertEquals(calendar.get(Calendar.MINUTE), 59);
			assertEquals(calendar.get(Calendar.SECOND), 0);
			
		} catch(Exception e) {
			fail();
		}
		
		try{
			Date date = tester.parse("0:0 pm");
			calendar.setTime(date);
			assertEquals(calendar.get(Calendar.HOUR_OF_DAY), 12);
			assertEquals(calendar.get(Calendar.MINUTE), 0);
			assertEquals(calendar.get(Calendar.SECOND), 0);
			
		} catch(Exception e) {
			fail();
		}
		
		try{
			tester.parse("13.59pm");
			fail();	
		} catch(Exception e) {
			
		}
	}
	
	/**
	 * Tests the parsing of time, with only hour specified, followed by AM.
	 */
	@Test public void onlyHourAM() {
		Parser<Date> tester = TimeParsers.ONLY_HOUR_AM;
		Calendar calendar = Calendar.getInstance();
		
		try{
			Date date = tester.parse("12 am");
			calendar.setTime(date);
			assertEquals(calendar.get(Calendar.HOUR_OF_DAY), 0);
			assertEquals(calendar.get(Calendar.MINUTE), 0);
			assertEquals(calendar.get(Calendar.SECOND), 0);
			
		} catch(Exception e) {
			fail();
		}
		
		try{
			Date date = tester.parse("00am");
			calendar.setTime(date);
			assertEquals(calendar.get(Calendar.HOUR_OF_DAY), 0);
			assertEquals(calendar.get(Calendar.MINUTE), 0);
			assertEquals(calendar.get(Calendar.SECOND), 0);
			
		} catch(Exception e) {
			fail();
		}
		
		try{
			Date date = tester.parse("5am");
			calendar.setTime(date);
			assertEquals(calendar.get(Calendar.HOUR_OF_DAY), 5);
			assertEquals(calendar.get(Calendar.MINUTE), 0);
			assertEquals(calendar.get(Calendar.SECOND), 0);
			
		} catch(Exception e) {
			fail();
		}
		
		try{
			tester.parse("13am");
			fail();
		} catch(Exception e) {
			
		}
	}
	
	/**
	 * Tests the parsing of time, with only hour specified, followed by PM.
	 */
	@Test public void onlyHourPM() {
		Parser<Date> tester = TimeParsers.ONLY_HOUR_PM;
		Calendar calendar = Calendar.getInstance();
		
		try{
			Date date = tester.parse("12 pm");
			calendar.setTime(date);
			assertEquals(calendar.get(Calendar.HOUR_OF_DAY), 12);
			assertEquals(calendar.get(Calendar.MINUTE), 0);
			assertEquals(calendar.get(Calendar.SECOND), 0);
			
		} catch(Exception e) {
			fail();
		}
		
		try{
			Date date = tester.parse("00pm");
			calendar.setTime(date);
			assertEquals(calendar.get(Calendar.HOUR_OF_DAY), 12);
			assertEquals(calendar.get(Calendar.MINUTE), 0);
			assertEquals(calendar.get(Calendar.SECOND), 0);
			
		} catch(Exception e) {
			fail();
		}
		
		try{
			Date date = tester.parse("5pm");
			calendar.setTime(date);
			assertEquals(calendar.get(Calendar.HOUR_OF_DAY), 17);
			assertEquals(calendar.get(Calendar.MINUTE), 0);
			assertEquals(calendar.get(Calendar.SECOND), 0);
			
		} catch(Exception e) {
			fail();
		}
		
		try{
			tester.parse("13pm");
			fail();
		} catch(Exception e) {
			
		}
	}
	
	/**
	 * Tests the parsing of time, of different format.
	 */
	@Test public void testTime() {
		Parser<Date> tester = TimeParsers.TIME;
		Calendar calendar = Calendar.getInstance();
		
		try{
			Date date = tester.parse("12 am");
			calendar.setTime(date);
			assertEquals(calendar.get(Calendar.HOUR_OF_DAY), 0);
			assertEquals(calendar.get(Calendar.MINUTE), 0);
			assertEquals(calendar.get(Calendar.SECOND), 0);
			
		} catch(Exception e) {
			fail();
		}
		
		try{
			Date date = tester.parse("00.30 am");
			calendar.setTime(date);
			assertEquals(calendar.get(Calendar.HOUR_OF_DAY), 0);
			assertEquals(calendar.get(Calendar.MINUTE), 30);
			assertEquals(calendar.get(Calendar.SECOND), 0);
			
		} catch(Exception e) {
			fail();
		}
		
		try{
			Date date = tester.parse("noon");
			calendar.setTime(date);
			assertEquals(calendar.get(Calendar.HOUR_OF_DAY), 12);
			assertEquals(calendar.get(Calendar.MINUTE), 0);
			assertEquals(calendar.get(Calendar.SECOND), 0);
			
		} catch(Exception e) {
			fail();
		}
		
		try{
			Date date = tester.parse("midnight");
			calendar.setTime(date);
			assertEquals(calendar.get(Calendar.HOUR_OF_DAY), 23);
			assertEquals(calendar.get(Calendar.MINUTE), 59);
			assertEquals(calendar.get(Calendar.SECOND), 59);
			
		} catch(Exception e) {
			fail();
		}
		
		try{
			Date date = tester.parse("23:30");
			calendar.setTime(date);
			assertEquals(calendar.get(Calendar.HOUR_OF_DAY), 23);
			assertEquals(calendar.get(Calendar.MINUTE), 30);
			assertEquals(calendar.get(Calendar.SECOND), 0);
			
		} catch(Exception e) {
			fail();
		}
		
		try{
			Date date = tester.parse("11:30pm");
			calendar.setTime(date);
			assertEquals(calendar.get(Calendar.HOUR_OF_DAY), 23);
			assertEquals(calendar.get(Calendar.MINUTE), 30);
			assertEquals(calendar.get(Calendar.SECOND), 0);
			
		} catch(Exception e) {
			fail();
		}
		
		try{
			Date date = tester.parse("11:59am");
			calendar.setTime(date);
			assertEquals(calendar.get(Calendar.HOUR_OF_DAY), 11);
			assertEquals(calendar.get(Calendar.MINUTE), 59);
			assertEquals(calendar.get(Calendar.SECOND), 0);
		} catch(Exception e) {
			fail();
		}
	}
	
	/**
	 * Tests the parsing of date and time.
	 */
	@Test public void testDateType() {
		Parser<DateType> tester = DateTypeParsers.DATETYPE;
		
		// Only time specified.
		
		try{
			DateType dateType = tester.parse("12 am");
			assertTrue(dateType.sameDay(new Date()));
			assertTrue(dateType.isPrecise());
		} catch(Exception e) {
			fail();
		}
		
		// Only date specified.
		
		try{
			DateType dateType = tester.parse("21 nov 2011");
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.DAY_OF_MONTH, 21);
			calendar.set(Calendar.MONTH, Calendar.NOVEMBER);
			calendar.set(Calendar.YEAR, 2011);
			assertTrue(dateType.sameDay(calendar.getTime()));
			assertTrue(!dateType.isPrecise());
		} catch(Exception e) {
			fail();
		}
		
		// Date and time specified.
		
		try{
			DateType dateType = tester.parse("21 nov 2011 noon");
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.DAY_OF_MONTH, 21);
			calendar.set(Calendar.MONTH, Calendar.NOVEMBER);
			calendar.set(Calendar.YEAR, 2011);
			assertTrue(dateType.sameDay(calendar.getTime()));
			assertTrue(dateType.isPrecise());
		} catch(Exception e) {
			fail();
		}
	}
}
