package main.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import main.domain.Game.*;

public class MultiPlayerMineSweeperServer {
	
	public static Game serverGame;

	static void receiveRequest(ServerSocket serverSocket, Game hostedGame) {
		
		try {
			Socket clientSocket = serverSocket.accept();
			Thread t = new Thread(new MultiPlayerMineSweeperServerThread(clientSocket, hostedGame));
			t.start();
		} catch (IOException e) {
			System.out.println("Exception caught when attempting to receive a request");
			System.out.println(e.getMessage());
		}
	}
	
	public static void main(String[] args) throws IOException{
		if (args.length != 1) {
			System.err.println("Usage: java KnockKnockServer <port number>");
            System.exit(1);			
		}
		
		serverGame = new Game();
		int portNumber = Integer.parseInt(args[0]);

        try ( 
        	ServerSocket serverSocket = new ServerSocket(portNumber);
        ) {
        	while (true){
        		receiveRequest(serverSocket, serverGame);
        	}

        } catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port "
                + portNumber);
            System.out.println(e.getMessage());
        }
	}

}
