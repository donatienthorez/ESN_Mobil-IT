package org.esn.mobilit.models;

import java.io.Serializable;

public class Notification implements Serializable {
    private String title;
    private String description;
    private String type;
    private String link;

    public Notification(String title, String description, String type, String link) {
        this.title = title;
        this.description = description;
        this.type = type;
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
