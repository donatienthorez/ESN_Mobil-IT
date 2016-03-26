package org.esn.mobilit.utils.callbacks;

public interface Callback<T> {
    void onSuccess(T result);
    void onFailure(String s);
}
