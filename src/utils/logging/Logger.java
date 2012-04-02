package utils.logging;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Date;

/**
 * Utility class for logging. 
 * The only method, log, can be imported statically.
 * 
 */
public class Logger {
	
	/** The output stream to the log file. */
	private static PrintStream stream;
	
	// Static initialization of the file.
	static {
		try {
			stream = new PrintStream(new FileOutputStream(new File("todo.log"), true));
		} catch(FileNotFoundException e) {
			System.err.println("Impossible to open log file");
			System.exit(1);
		}
	}
	
	/**
	 * Logs some informations to the log file.
	 * The date is always put before the information, 
	 * no need to provide it yourself.
	 * 
	 * @param infos The text to log.
	 */
	public static void log(String infos) {
		infos = "[" + new Date() + "] " + infos;
		stream.println(infos);
	}
}
