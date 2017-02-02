package org.esn.mobilit.services.feeds;

import org.esn.mobilit.utils.ApplicationConstants;

public enum FeedType {
    NEWS (ApplicationConstants.FEED_TYPE_NEWS),
    EVENTS (ApplicationConstants.FEED_TYPE_EVENTS),
    PARTNERS (ApplicationConstants.FEED_TYPE_PARTNERS);

    private String cacheableString;

    FeedType(String cacheableString) {
        this.cacheableString = cacheableString;
    }

    public String getCacheableString() {
        return cacheableString;
    }

    public void setCacheableString(String cacheableString) {
        this.cacheableString = cacheableString;
    }
}
