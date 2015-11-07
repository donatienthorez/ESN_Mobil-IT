package org.esn.mobilit.utils.image;

import android.content.Context;

import org.esn.mobilit.MobilITApplication;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public final class InternalStorage{

    public static void writeObject(String key, Object object) throws IOException {
        FileOutputStream fos = MobilITApplication.getContext().openFileOutput(key, Context.MODE_PRIVATE);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(object);
        oos.close();
        fos.close();
    }

    public static Object readObject(String key) throws IOException,
            ClassNotFoundException {
        FileInputStream fis = MobilITApplication.getContext().openFileInput(key);
        ObjectInputStream ois = new ObjectInputStream(fis);
        Object object = ois.readObject();
        return object;
    }

    public static boolean deleteObject(String key) throws IOException {

        return MobilITApplication.getContext().deleteFile(key);
    }
}
