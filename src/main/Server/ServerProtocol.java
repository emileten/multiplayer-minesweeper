package main.server;


import main.domain.Events.*;

//TODO change the bye so that it matches that of the game... lol 
//TODO consider having objects with a 'regex' rep and a 'human readable' rep.
// benefit : less likely to end up with wrong strings shown to user...
// there may be some package already doing that for you, mapping a regex string to a human readable string...
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
	
	public static String getRegexServerProtocolEndSignals() {
		return "(" + getRegexServerByeProtocol() + ")"
				+ "|(" + new AllDugEvent().toString() + ")"
				+ "|(" + new BoomEvent().toString() + ")"
				+ "|(" + new PlayerRemovedEvent().toString() + ")"
				+ "|(" + new NoPlayerInGameEvent().toString() + ")";
	}
	
	
	public static String getRegexServerUnSupportedPlayerActionSignal() {
		return "(" + new UnSupportedPlayerActionEvent().toString() + ")";
	}
}
