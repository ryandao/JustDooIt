package parser;

import java.util.List;

import commons.exceptions.ParserException;
import commons.tasks.Task;
import commons.timeframes.TimeFrame;

import static org.codehaus.jparsec.Parsers.EOF;

import static parser.parsers.Identifiers.IDS;
import static parser.parsers.TaskParsers.STRICT_TASK;
import static parser.parsers.TaskParsers.TASK;
import static parser.parsers.TimeFrameParsers.TIMEFRAME;
import static utils.logging.Logger.log;


/**
 * TodoParser is the component in charge of parsing user input into comprehensive output,
 * like Task, TimeFrame or list of integers.
 *
 */
public class TodoParser {
	
	/**
	 * Parses a text into a list of integers.
	 * 
	 * @param text The text to parse.
	 * @return The list of ids specified by the text.
	 * @throws ParserException if the text is not the description of a list of integers.
	 */
	public List<Integer> getIds(String text) throws ParserException {
		assert(text != null);
		
		try {
			return IDS.followedBy(EOF).parse(text.trim());
		} catch(Exception e) {
			
			log("TodoParser.getIds failed for text : " + text);
			
			throw new ParserException(text + " is not a valid list of ids.");
		}
	}
	
	/**
	 * Parses a text into a TimeFrame.
	 * 
	 * @param text The text to parse.
	 * @return The TimeFrame specified by the text.
	 * @throws ParserException if the text is not the description of a TimeFrame.
	 */
	public TimeFrame getTimeFrame(String text) throws ParserException {
		assert(text != null);
		
		try {
			return TIMEFRAME.followedBy(EOF).parse(text.trim());
		} catch(Exception e) {
			
			log("TodoParser.getTimeFrame failed for text : " + text);
			
			throw new ParserException(text + " is not a valid timeframe.");
		}
	}
	
	/**
	 * Parses a text into a Task.
	 * 
	 * @param text The text to parse.
	 * @return The Task described by the text. 
	 * @throws ParserException if the text does not describe a Task.
	 */
	public Task getTask(String text) throws ParserException {
		assert(text != null);
		
		try {
			if(text.contains("|")) {
				return STRICT_TASK.parse(text.trim());
			} else {
				return TASK.parse(text.trim());
			}
		} catch(Exception e) {
			log("TodoParser.getTask failed for text : " + text);
			
			throw new ParserException(text + " is not a valid description of a task.");
		}
	}
}

