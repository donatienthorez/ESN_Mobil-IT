package org.esn.mobilit.ref;

/**
 * Created by mada on 10/8/15.
 */
public interface DummyCallback<T> {
    void onSuccess(T result);
    void onFailure(Exception ex);
}
