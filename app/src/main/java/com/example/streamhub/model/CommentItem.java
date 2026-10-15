package com.example.streamhub.model;

public class CommentItem {
    private final String id;
    private final String author;
    private final String comment;
    private final String timestamp;

    public CommentItem(String id, String author, String comment, String timestamp) {
        this.id = id;
        this.author = author;
        this.comment = comment;
        this.timestamp = timestamp;
    }

    public String getId() { return id; }
    public String getAuthor() { return author; }
    public String getComment() { return comment; }
    public String getTimestamp() { return timestamp; }
}
