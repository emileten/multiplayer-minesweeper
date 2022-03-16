package main.client;

import java.io.*;
import java.net.*;

//TODO copy pasting the KK client doesn't work. Doesn't transmit properly your msg.
// Also you have to type spaces, it's weird. 
public class MultiPlayerMineSweeperClient {

    public static void main(String[] args) throws IOException {
        
        if (args.length != 2) {
            System.err.println(
                "Usage: java EchoClient <host name> <port number>");
            System.exit(1);
        }

        String hostName = args[0];
        int portNumber = Integer.parseInt(args[1]);

        try (
            Socket kkSocket = new Socket(hostName, portNumber);
            PrintWriter out = new PrintWriter(kkSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                new InputStreamReader(kkSocket.getInputStream()));
        ) {
            BufferedReader stdIn =
                new BufferedReader(new InputStreamReader(System.in));
            String fromServer;
            String fromClient;
            
            
            // this waits indefinitely for the first message
            while((fromServer = in.readLine()) != null) { // infinite loop
            	System.out.println(fromServer);	
            	if (fromServer.equals("Bye")) { // end signal 
            		break;
            	}
            	fromClient = in.readLine();
            	if (fromClient!=null) {
            		out.println(fromClient);         
            	}
            }
            
                               
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.out.println("Could not get IO for the connection to " + hostName);
        	System.exit(1);
        }
    }

}
