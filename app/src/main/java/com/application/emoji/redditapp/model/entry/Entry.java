package com.application.emoji.redditapp.model.entry;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.Serializable;

/**
 * Created by Sahil on 28-12-2017.
 */

@Root(name="entry", strict = false)
public class Entry implements Serializable{

    @Element(name = "content")
    private String content;

    @Element(required = false, name = "author")
    private Author author;

    @Element(name = "title")
    private String title;

    @Element(name = "id")
    private String id;

    @Element(name = "updated")
    private String updated;

    public Entry(String content, Author author, String title, String updated) {
        this.content = content;
        this.author = author;
        this.title = title;
        this.updated = updated;
    }

    public Entry() {

    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
