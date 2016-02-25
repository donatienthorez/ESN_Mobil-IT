package org.esn.mobilit.utils.callbacks;

import org.esn.mobilit.utils.parser.RSSFeedParser;

import retrofit.RetrofitError;

public interface LoadingCallback<T> {
    void onSuccess(T result);
    void onNoAvailableData();
    void onFailure(RetrofitError error);
    void onProgress(int i, RSSFeedParser result);
}
