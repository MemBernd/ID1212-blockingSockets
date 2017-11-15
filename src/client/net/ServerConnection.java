/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 *
 * @author Bernardo
 */
public class ServerConnection {
    private Socket socket;
    private PrintWriter printWriter;
    private BufferedReader bufferedReader;
    private static final int TIMEOUT_LONG = 1200000;
    private static final int TIMEOUT_SHORT = 30000;
    private volatile boolean connected = false;
    
    public void connect(String host, int port, OutputHandler output) throws IOException {
        socket = new Socket();
        socket.connect(new InetSocketAddress(host, port), TIMEOUT_SHORT);
        socket.setSoTimeout(TIMEOUT_LONG);
        connected = true;
        printWriter = new PrintWriter(socket.getOutputStream(), true);
        bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        new Thread(new Listener(output)).start();
    }
    
    public void disconnect() throws IOException {
        socket.close();
        socket = null;
        connected = false;
    }
    
    private class Listener implements Runnable {
        private final OutputHandler output;
        
        Listener (OutputHandler output) {
            this.output = output;
        }
        
        @Override
        public void run() {
            try {
                while(true) {
                    output.printMessage(bufferedReader.readLine());
                }  
            } catch (Exception e) {
                output.printMessage("error listening");
            }
          
        }
    }
}
