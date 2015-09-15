package org.esn.mobilit.network;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.esn.mobilit.models.Category;
import org.esn.mobilit.models.SurvivalGuide;
import org.esn.mobilit.utils.ApplicationConstants;
import org.esn.mobilit.utils.Utils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class JSONfunctions {

    private static final String TAG = "JSONfunctions";

    public static JSONObject getJSONfromURL(String url) {
        InputStream is = null;
        String result = "";
        JSONObject jArray = null;

        // Download JSON data from URL
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url);
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();

        } catch (Exception e) {
            Log.e("log_tag", "Error in http connection " + e.toString());
        }

        // Convert response to string
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            result = sb.toString();
        } catch (Exception e) {
            Log.e("log_tag", "Error converting result " + e.toString());
        }

        try {

            jArray = new JSONObject(result);
        } catch (JSONException e) {
            Log.e("log_tag", "Error parsing data " + e.toString());
        }

        return jArray;
    }

    public static SurvivalGuide getSurvivalGuide(String url){

        SurvivalGuide survivalguide = new SurvivalGuide();

        JSONObject jsonobject = JSONfunctions
                .getJSONfromURL(url);
        try {
            // SURVIVAL GUIDE LEVEL 1
            JSONArray jsonarray_level1 = jsonobject.getJSONArray("categories");
            for (int i = 0; i < jsonarray_level1.length(); i++) {
                JSONObject jsonobject_level1 = jsonarray_level1.getJSONObject(i);
                Category category = new Category(
                        jsonobject_level1.optInt("id"),
                        jsonobject_level1.optString("name"),
                        jsonobject_level1.optString("section"),
                        jsonobject_level1.optString("content"),
                        0,
                        jsonobject_level1.optInt("position")
                );
                survivalguide.getCategories().add(category);

                // SURVIVAL GUIDE LEVEL 2
                JSONArray jsonarray_level2 = jsonobject_level1.getJSONArray("categories");
                for (int j = 0; j < jsonarray_level2.length(); j++) {
                    JSONObject jsonobject_level2 = jsonarray_level2.getJSONObject(j);
                    Category categorylvl2 = new Category(
                            jsonobject_level2.optInt("id"),
                            jsonobject_level2.optString("name"),
                            jsonobject_level2.optString("section"),
                            jsonobject_level2.optString("content"),
                            1,
                            jsonobject_level2.optInt("position")
                    );
                    survivalguide.getCategories().add(categorylvl2);

                    // SURVIVAL GUIDE LEVEL 3
                    JSONArray jsonarray_level3 = jsonobject_level2.getJSONArray("categories");
                    for (int k = 0; k < jsonarray_level3.length(); k++) {
                        JSONObject jsonobject_level3 = jsonarray_level3.getJSONObject(k);
                        Category categorylvl3 = new Category(
                                jsonobject_level3.optInt("id"),
                                jsonobject_level3.optString("name"),
                                jsonobject_level3.optString("section"),
                                jsonobject_level3.optString("content"),
                                2,
                                jsonobject_level3.optInt("position")
                        );
                        survivalguide.getCategories().add(categorylvl3);
                    }
                }
            }
        } catch (Exception e) {
            Log.d(TAG,"Error" + e.getMessage());
            e.printStackTrace();
        }

        return survivalguide;

    }
}
