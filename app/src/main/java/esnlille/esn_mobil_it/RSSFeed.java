package esnlille.esn_mobil_it;

import java.io.Serializable;
import java.util.List;
import java.util.Vector;

/**
 * Created by Spider on 21/11/14.
 */
public class RSSFeed implements Serializable {

    private static final long serialVersionUID = 1L;
    private int itemcount = 0;
    private List itemlist;

    public RSSFeed() {
        itemlist = new Vector(0);
    }

    void addItem(Event event) {
        itemlist.add(event);
        itemcount++;
    }

    public Event getItem(int location) {
        return (Event)itemlist.get(location);
    }

    public int getItemCount() {
        return itemcount;
    }

}