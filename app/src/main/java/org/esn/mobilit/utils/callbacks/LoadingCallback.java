package org.esn.mobilit.utils.callbacks;

public interface LoadingCallback {
    void onSuccess();
    void onNoAvailableData();
    void onFailure();
    void onProgress(int i);
}
