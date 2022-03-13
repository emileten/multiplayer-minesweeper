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
	
	public MultiPlayerMineSweeperServerThread(Socket socket, Game hostedGame){
		this.clientSocket = socket;
		this.hostedGame = hostedGame;
	}
	
	/**
	 * Handler for client starting game
	 * 
	 * @param input message from client, must match 
	 * ServerProtocol.getRegexServerStartProtocol()
	 * 
	 * @return message to client or null
	 */
	private String handleStartRequest(String input) {
		String[] tokenStrings = input.split(input);
		int numberOfRows = Integer.valueOf(tokenStrings[1]);
		int numberOfColumns = Integer.valueOf(tokenStrings[2]);
		int size = numberOfRows*numberOfColumns;
		List<Integer> bombLocations = Game.randomBombLocations(Math.floorDiv(size, 5), size);
	    this.threadPlayer = new StringPlayer(tokenStrings[0]);
		Event resultEvent = hostedGame.startGame(this.threadPlayer,
				Integer.valueOf(tokenStrings[1]),
				Integer.valueOf(tokenStrings[2]),
				Integer.valueOf(tokenStrings[3]),
				bombLocations);
		return resultEvent.toString();
	}
	
	
	/**
	 * Handler for client joining game
	 * 
	 * @param input message from client, must match 
	 * ServerProtocol.getRegexServerJoinProtocol()
	 * 
	 * @return message to client or null
	 */
	private String handleJoinRequest(String input) {
		String[] tokenStrings = input.split(input);
		this.threadPlayer = new StringPlayer(tokenStrings[0]);
		Event resultEvent = hostedGame.joinGame(this.threadPlayer);
		return resultEvent.toString();
	}
	
	
	public void run() {
        try ( 
                PrintWriter out =
                    new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));
            ) {
            
                String inputLine;
                
                out.println("Welcome to the multiplayer MineSweeper game ! \n");
                
                if (this.hostedGame.hasStarted()) {
                	out.println("A game is already ongoing. You can either join, or leave. \n");
                	out.println(ServerProtocol.getHumanReadableServerJoinProtocol() + "\n");    
                    inputLine = in.readLine();
                    while (!inputLine.matches(ServerProtocol.getRegexServerJoinProtocol())) {
                    	out.println("I could not understand ! \n");
                    	out.println(ServerProtocol.getHumanReadableServerJoinProtocol());
                    	inputLine = in.readLine();
                    }              
                    out.println(handleJoinRequest(inputLine));
                } else {
                	out.println("There is no game ongoing. You can either start a new game or leave.\n");
                	out.println(ServerProtocol.getHumanReadableServerStartProtocol() + "\n");
                    inputLine = in.readLine(); 
                    while (!inputLine.matches(ServerProtocol.getRegexServerStartProtocol())) {
                    	out.println("I could not understand ! \n");
                    	out.println(ServerProtocol.getHumanReadableServerStartProtocol());
                    	inputLine = in.readLine();
                    }
                    out.println(handleStartRequest(inputLine));
                }
                
                // Keep handling the player's commmands
                while (((inputLine = in.readLine()) != null)) {
                	Event resultEvent = hostedGame.play(this.threadPlayer, inputLine);
                	out.println(resultEvent.toString());
                	if(this.hostedGame.hasEnded() |  // TODO that's a bit weird: shouldn't i be using events ?
                			// fields and events are entangled here. 
                			resultEvent instanceof PlayerRemovedEvent) {
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
