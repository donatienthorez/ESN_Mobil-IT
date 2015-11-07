package org.esn.mobilit.models;

import java.io.Serializable;

public class Section implements Serializable{
    private int    sid;
    private int    sid_country;
    private String sname;
    private String surl;
    private String scode_section;
    private String logo_url;
    private String saddress;
    private String swebsite;
    private String sphone;
    private String semail;
    private String suniversity;

    public Section(int id, int id_country, String name, String url, String code_section, String address, String website, String phone, String email, String university) {
        this.sid = id;
        this.sid_country = id_country;
        this.sname = name;
        this.surl = url;
        this.scode_section = code_section;
        this.logo_url = "";
        this.saddress = address;
        this.swebsite = website;
        this.sphone = phone;
        this.semail = email;
        this.suniversity = university;
    }

    public int getId() {
        return sid;
    }

    public int getId_country() {
        return sid_country;
    }

    public void setId_country(int id_country) {
        this.sid_country = id_country;
    }

    public String getName() {
        return sname;
    }

    public void setName(String name) {
        this.sname = name;
    }

    public String getUrl() {
        return surl;
    }

    public void setUrl(String url) {
        this.surl = url;
    }

    public String getCode_section() {
        return scode_section;
    }

    public void setCode_section(String code_section) {
        this.scode_section = code_section;
    }

    public String getLogo_url() {
        return logo_url;
    }

    public void setLogo_url(String logo_url) {
        this.logo_url = logo_url;
    }

    public String getAddress() {
        return saddress;
    }

    public void setAddress(String address) {
        this.saddress = address;
    }

    public String getWebsite() {
        return swebsite;
    }

    public void setWebsite(String website) {
        this.swebsite = website;
    }

    public String getPhone() {
        return sphone;
    }

    public void setPhone(String phone) {
        this.sphone = phone;
    }

    public String getEmail() {
        return semail;
    }

    public void setEmail(String email) {
        this.semail = email;
    }

    public String getUniversity() {
        return suniversity;
    }

    public void setUniversity(String university) {
        this.suniversity = university;
    }

    @Override
    public String toString() {
        return "Section{" +
                "id=" + sid +
                ", id_country=" + sid_country +
                ", name='" + sname + '\'' +
                ", url='" + surl + '\'' +
                ", code_section='" + scode_section + '\'' +
                ", logo_url='" + logo_url + '\'' +
                ", address='" + saddress + '\'' +
                ", website='" + swebsite + '\'' +
                ", phone='" + sphone + '\'' +
                ", email='" + semail + '\'' +
                ", university='" + suniversity + '\'' +
                '}';
    }
}
