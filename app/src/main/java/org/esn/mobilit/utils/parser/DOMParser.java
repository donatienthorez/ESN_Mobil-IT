package org.esn.mobilit.utils.parser;

import org.esn.mobilit.models.RSS.RSSItem;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

public class DOMParser {

	public static void moveImage(RSSItem item){
        org.jsoup.nodes.Document docHtml = Jsoup.parse(item.getDescription());
        Elements imgEle = docHtml.select("img");
        Elements colorboxLink = docHtml.getElementsByClass("colorbox");

        if(imgEle != null && imgEle.first() != null) {
            item.setImage(imgEle.first().attr("src"));
            String description = item.getDescription().replace(imgEle.first().toString(), "");
            item.setDescription(description);
        }
        /**
         * ColorBoxLink is a image with a better quality but not present all the time
         * if we find it we replace the image by the better one
         */
        if (colorboxLink != null && colorboxLink.first() != null) {
            item.setImage(colorboxLink.first().attr("href"));
        }
    }
}
