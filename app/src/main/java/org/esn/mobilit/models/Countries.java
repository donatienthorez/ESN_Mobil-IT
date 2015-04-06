package org.esn.mobilit.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Spider on 03/12/14.
 */
public class Countries implements Serializable{

    private ArrayList<Country> countries;
    private String revision;

    public Countries(String revision) {
        this.countries = new ArrayList<Country>();
        this.revision  = revision;
    }

    public Country getCountryFromSection(Section section){
        for (Country c : countries){
            if (c.getId() == section.getId_country()){
                return c;
            }
        }
        return null;
    }
    public ArrayList<Country> getCountries() {
        return countries;
    }

    public void setCountries(ArrayList<Country> countries) {
        this.countries = countries;
    }

    public void addCountry(Country country){
        this.getCountries().add(country);
    }
    public String getRevision() {
        return revision;
    }

    public void setRevision(String revision) {
        this.revision = revision;
    }

    @Override
    public String toString() {
        return "Countries{" +
                "countries=" + countries +
                ", revision='" + revision + '\'' +
                '}';
    }
}
