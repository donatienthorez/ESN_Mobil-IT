package org.esn.mobilit.pojo;

/**
 * Created by Spider on 03/12/14.
 */
public class Countries {
    private String name;
    private String url;
    private String code_country;

    public Countries(String name, String url, String code_country) {
        this.name = name;
        this.url = url;
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

    public String getCode_country() {
        return code_country;
    }

    public void setCode_country(String code_country) {
        this.code_country = code_country;
    }

    @Override
    public String toString() {
        return "Countries{" +
                "name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", code_country='" + code_country + '\'' +
                '}';
    }
}
