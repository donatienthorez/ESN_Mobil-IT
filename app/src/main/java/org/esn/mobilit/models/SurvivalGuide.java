package org.esn.mobilit.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Spider on 27/01/15.
 */
public class SurvivalGuide implements Serializable {
    private ArrayList<Category> firstlevel;
    private ArrayList<Category> secondlevel;
    private ArrayList<Category> thirdlevel;

    public SurvivalGuide(){
        this.firstlevel = new ArrayList<Category>();
        this.secondlevel = new ArrayList<Category>();
        this.thirdlevel = new ArrayList<Category>();
    }

    public ArrayList<Category> getFirstlevel() {
        return firstlevel;
    }

    public void setFirstlevel(ArrayList<Category> firstlevel) {
        this.firstlevel = firstlevel;
    }

    public ArrayList<Category> getSecondlevel() {
        return secondlevel;
    }

    public void setSecondlevel(ArrayList<Category> secondlevel) {
        this.secondlevel = secondlevel;
    }

    public ArrayList<Category> getThirdlevel() {
        return thirdlevel;
    }

    public void setThirdlevel(ArrayList<Category> thirdlevel) {
        this.thirdlevel = thirdlevel;
    }
}
