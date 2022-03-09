package main.domain.Game;

public class GameProtocol {

	
	/**
	 * @return human friendly explanations of the protocol
	 */
	public static String getHumanReadableProtocol() {
		return "Messages not modifying the game : <look> <help> <bye> Messages modifying the game : <dig X Y> <flag X Y> <deflag X Y>";
	}
	
	/**
	 * @return a regex String representing the set of symbols identifying observation actions //TODO consider making
	 * this a constant variable OR have this in a separate class because you'll also need a pretty formatting of it.
	 */
	public static String getRegexProtocolObservationAction() {
		return "(look)|(help)|(bye)|";
	}	
	
	/**
	 * @return a regex String representing the set of symbols identifying modification actions. //TODO consider making 
	 * this a constant variable. 
	 */
	public static String getRegexProtocolModificationAction() {
		return "(dig -?\\d+ -?\\d+)|(flag -?\\d+ -?\\d+)|(deflag -?\\d+ -?\\d+)";
	}

}
