package org.esn.mobilit.services.launcher.interfaces;

import org.esn.mobilit.utils.callbacks.NetworkCallback;
import org.esn.mobilit.utils.parser.RSSFeedParser;

public interface Launchable<T> {
    String getString();
    void doAction(NetworkCallback<T> callback);
}
