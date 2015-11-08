package org.esn.mobilit.models;

import java.util.ArrayList;

public class Abouts {
    private ArrayList<About> section;

    public Abouts(){
        section = new ArrayList<About>();
    }

    public boolean hasAbout() {
        return section.size() > 0;
    }

    public About getAbout(){
        return section.size() > 0 ? section.get(0) : new About();
    }
}
