package com.brcaninovich.projekat.MainApplication.ui.messages;

import java.util.List;
import com.brcaninovich.projekat.MessageActivity.Objects.Message;

public class Chat {
    private List<String> participantIds;
    private String lastMessage;
    private long lastMessageTimestamp;
    private String artikalId;
    private String nazivArtikla;
    private List<Message> messages;

    public Chat() {
    }

    public List<String> getParticipantIds() {
        return participantIds;
    }

    public void setParticipantIds(List<String> participantIds) {
        this.participantIds = participantIds;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public long getLastMessageTimestamp() {
        return lastMessageTimestamp;
    }

    public void setLastMessageTimestamp(long lastMessageTimestamp) {
        this.lastMessageTimestamp = lastMessageTimestamp;
    }

    public String getArtikalId() {
        return artikalId;
    }

    public void setArtikalId(String artikalId) {
        this.artikalId = artikalId;
    }

    public String getNazivArtikla() {
        return nazivArtikla;
    }

    public void setNazivArtikla(String nazivArtikla) {
        this.nazivArtikla = nazivArtikla;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }
}
