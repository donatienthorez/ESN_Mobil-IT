package org.esn.mobilit.models.RSS;

import com.bumptech.glide.Glide;

import org.esn.mobilit.MobilITApplication;
import org.esn.mobilit.utils.parser.DOMParser;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(name = "RSSChannel", strict = false)
public class RSSChannel {

    @Element(name = "title", required = false)
    private String title;

    @Element(name = "link", required = false)
    private String link;

    @Element(name = "description", required = false)
    private String description;

    @Element(name = "language", required = false)
    private String language;

    @ElementList(name = "item", inline = true)
    private List<RSSItem> rssItemList;

    public List<RSSItem> getList(){
        return rssItemList;
    }

    public void moveImage(){
        for(RSSItem item : getList())
        {
            DOMParser.moveImage(item);

            Glide.with(MobilITApplication.getContext())
                    .load(item.getImage())
                    .downloadOnly(150, 250);
        }
    }
}
