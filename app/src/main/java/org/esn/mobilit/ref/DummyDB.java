package org.esn.mobilit.ref;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mada on 10/8/15.
 */
public class DummyDB {

    private static DummyDB instance;

    private DummyDB(){

    }

    public static DummyDB getInstance(){
        if (instance == null){
            instance = new DummyDB();
        }
        return instance;
    }

    public void getAllDummyElements(DummyCallback<List<DummyElement>> callback){

        //Assume the creation of this list is async
        DummyElement dummyElement1 = new DummyElement("title1","subtitle1");
        DummyElement dummyElement2 = new DummyElement("title2","subtitle2");
        DummyElement dummyElement3 = new DummyElement("title3","subtitle3");
        List<DummyElement> list = new ArrayList<DummyElement>();
        list.add(dummyElement1);
        list.add(dummyElement2);
        list.add(dummyElement3);

        //When the job/task is ready, you call onSuccess on the callback
        callback.onSuccess(list);
    }

}
