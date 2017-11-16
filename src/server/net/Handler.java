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
                System.out.println(message[0]);
                switch (message[0]) {
                    case "QUIT":
                        disconnect();
                        exit = true;
                        break;
                    case "START":
                        controller.startGame();
                        break;
                    default:
                        controller.attempt(message[0].toCharArray());
                        System.out.println(Arrays.toString(controller.getGameState()));
                }
            } catch(Exception e) {
                e.printStackTrace();
                System.err.println("Error in clienthandler run");
                exit = true;
            }
            
        }
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
