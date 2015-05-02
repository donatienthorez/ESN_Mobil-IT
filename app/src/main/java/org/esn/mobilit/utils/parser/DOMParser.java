package org.esn.mobilit.utils.parser;

import android.util.Log;

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

	private RSSFeed _feed = new RSSFeed();
    private static final String TAG = DOMParser.class.getSimpleName();

	public RSSFeed parseXml(String xml) {

		URL url = null;

		try {
			url = new URL(xml);
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
            Log.d(TAG, "ERROR MalformedURLException : " + e1.toString());
		}

        try {
            URLConnection urlc = url.openConnection();
            urlc.addRequestProperty("User-Agent", "firefox");
        }catch (IOException ioe){
            Log.d(TAG, "ERROR IOException : Adding User-agent : " + ioe.toString());
        }

		try {
			// Create required instances
			DocumentBuilderFactory dbf;
			dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = null;

            try{
                doc = db.parse(new InputSource(url.openStream()));
            }catch (Exception e){
                Log.d(TAG, e.toString());
                e.printStackTrace();
            }

			doc.getDocumentElement().normalize();

			// Get all <item> tags.
			NodeList nl = doc.getElementsByTagName("item");
			int length = nl.getLength();
			for (int i = 0; i < length; i++) {
				Node currentNode = nl.item(i);
				RSSItem _item = new RSSItem();

				NodeList nchild = currentNode.getChildNodes();
				int clength = nchild.getLength();

				// Get the required elements from each Item
				for (int j = 1; j < clength; j = j + 2) {

					Node thisNode = nchild.item(j);
					String theString = null;
					String nodeName = thisNode.getNodeName();

					theString = nchild.item(j).getFirstChild().getNodeValue();

					if (theString != null) {
						if ("title".equals(nodeName)) {
							// Node name is equals to 'title' so set the Node
							// value to the Title in the RSSItem.
							_item.setTitle(theString);
						}
						else if ("description".equals(nodeName)) {
							// Get the image url
							String html = theString;
							org.jsoup.nodes.Document docHtml = Jsoup.parse(html);
							Elements imgEle = docHtml.select("img");
							_item.setImage(imgEle.first().attr("src"));

                            //Remove image from description
                            String description = theString.replace(imgEle.first().toString(), "");
                            _item.setDescription(description);

                            // Get the date
                            Elements dateEle = docHtml.select("span.date-display-single");
                            if (dateEle.size() == 1){
                                _item.setDate(dateEle.text());
                            }
						}
                        else if ("link".equals(nodeName)) {
                            _item.setLink(theString);
                        }
						else if ("pubDate".equals(nodeName)) {
							String formatedDate = theString.replace(" +0000","");
							_item.set_pubDate(formatedDate);
						}

					}
				}

				// add item to the list
				_feed.addItem(_item);
			} // end FOR

		} catch (Exception e) {
		}

		// Return the final feed once all the Items are added to the RSSFeed
		// Object(_feed).
		return _feed;
	}

    //private String getText()

}
