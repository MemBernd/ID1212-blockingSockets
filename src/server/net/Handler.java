/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.net;

import java.net.Socket;

/**
 *
 * @author Bernardo
 */
public class Handler extends Thread {
    private Socket socket;
    private boolean exit = false;
    
    Handler(Socket client) {
        socket = client;
    }
    
    @Override
    public void run() {
        System.out.println(socket.getPort());
    }
}
