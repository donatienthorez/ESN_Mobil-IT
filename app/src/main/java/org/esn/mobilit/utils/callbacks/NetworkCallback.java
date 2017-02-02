package org.esn.mobilit.utils.callbacks;

public interface NetworkCallback<T> {
    void onNoConnection(T result);
    void onSuccess(T result);
    void onNoAvailableData();
    void onFailure(String error);
}
