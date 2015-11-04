package org.esn.mobilit.services;

import org.esn.mobilit.utils.callbacks.NetworkCallback;
import org.esn.mobilit.models.RevisionList;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.GET;

public class RevisionService {

    private static RevisionService instance;
    private static String revision;

    private RevisionService(){
        instance = new RevisionService();
    }

    public static RevisionService getInstance(){
            return instance;
    }

    private static RestAdapter restAdapter = new RestAdapter.Builder()
            .setEndpoint("http://esnlille.fr")
            .build();

    private interface RevisionServiceInterface {
        @GET("/webservices/esnmobilit/getRevision.php")
        void revision(Callback<RevisionList> callback);
    }

    public static String getRevision() {
        return revision;
    }

    public static void getRevision(final NetworkCallback<String> callback) {
        if (revision != null){
            callback.onSuccess(revision);
        } else {
            makeRevisionRequest(callback);
        }
    }

    public static void makeRevisionRequest(final NetworkCallback<String> callback){
        RevisionServiceInterface service = restAdapter.create(RevisionServiceInterface.class);
        service.revision(new Callback<RevisionList>() {
            @Override
            public void success(RevisionList revisionList, Response response) {
                revision = revisionList.getRevision(0).getDate();
                callback.onSuccess(revision);
            }

            @Override
            public void failure(RetrofitError error) {
                callback.onFailure(error);
            }
        });
    }
}
