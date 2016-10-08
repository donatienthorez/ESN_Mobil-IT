package org.esn.mobilit.models;

import java.io.Serializable;

public class Notification implements Serializable {
    private String title;
    private String description;

    public Notification(String title, String description) {
        this.title = title;
        this.description = description;
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
}
