/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UncheckedIOException;
import java.net.Socket;
import java.util.Arrays;
import java.util.StringJoiner;
import protocol.Constants;
import server.controller.Controller;

/**
 *
 * @author Bernardo
 */
public class Handler extends Thread {
    private Socket socket;
    private boolean exit = false;
    private BufferedReader reader;
    private PrintWriter writer;
    private Controller controller = new Controller();
    
    Handler(Socket client) {
        socket = client;
    }
    
    @Override
    public void run() {
        try {
            reader = new BufferedReader(new
                InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        while(!exit) {
            try {
                String[] message = reader.readLine().split(Constants.DELIMITER);
                String reply = "--------------------" + Constants.NEW_LINE;
                //System.out.println(message[0]);
                switch (message[0]) {
                    case "QUIT":
                        disconnect();
                        exit = true;
                        break;
                    case "START":
                        controller.startGame();
                        reply += "Game started:" + Constants.NEW_LINE;
                        writer.println(reply + stateToString());
                        break;
                    default:
                        if (controller.gameStarted()) {
                            try {
                                if(controller.attempt(message[0].toCharArray())) {
                                    reply += "Game finished as followed:" + Constants.NEW_LINE;
                                }
                                reply += stateToString();
                            } catch (Exception e) {
                                reply += "Incorrect amount of characters.";
                            }

                        } else {
                            reply += "Game hasn't started yet.";
                        }
                        
                        writer.println(reply);
                }
            } catch(Exception e) {
                //e.printStackTrace();
                System.err.println("Error in clienthandler run, aborting.");
                exit = true;
            }
            
        }
    }
    
    private String stateToString() {
        StringJoiner joinedMessage = new StringJoiner(Constants.NEW_LINE);
        String[]state = controller.getGameState();
        for (String element : state) {
            joinedMessage.add(element);
        }
        return joinedMessage.toString();
    }
    
    private void disconnect() {
        try {
            socket.close();
        } catch (IOException e) {
            System.err.println("Error closing socket.");
        }
        exit = true;
    }
}
