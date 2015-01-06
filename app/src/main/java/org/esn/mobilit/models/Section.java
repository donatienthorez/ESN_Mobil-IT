package org.esn.mobilit.models;

/**
 * Created by Spider on 02/12/14.
 */
public class Section {

    private String name;
    private String code_section;
    private String code_country;
    private String Adress;
    private String phone;
    private String website;
    private String email;
    private String univercity;

    public Section(String name, String code_section, String code_country, String adress, String phone, String website, String email, String univercity) {
        this.name = name;
        this.code_section = code_section;
        this.code_country = code_country;
        Adress = adress;
        this.phone = phone;
        this.website = website;
        this.email = email;
        this.univercity = univercity;
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

    public String getCode_country() {
        return code_country;
    }

    public void setCode_country(String code_country) {
        this.code_country = code_country;
    }

    public String getAdress() {
        return Adress;
    }

    public void setAdress(String adress) {
        Adress = adress;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public String getUnivercity() {
        return univercity;
    }

    public void setUnivercity(String univercity) {
        this.univercity = univercity;
    }

    @Override
    public String toString() {
        return "Section{" +
                "name='" + name + '\'' +
                ", code_section='" + code_section + '\'' +
                ", code_country='" + code_country + '\'' +
                ", Adress='" + Adress + '\'' +
                ", phone='" + phone + '\'' +
                ", website='" + website + '\'' +
                ", email='" + email + '\'' +
                ", univercity='" + univercity + '\'' +
                '}';
    }
}
