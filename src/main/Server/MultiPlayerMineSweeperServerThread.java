package main.server;

import java.net.*;
import java.io.*;
import java.util.List;


import main.domain.Game.*;
import main.domain.Players.*;

public class MultiPlayerMineSweeperServerThread implements Runnable {

	private Socket clientSocket;
	public Game hostedGame;
	public Player threadPlayer;
	private String stateString = "WAITING";
	
	public MultiPlayerMineSweeperServerThread(Socket socket, Game hostedGame){
		this.clientSocket = socket;
		this.hostedGame = hostedGame;
	}
	
	private String startHostedGame() {
		

	}
	
	
	/**
	 * Handler for client starting game or joining game
	 * 
	 * @param input message from client, must match 
	 * ServerProtocol.getRegexServerStartProtocol() or ServerProtocol.getRegexServerJoinProtocol() 
	 * 
	 * @return message to client or null
	 */
	private String handleFirstRequest(String input) {
		
		if(input.matches(ServerProtocol.getRegexServerJoinProtocol())) {
			String[] tokenStrings = input.split("//s+");
			this.threadPlayer = new StringPlayer(tokenStrings[1]);
			Event resultEvent = hostedGame.joinGame(this.threadPlayer);
			return resultEvent.toString();			
		} else {
			String[] tokenStrings = input.split("\\s+");		
		    this.threadPlayer = new StringPlayer(tokenStrings[1]);
			int numberOfRows = Integer.valueOf(tokenStrings[2]);
			int numberOfColumns = Integer.valueOf(tokenStrings[3]);
			int size = numberOfRows*numberOfColumns;
			List<Integer> bombLocations = Game.randomBombLocations(Math.floorDiv(size, 5), size);
			Event resultEvent = hostedGame.startGame(this.threadPlayer,
					numberOfRows,
					numberOfColumns,
					Integer.valueOf(tokenStrings[4]),
					bombLocations);
			return resultEvent.toString();	
		}

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
                
                // TODO define protocol. It's a function String -> String mapping a client message to a server message
                // it should include  the first message of the server (with a null client entry)
                
                /*
                 * 
                 * parameter of this algorithm : input 
                 * 
                 * output = null 
                 * 
                 * if waiting 
                 * 	if game has not started
                 * 		output = offer to start game 
                 * 		waiting = false
                 *	else 
                 *		output = offer to join
                 *		waiting = false
                 * else
                 * 	if input is a bye
                 *  	output = bye
                 *  else if input matches an action
                 *  	output is the event
                 *  else
                 *  	output is help 
       			 *
                 * return output
                 * 
                 */
                
                // send first message
                outputLineString = this.handleRequest(null);
                out.println(outputLineString);
                
                // pursue communication until end signal
                while((intputLineString = in.readLine()) != null) { // infinite per se
                	outputLineString = this.handleRequest(intputLineString);
                	out.println(outputLineString);
                	if (outputLineString.equals("Bye.")){
                		break;
                	}
                }
//                 
//                
//                if (this.hostedGame.hasStarted()) {
//                    out.println("Welcome to the multiplayer MineSweeper game. " + ServerProtocol.getHumanReadableServerJoinProtocol());
//                    inLineString = in.readLine();
//                    while (!inLineString.matches(ServerProtocol.getRegexServerJoinProtocol())) {
//                    	out.println(ServerProtocol.getHumanReadableServerJoinProtocol());
//                    	inLineString = in.readLine();
//                    }
//                } else {
//                    out.println("Welcome to the multiplayer MineSweeper game. " + ServerProtocol.getHumanReadableServerStartProtocol());
//                    inLineString = in.readLine();
//                    while (!inLineString.matches(ServerProtocol.getRegexServerStartProtocol())) {
//                    	out.println(ServerProtocol.getHumanReadableServerStartProtocol());
//                    	inLineString = in.readLine();
//                    }            	
//                }
//                
//                if (inLineString.matches(ServerProtocol.getRegexServerByeProtocol())) {
//                	out.println("bye");
//                } else {
//                    //outLineString = handleFirstRequest(inLineString);
//                    //out.println(outLineString);                	
//                }
//                

                
//                out.println("Welcome to the multiplayer MineSweeper game. " + ServerProtocol.getHumanReadableServerStartProtocol());
//                inputLine = in.readLine(); 
//                while (!inputLine.matches(ServerProtocol.getRegexServerStartProtocol())) {
//                	out.println("I could not understand ! " + ServerProtocol.getHumanReadableServerStartProtocol());
//                	inputLine = in.readLine();
//                }
//                
//                if (inputLine.matches(ServerProtocol.getRegexServerByeProtocol())) {
//                	out.println("bye!");
//                	clientSocket.close();
//                } else {
//                	out.println(handleStartRequest(inputLine));	
//                }
//                
//                
//                // Keep handling the player's commmands
//                while (((inputLine = in.readLine()) != null)) {
//                	Event resultEvent = hostedGame.play(this.threadPlayer, inputLine);
//                	out.println(resultEvent.toString());
//                	if(this.hostedGame.hasEnded() |  // TODO that's a bit weird: shouldn't i be using events ?
//                			// fields and events are entangled here. 
//                			resultEvent instanceof PlayerRemovedEvent) {
//                		break;
//                	}
//                }
                          
            } catch (IOException e) {
                System.out.println("Exception caught when trying to handle request in thread" + 
                Thread.currentThread().getName());
                System.out.println(e.getMessage()); 	
            }
		
	}

}
