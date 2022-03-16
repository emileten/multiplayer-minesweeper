package main.server;

public class ServerProtocol {

	public static String getHumanReadableServerJoinProtocol() {
		return "To join a game : <join PLAYER_ID> To exit : <bye>";
	}
	
	
	public static String getHumanReadableServerStartProtocol() {
		return "To start a game : <start PLAYER_ID ROWS COLUMNS NUMBER_OF_PLAYERS> To exit : <bye>";
	}
	
	
	public static String getRegexServerJoinProtocol() {
		return "(join -?\\w+)" + "|" + getRegexServerByeProtocol() ;
	}
	
	public static String getRegexServerStartProtocol() {
		return "(start -?\\w+ -?\\d+ -?\\d+ -?\\d+)" + "|" + getRegexServerByeProtocol();
	}
		
	public static String getRegexServerByeProtocol() {
		return "(bye)";
	}
}
