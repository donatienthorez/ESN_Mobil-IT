package org.esn.mobilit;

import retrofit.RetrofitError;

public interface NetworkCallback<T> {
    void onSuccess(T result);
    void onFailure(RetrofitError error);
}
