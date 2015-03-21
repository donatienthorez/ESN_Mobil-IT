package org.esn.mobilit.models;

import java.io.Serializable;

/**
 * Created by Spider on 02/12/14.
 */
public class Section implements Serializable{
    private int    id;
    private int    id_country;
    private String name;
    private String url;
    private String code_section;
    private String address;
    private String website;
    private String phone;
    private String email;
    private String university;

    public Section(int id, int id_country, String name, String url, String code_section, String address, String website, String phone, String email, String university) {
        this.id = id;
        this.id_country = id_country;
        this.name = name;
        this.url = url;
        this.code_section = code_section;
        this.address = address;
        this.website = website;
        this.phone = phone;
        this.email = email;
        this.university = university;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_country() {
        return id_country;
    }

    public void setId_country(int id_country) {
        this.id_country = id_country;
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

    public String getCode_section() {
        return code_section;
    }

    public void setCode_section(String code_section) {
        this.code_section = code_section;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public String toString() {
        return "Section{" +
                "id=" + id +
                ", id_country='" + id_country + '\'' +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", code_section='" + code_section + '\'' +
                ", address='" + address + '\'' +
                ", website='" + website + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", university='" + university + '\'' +
                '}';
    }
}
