/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import server.controller.Controller;

/**
 *
 * @author Bernardo
 */
public class Server {
    private int port = 54321;
    private final Controller controller = new Controller();
    private static final int LINGER = 3000;
    private static final int TIMEOUT_LONG = 1200000;
    
    public Server(String[] args) {
        parsePort(args);
        serve();
    }
    
    private void serve() {
        try {
            ServerSocket listeningSocket = new ServerSocket(port);
            while (true) {
                Socket client = listeningSocket.accept();
                handleClient(client);
            }
        } catch (IOException e) {
            System.err.println("Server failure.");
        }
    }
    
    private void handleClient(Socket client) {
        try {
            client.setSoLinger(true, LINGER);
            client.setSoTimeout(TIMEOUT_LONG);
            new Handler(client).start();
            
        } catch (SocketException e) {
            System.err.println("Problem handling client.");
        }

    }
    
    private void parsePort(String[] args) {
        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.err.println("Invalid port number, using default.");
            }
        }
    }
}
