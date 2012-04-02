package userinterface.actions;

import java.io.*;

import userinterface.CLILogic;
import userinterface.GraphicalInterface;
import logic.Logic;


/**
 * Action that displays the help file.
 * 
 */
public class HelpAction extends UIAction {
		
	private static String newLine = System.getProperty("line.separator");
	
	/**
	 * Sole constructor of DisplayAction.
	 * 
	 * @param ui
	 *            The user interface.
	 */
	public HelpAction(GraphicalInterface ui, Logic logic) {
		super(ui, logic, false);
	}

	@Override
	public void apply() {
		String helpText = "";
		try {
			FileInputStream fileInputStream = new FileInputStream(CLILogic.HELP_FILE_NAME);
			DataInputStream input = new DataInputStream(fileInputStream);
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(input));
			String currentLine = bufferedReader.readLine();
			helpText = printCurrentLine(helpText, currentLine, bufferedReader);
			input.close();
			
			_UI.displayHelpFile(helpText);
			
		} catch (FileNotFoundException e) {
			_UI.showFeedBackMessage(CLILogic.MESSAGE_HELP_FILE_NOT_FOUND);
		} catch (IOException e) {
			_UI.displayFeedBackMessage("Impossible to read help file");
		}	
	}
	
	public static String printCurrentLine(String helpText, String currentLine, BufferedReader bufferedReader) {
		try {
				while (currentLine != null) {
					helpText = helpText + newLine + currentLine;
					currentLine = bufferedReader.readLine();
				}			
		} catch (IOException e) {
			
		}
		
		return helpText;
	}
}