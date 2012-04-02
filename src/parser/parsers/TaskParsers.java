package parser.parsers;

import org.codehaus.jparsec.Parser;

import static org.codehaus.jparsec.Parsers.tuple;
import static org.codehaus.jparsec.Parsers.or;
import static org.codehaus.jparsec.Scanners.ANY_CHAR;
import static org.codehaus.jparsec.Parsers.EOF;

import commons.tasks.Task;
import static parser.parsers.Identifiers.BAR;
import static parser.parsers.Identifiers.COMMA;
import static parser.parsers.Identifiers.DASH;
import static parser.parsers.Identifiers.WHITESPACE;
import static parser.parsers.TimeFrameParsers.TIMEFRAME;
import static parser.util.Mappers.TO_TASK;
/**
 * TaskParsers contains a Parser of Task.
 * 
 */
public class TaskParsers {
	
	/** Matches a text describing a task and returns a Task object. */
	public static final Parser<Task> TASK = 
		
		tuple(
			TIMEFRAME.followedBy(
				or(
					COMMA, 
					DASH, 
					WHITESPACE
				)
			), 
			ANY_CHAR.many1().followedBy(EOF).source()
		).map(TO_TASK);
	
	/** Matches a text describing a task, with a strict separation using "|", and returns a Task object. */
	public static final Parser<Task> STRICT_TASK = 
		
		tuple(
			TIMEFRAME.followedBy(BAR), 
			ANY_CHAR.many1().followedBy(EOF).source()
		).map(TO_TASK);

	
}
