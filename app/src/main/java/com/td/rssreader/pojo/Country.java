package com.td.rssreader.pojo;

/**
 * Created by Spider on 02/12/14.
 */
public class Country {

    private String code_country;
    private String name;
    private String url;

    public Country(String code_country, String name, String url) {
        this.code_country = code_country;
        this.name = name;
        this.url = url;
    }

    public String getCode_country() {
        return code_country;
    }

    public void setCode_country(String code_country) {
        this.code_country = code_country;
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

    @Override
    public String toString() {
        return "Country{" +
                "code_country='" + code_country + '\'' +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
