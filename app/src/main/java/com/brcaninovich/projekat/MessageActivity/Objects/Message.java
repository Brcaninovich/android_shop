package com.brcaninovich.projekat.MessageActivity.Objects;

public class Message {
    private String senderId;
    private String receiverId;
    private String message;
    private long timestamp;
    private String artikalId;
    private String nazivArtikla;

    public Message() {

    }

    public Message(String senderId, String receiverId, String message, long timestamp, String artikalId, String nazivArtikla) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.message = message;
        this.timestamp = timestamp;
        this.artikalId = artikalId;
        this.nazivArtikla = nazivArtikla;
    }

    // Getteri
    public String getSenderId() {
        return senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public String getMessage() {
        return message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getArtikalId() {
        return artikalId;
    }

    public String getNazivArtikla() {
        return nazivArtikla;
    }

    // Setteri
    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void setArtikalId(String artikalId) {
        this.artikalId = artikalId;
    }

    public void setNazivArtikla(String nazivArtikla) {
        this.nazivArtikla = nazivArtikla;
    }
}
