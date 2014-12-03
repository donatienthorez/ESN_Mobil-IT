package org.esn.mobilit.pojo;

/**
 * Created by Spider on 02/12/14.
 */
public class Section {

    private String name;
    private String code_section;
    private String Adress;
    private String website;
    private String phone;
    private String univercity;
    private String email;

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

    public String getAdress() {
        return Adress;
    }

    public void setAdress(String adress) {
        Adress = adress;
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

    public String getUnivercity() {
        return univercity;
    }

    public void setUnivercity(String univercity) {
        this.univercity = univercity;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Section{" +
                "name='" + name + '\'' +
                ", code_section='" + code_section + '\'' +
                ", Adress='" + Adress + '\'' +
                ", website='" + website + '\'' +
                ", phone='" + phone + '\'' +
                ", univercity='" + univercity + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
