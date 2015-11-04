package org.esn.mobilit.models.RSS;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "rss", strict = false)
public class RSS {

    @Element(name = "channel", required= true)
    public RSSChannel RSSChannel;

    @Attribute(name = "version", required = false)
    public String version;

    @Attribute(name = "base", required = false)
    public String base;

    @Attribute(name = "dc", required = false)
    public String dc;

    public RSSChannel getRSSChannel() {
        return RSSChannel;
    }
}
