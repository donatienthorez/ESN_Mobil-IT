package org.esn.mobilit.models.RSS;

import org.esn.mobilit.utils.parser.DOMParser;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;

import java.util.List;

@Root(name = "RSSChannel", strict = false)
public class RSSChannel {

    @Element(name = "title", required = false)
    private String title;

    @ElementList(entry = "link", inline = true, required = false)
    private List<Link> links;

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

    public static class Link {
        @Attribute(required = false)
        public String href;

        @Attribute(required = false)
        public String rel;

        @Attribute(name = "type", required = false)
        public String contentType;

        @Text(required = false)
        public String link;
    }
}
