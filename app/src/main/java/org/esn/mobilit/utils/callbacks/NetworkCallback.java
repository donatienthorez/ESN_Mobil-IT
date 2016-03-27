package org.esn.mobilit.utils.callbacks;

public interface NetworkCallback<T> {
    void onSuccess(T result);
    void onNoAvailableData();
    void onFailure(String error);
}
