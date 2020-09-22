package com.mrsoftit.tictocticonline.note;

public class PlayGameNote {
    String gameID;
    String gameRandorCode;

    int activePlayer ;

    String player1ID;
    String player1Name;
    String player1ImageURL;
    int player1Wine;


    String player2ID;
    String player2Name;
    String player2ImageURL;
    int player2Wine;



    int machCount;

    int drow;

    int btn0;
    int btn1;
    int btn2;
    int btn3;
    int btn4;
    int btn5;
    int btn6;
    int btn7;
    int btn8;

    public  PlayGameNote(){}


    public PlayGameNote(String gameID, String gameRandorCode, int activePlayer, String player1ID,
                        String player1Name, int player1Wine, String player1ImageURL, String player2ID,
                        String player2Name, int player2Wine, String player2ImageURL, int machCount,
                        int drow, int btn0, int btn1, int btn2, int btn3, int btn4, int btn5, int btn6, int btn7, int btn8) {
        this.gameID = gameID;
        this.gameRandorCode = gameRandorCode;
        this.activePlayer = activePlayer;
        this.player1ID = player1ID;
        this.player1Name = player1Name;
        this.player1Wine = player1Wine;
        this.player1ImageURL = player1ImageURL;
        this.player2ID = player2ID;
        this.player2Name = player2Name;
        this.player2Wine = player2Wine;
        this.player2ImageURL = player2ImageURL;
        this.machCount = machCount;
        this.drow = drow;
        this.btn0 = btn0;
        this.btn1 = btn1;
        this.btn2 = btn2;
        this.btn3 = btn3;
        this.btn4 = btn4;
        this.btn5 = btn5;
        this.btn6 = btn6;
        this.btn7 = btn7;
        this.btn8 = btn8;
    }
}
