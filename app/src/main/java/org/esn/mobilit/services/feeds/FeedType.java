package org.esn.mobilit.services.feeds;

import org.esn.mobilit.utils.ApplicationConstants;

import java.io.Serializable;

public enum FeedType implements Serializable {
    NEWS (ApplicationConstants.FEED_TYPE_NEWS),
    EVENTS (ApplicationConstants.FEED_TYPE_EVENTS),
    PARTNERS (ApplicationConstants.FEED_TYPE_PARTNERS);

    private String feedTypeString;

    FeedType(String feedTypeString) {
        this.feedTypeString = feedTypeString;
    }

    public String getFeedTypeString() {
        return feedTypeString;
    }

    public void setFeedTypeString(String feedTypeString) {
        this.feedTypeString = feedTypeString;
    }

    public static FeedType getFeedType(String feedType) {
        for (FeedType feedTypes : FeedType.values() ) {
            if ((feedTypes.getFeedTypeString()).equals(feedType)) {
                return feedTypes;
            }
        }
        return null;
    }
}
