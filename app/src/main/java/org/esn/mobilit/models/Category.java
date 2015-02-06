package org.esn.mobilit.models;

import java.io.Serializable;

/**
 * Created by Spider on 27/01/15.
 */
public class Category implements Serializable {
    private int id;
    private String name;
    private String section;
    private String content;
    private int level;
    private int position;


    public Category(int id, String name, String section, String content, int level, int position) {
        this.id         = id;
        this.name       = name;
        this.section    = section;
        this.content    = content;
        this.level      = level;
        this.position   = position;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public boolean hasContent(){
        return (this.getContent().length() == 0) ? false : true;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", section='" + section + '\'' +
                ", content='" + content + '\'' +
                ", position=" + position +
                '}';
    }
}
