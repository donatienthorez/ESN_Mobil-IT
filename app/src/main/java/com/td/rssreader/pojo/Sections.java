package com.td.rssreader.pojo;

/**
 * Created by Spider on 02/12/14.
 */
public class Sections {
    private String name;
    private String url;
    private String code_country;
    private String code_section;

    public Sections(String name, String url, String code_country, String code_section) {
        this.name = name;
        this.url = url;
        this.code_country = code_country;
        this.code_section = code_section;
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

    public String getCode_country() {
        return code_country;
    }

    public void setCode_country(String code_country) {
        this.code_country = code_country;
    }

    public String getCode_section() {
        return code_section;
    }

    public void setCode_section(String code_section) {
        this.code_section = code_section;
    }

    @Override
    public String toString() {
        return "Sections{" +
                "name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", code_country='" + code_country + '\'' +
                ", code_section='" + code_section + '\'' +
                '}';
    }
}
