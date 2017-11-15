/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.controller;

import client.net.OutputHandler;
import client.net.ServerConnection;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.concurrent.CompletableFuture;

/**
 *
 * @author Bernardo
 */
public class Controller {
    private ServerConnection conn;
    
    public Controller() {
        conn = new ServerConnection();
    }
    
    public void connect(String host, int port, OutputHandler output) {
        CompletableFuture.runAsync(() -> {
            try {
                conn.connect(host, port, output);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }).thenRun(() -> output.printMessage("Successfully connected"));
    }
    
    public void disconnect() throws IOException {
        conn.disconnect();
    }

}
