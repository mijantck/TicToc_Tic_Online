package com.mrsoftit.tictocticonline.note;

public class UsersNote {
    String name;
    String email;
    String fbID;
    String fbProfiteImageURL;
    String uID;
    String currentGameID;
    String playing ;
    String status ;
    int winGame;
    int  how_money_mach;

    public UsersNote(){}

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCurrentGameID() {
        return currentGameID;
    }

    public void setCurrentGameID(String currentGameID) {
        this.currentGameID = currentGameID;
    }

    public String getPlaying() {
        return playing;
    }

    public void setPlaying(String playing) {
        this.playing = playing;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFbID() {
        return fbID;
    }

    public void setFbID(String fbID) {
        this.fbID = fbID;
    }

    public String getFbProfiteImageURL() {
        return fbProfiteImageURL;
    }

    public void setFbProfiteImageURL(String fbProfiteImageURL) {
        this.fbProfiteImageURL = fbProfiteImageURL;
    }

    public String getuID() {
        return uID;
    }

    public void setuID(String uID) {
        this.uID = uID;
    }

    public int getWinGame() {
        return winGame;
    }

    public void setWinGame(int winGame) {
        this.winGame = winGame;
    }

    public int getHow_money_mach() {
        return how_money_mach;
    }

    public void setHow_money_mach(int how_money_mach) {
        this.how_money_mach = how_money_mach;
    }
}
