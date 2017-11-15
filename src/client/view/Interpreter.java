/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.view;

import client.controller.Controller;
import client.net.OutputHandler;
import java.util.Scanner;

/**
 *
 * @author Bernardo
 */
public class Interpreter extends Thread {
    private final Scanner consoleInput = new Scanner(System.in);
    private static final String PROMPT = "> ";
    private boolean exit = false;
    private PrintUsingThread put = new PrintUsingThread();
    private final Controller controller = new Controller();
    
    @Override
    public void run() {
        while (!exit) {
            try {
                CmdHandling cmd = new CmdHandling(readLine());
                //System.out.println(cmd.getCmd());
                switch (cmd.getCmd()) {
                    case CONNECT:
                        controller.connect(cmd.getArgument(0),
                                Integer.parseInt(cmd.getArgument(1)), new ConsoleOutput());
                        break;
                    case QUIT:
                        exit = true;
                        controller.disconnect();
                        break;
                    case INVALID:
                        
                        break;
                    default:
                        
                    
                }
            } catch (Exception e) {
                put.println("Problem with operation");
            }
        }
    }
    
    private String readLine() {
        put.print(PROMPT);
        return consoleInput.nextLine();
    }
    
    private class ConsoleOutput implements OutputHandler {
        @Override
        public void printMessage (String output) {
            put.println(output);
            put.print(PROMPT);
        }
    }
}