package com.example.amankumar.layouttest.Model;

/**
 * Created by AmanKumar on 4/18/2016.
 */
public class ChatMessageModel {
    String author;
    String message;

    public ChatMessageModel() {
    }

    public ChatMessageModel(String author, String message) {
        this.author = author;
        this.message = message;
    }

    public String getAuthor() {
        return author;
    }

    public String getMessage() {
        return message;
    }
}
