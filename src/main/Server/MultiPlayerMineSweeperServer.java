package main.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import main.domain.Game.*;
import java.util.*;

public class MultiPlayerMineSweeperServer {
	
	public static Game serverGame;
	
    /** Default server port. */
    private static final int DEFAULT_PORT = 4444;
    /** Maximum port number as defined by ServerSocket. */
    private static final int MAXIMUM_PORT = 65535;
    /** Default square board size. */

    /** Socket for receiving incoming connections. */
    private final ServerSocket serverSocket;

    /**
     * Make a MinesweeperServer that listens for connections on port.
     * 
     * @param port port number, requires 0 <= port <= 65535
     * @param debug debug mode flag
     * @throws IOException if an error occurs opening the server socket
     */
    public MultiPlayerMineSweeperServer(int port, boolean debug) throws IOException {
        serverSocket = new ServerSocket(port);
    }
	
    /* Instantiates a Game, and opens itself to client connections to handle them
     * Never returns, unless an exception is thrown.
     * 
     * @throws IOException if the main server socket is broken. 
     */
	public void serve() throws IOException {
		
		serverGame = new Game();
		while (true) { //means it's constantly serving, until I decide to turn it off.    	
			Socket socket = serverSocket.accept(); // blocks until reception of connection attempt. Might throw IO Exception ! 			
			Thread t = new Thread(new MultiPlayerMineSweeperServerThread(socket, serverGame)); //sending over to new thread	
			t.start();							
		}       
	}
	

    /**
     * Start a MinesweeperServer using the given arguments.
     * 
     * <br> Usage:
     *      MinesweeperServer [--debug | --no-debug] [--port PORT]
     * 
     * <br> The --debug argument means the server should run in debug mode. The server should disconnect a
     *      client after a BOOM message if and only if the --debug flag was NOT given.
     *      Using --no-debug is the same as using no flag at all.
     * <br> E.g. "MinesweeperServer --debug" starts the server in debug mode.
     * 
     * <br> PORT is an optional integer in the range 0 to 65535 inclusive, specifying the port the server
     *      should be listening on for incoming connections.
     * <br> E.g. "MinesweeperServer --port 1234" starts the server listening on port 1234.
     * 
     * @param args arguments as described
     */
    public static void main(String[] args) throws IOException {
        // Command-line argument parsing is provided. Do not change this method.
        boolean debug = false;
        int port = DEFAULT_PORT;

        Queue<String> arguments = new LinkedList<String>(Arrays.asList(args));
        
        // trying to parse args
        try {
            while ( ! arguments.isEmpty()) {
                String flag = arguments.remove();
                try {
                    if (flag.equals("--debug")) {
                        debug = true;
                    } else if (flag.equals("--no-debug")) {
                        debug = false;
                    } else if (flag.equals("--port")) {
                        port = Integer.parseInt(arguments.remove());
                        if (port < 0 || port > MAXIMUM_PORT) {
                            throw new IllegalArgumentException("port " + port + " out of range");
                        }
                    } else {
                        throw new IllegalArgumentException("unknown option: \"" + flag + "\"");
                    }
                } catch (NoSuchElementException nsee) {
                    throw new IllegalArgumentException("missing argument for " + flag);
                } catch (NumberFormatException nfe) {
                    throw new IllegalArgumentException("unable to parse number for " + flag);
                }
            }
        } catch (IllegalArgumentException iae) {
            System.err.println(iae.getMessage());
            System.err.println("usage: MinesweeperServer [--debug | --no-debug] [--port PORT]");
            return;
        }

        
        // trying to run server
        try {
        	runMultiPlayerMineSweeperServer(debug, port);	
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

       

    }

    /**
     * Start a MinesweeperServer running on the specified port.
     * 
     * @param debug The server will disconnect a client after a BOOM message if and only if debug is false.
     * @param port The network port on which the server should listen, requires 0 <= port <= 65535.
     * @throws IOException if a network error occurs
     */
    public static void runMultiPlayerMineSweeperServer(boolean debug, int port) throws IOException {
               
        MultiPlayerMineSweeperServer server = new MultiPlayerMineSweeperServer(port, debug);
        server.serve();
     
    }
	

}
