package ru.geekbrains.baster.appchat.common;

import com.google.gson.Gson;

import java.util.List;

/**
 * DataTransferObject - объект, который имеет все нужные нам поля и будет отправляться в обе стороны по сети.
 * Также присутствуют методы сериализации и десереализации в формат JSON
 */

public class MessageDTO {
    private MessageType messageType;
    private String body;
    private String login;
    private String password;
    private String to;
    private String from;
    private List<String> usersOnline;

    public List<String> getUsersOnline() {
        return usersOnline;
    }

    public void setUsersOnline(List<String> usersOnline) {
        this.usersOnline = usersOnline;
    }

    public MessageDTO() {
    }

    public static MessageDTO convertFromJson(String json) {
        return new Gson().fromJson(json, MessageDTO.class);
    }

    public String convertToJson() {
        return new Gson().toJson(this);
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }
}