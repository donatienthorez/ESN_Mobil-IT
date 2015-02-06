package org.esn.mobilit.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Spider on 27/01/15.
 */
public class SurvivalGuide implements Serializable {
    private ArrayList<Category> categories;

    public SurvivalGuide(){
        this.categories = new ArrayList<Category>();
    }

    public ArrayList<Category> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<Category> categories) {
        this.categories = categories;
    }
}
