package org.esn.mobilit.activities;

public interface Callback<T> {
    void onSuccess(T result);
    void onFailure(Exception ex);
}
