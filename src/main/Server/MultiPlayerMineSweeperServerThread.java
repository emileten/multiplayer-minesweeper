package main.server;

import java.net.*;
import java.io.*;
import java.util.List;


import main.domain.Game.*;
import main.domain.Players.*;
import main.domain.Events.*;

public class MultiPlayerMineSweeperServerThread implements Runnable {

	private Socket clientSocket;
	public Game hostedGame;
	public Player threadPlayer;
	private String stateString = "WAITING";
	
	public MultiPlayerMineSweeperServerThread(Socket socket, Game hostedGame){
		this.clientSocket = socket;
		this.hostedGame = hostedGame;
	}
	
	
	
	/**
	 * Defines higher level protocol 
	 * 
	 * @param input message from client
	 * 
	 * @return message to client
	 */
	private String handleRequest(String input) {
		String theOutputString = null;
		String welcomeString = "Welcome to the multiplayer MineSweeper game ! ";
		
		if (this.stateString.equals("WAITING")) { // for first message
			if (this.hostedGame.hasStarted()) {
				theOutputString = welcomeString + ServerProtocol.getHumanReadableServerJoinProtocol();
				this.stateString = "OFFERED_JOIN";
			} else {
				theOutputString = welcomeString + ServerProtocol.getHumanReadableServerStartProtocol();
				this.stateString = "OFFERED_START";
			}					
		} else if (this.stateString.equals("OFFERED_START")){ // here the conversation is going on 
			if (input.matches(ServerProtocol.getRegexServerByeProtocol())) {
            	theOutputString = "Bye";
            } else if (input.matches(ServerProtocol.getRegexServerStartProtocol())) {
    			String[] tokenStrings = input.split("\\s+");		
    		    this.threadPlayer = new StringPlayer(tokenStrings[1]);
    			int numberOfRows = Integer.valueOf(tokenStrings[2]);
    			int numberOfColumns = Integer.valueOf(tokenStrings[3]);
    			int size = numberOfRows*numberOfColumns;
    			List<Integer> bombLocations = Game.randomBombLocations(Math.floorDiv(size, 5), size);
    			theOutputString = hostedGame.startGame(this.threadPlayer,
    					numberOfRows,
    					numberOfColumns,
    					Integer.valueOf(tokenStrings[4]),
    					bombLocations).toString();
    		} else {
    			theOutputString = ServerProtocol.getRegexServerStartProtocol();
    		}
		} else if (this.stateString.equals("OFFERED_JOIN")){ // here the conversation is going on 
			if (input.matches(ServerProtocol.getRegexServerByeProtocol())) {
            	theOutputString = "Bye";
            } else if(input.matches(ServerProtocol.getRegexServerJoinProtocol())) {
				String[] tokenStrings = input.split("//s+");
				this.threadPlayer = new StringPlayer(tokenStrings[1]);
				theOutputString = hostedGame.joinGame(this.threadPlayer).toString();
			} else {
            	theOutputString = ServerProtocol.getHumanReadableServerJoinProtocol();
            }
		} else if (this.stateString.equals("PLAYING")){ 
			if (input.matches(ServerProtocol.getRegexServerByeProtocol())) {
            	theOutputString = "Bye";
            } else {
            	theOutputString = hostedGame.play(this.threadPlayer, input).toString();        
            }
		} 

		return theOutputString;
	}
	
	
	
	public void run() {
        try ( 
                PrintWriter out =
                    new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));
            ) {
            
                String outputLineString, intputLineString;
                
                // a protocol is a mapping String -> String, mapping a client message to a server message.
                // it should handle the first message case hand have a defined set of conversation end identifiers.

                
                // send first message
                outputLineString = this.handleRequest(null);
                out.println(outputLineString);
                
                // pursue communication until end signal
                while((intputLineString = in.readLine()) != null) { // infinite per se
                	outputLineString = this.handleRequest(intputLineString);
                	out.println(outputLineString);
                	if (outputLineString.matches(ServerProtocol.getRegexServerProtocolEndSignals())){ 
                		break;
                	}
                }
                    
            } catch (IOException e) {
                System.out.println("Exception caught when trying to handle request in thread" + 
                Thread.currentThread().getName());
                System.out.println(e.getMessage()); 	
            }
		
	}

}
