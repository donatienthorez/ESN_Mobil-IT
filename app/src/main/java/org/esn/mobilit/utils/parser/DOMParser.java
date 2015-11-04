package org.esn.mobilit.utils.parser;

import android.util.Log;

import org.esn.mobilit.models.RSS.RSSItem;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class DOMParser {

	private RSSFeedParser _feed = new RSSFeedParser();
    private static final String TAG = DOMParser.class.getSimpleName();

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
