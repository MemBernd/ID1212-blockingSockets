/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.model;

import java.util.Arrays;
import protocol.Constants;
import server.FileHandling.FileReader;

/**
 *
 * @author Bernardo
 */
public class GameState {
    private char[] word;
    private char[] actualWord;
    private int attemptsLeft;
    private int score = 0;
    private char placeholder = '_';
    
    
    //very ugly glue; dividing into several methods
    public boolean finishes(char[] attempt) throws Exception {
        if (attempt.length == actualWord.length) {
            //if user tries to guess whole word
            for (int i = 0 ; i < actualWord.length; i++) {
                if (attempt[i] != actualWord[i]) {
                    attemptsLeft--;
                    if (attemptsLeft == 0) { return true;}
                    return false;
                }
            }
            return true;
        } else if (attempt.length == 1) {
            //if one char is to find
            boolean found = false;
            for (int i = 0; i< actualWord.length; i++) {
                if ((word[i] == placeholder) && (actualWord[i] == attempt[0])) {
                    found = true;
                    word[i]=actualWord[i];
                }
            }
            if (!found) {
                attemptsLeft--;
                if (attemptsLeft == 0) { return true;}
            }
            return false;
        } else {
            throw new Exception("amount of characters doesn't match");
        }
    }
    public String[] getbuiltState() {
        String[] state = new String[3];
        state[Constants.SCORE_INDEX] = Integer.toString(score);
        state[Constants.ATTEMPTS_INDEX] = Integer.toString(attemptsLeft);
        state[Constants.WORD_INDEX] = Arrays.toString(word);
        return state;
    }
    public void initializeGame() {
        actualWord = getRandomWord().toCharArray();
        word = new char[actualWord.length];
        Arrays.fill(word, placeholder);
        attemptsLeft = actualWord.length;
    }
    
    
    private String getRandomWord() {
        FileReader file = new FileReader();
        return "stub";
    }
}
