package com.example.chatapplication.Model;

public class Messege {

    public String sender;
    public String receiver;
    public String messege;
    public boolean isseen;

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessege() {
        return messege;
    }

    public void setMessege(String messege) {
        this.messege = messege;
    }

    public Messege() {
    }

    public boolean isIsseen() {
        return isseen;
    }

    public void setIsseen(boolean isseen) {
        this.isseen = isseen;
    }

    public Messege(String sender, String receiver, String messege, boolean isseen) {
        this.sender = sender;
        this.receiver = receiver;
        this.messege = messege;
        this.isseen = isseen;
    }
}
