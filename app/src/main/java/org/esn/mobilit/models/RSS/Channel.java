package org.esn.mobilit.models.RSS;

import org.esn.mobilit.utils.parser.DOMParser;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(name = "channel", strict = false)
public class Channel {

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
        }
    }
}
