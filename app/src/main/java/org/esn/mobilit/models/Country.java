package org.esn.mobilit.models;

import java.io.Serializable;
import java.util.ArrayList;

public class Country implements Serializable{

    private int    id;
    private String name;
    private String url;
    private String code_country;
    private String website;
    private String email;
    private ArrayList<Section> sections;

    public Country(int id, String name, String url, String code_country, String website, String email) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.code_country = code_country;
        this.website = website;
        this.email = email;
        this.sections = new ArrayList<Section>();
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCodeCountry() {
        return code_country;
    }

    public void setCodeCountry(String code_country) {
        this.code_country = code_country;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ArrayList<Section> getSections() {
        return sections;
    }

    public void setSections(ArrayList<Section> sections) {
        this.sections = sections;
    }

    public void addSection(Section section){
        this.sections.add(section);
    }

    public String toString() {
        return "Country{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", code_country='" + code_country + '\'' +
                ", website='" + website + '\'' +
                ", email='" + email + '\'' +
                ", sections=" + sections +
                '}';
    }
}
