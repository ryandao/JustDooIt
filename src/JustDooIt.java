import userinterface.CLILogic;

/**
 * Main class 
 */
public class JustDooIt {
	public static void main(String[] args) {
		CLILogic cliLogic = CLILogic.getInstance();
		cliLogic.executeUntilExit();		
	}
}
