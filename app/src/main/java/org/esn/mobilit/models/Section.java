package org.esn.mobilit.models;

import java.io.Serializable;

public class Section implements Serializable{
    private String name;
    private String code_section;
    private String logo_url;
    private String address;
    private String website;
    private String phone;
    private String email;
    private String university;

    public Section(int id, int id_country, String name, String url, String code_section, String address, String website, String phone, String email, String university) {

        this.name = name;
        this.code_section = code_section;
        this.logo_url = "";
        this.address = address;
        this.website = website;
        this.phone = phone;
        this.email = email;
        this.university = university;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode_section() {
        return code_section;
    }

    public void setCode_section(String code_section) {
        this.code_section = code_section;
    }

    public String getLogo_url() {
        return logo_url;
    }

    public void setLogo_url(String logo_url) {
        this.logo_url = logo_url;
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

    @Override
    public String toString() {
        return "Section{" +
                ", name='" + name + '\'' +
                ", code_section='" + code_section + '\'' +
                ", logo_url='" + logo_url + '\'' +
                ", address='" + address + '\'' +
                ", website='" + website + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", university='" + university + '\'' +
                '}';
    }
}
