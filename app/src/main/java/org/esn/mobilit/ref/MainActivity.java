package org.esn.mobilit.ref;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import org.esn.mobilit.R;

import java.util.List;

/**
 * Created by mada on 10/8/15.
 */
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dummy_activity_main);

        //Define the actual callback on the activity
        DummyDB.getInstance().getAllDummyElements(new DummyCallback<List<DummyElement>>() {
            @Override
            public void onSuccess(List<DummyElement> result) {
                //Suppose that you want to show the second element of the list
                DummyElement dummyElement = result.get(1);
                TextView title = (TextView)findViewById(R.id.title);
                TextView subtitle = (TextView)findViewById(R.id.subtitle);
                title.setText(dummyElement.getTitle());
                title.setText(dummyElement.getSubtitle());
            }

            @Override
            public void onFailure(Exception ex) {
                //Here you should verify the type of error and modify the UI accordingly
            }
        });

    }
}
