package org.esn.mobilit.utils.parser;

import android.util.Log;

import org.esn.mobilit.models.RSS.RSSItem;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

public class DOMParser {

	public static void moveImage(RSSItem item){
        org.jsoup.nodes.Document docHtml = Jsoup.parse(item.getDescription());
        Elements imgEle = docHtml.select("img");
        if(imgEle != null && imgEle.first() != null){
            item.setImage(imgEle.first().attr("src"));

            //Remove image from description
            String description = item.getDescription().replace(imgEle.first().toString(), "");
            item.setDescription(description);
        }
    }
}
