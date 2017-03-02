package org.esn.mobilit.services.cache;

import android.content.Context;

import org.esn.mobilit.utils.inject.ForApplication;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class InternalStorage {

    @ForApplication
    @Inject
    Context context;

    @Inject
    public InternalStorage() {
    }

    public void writeObject(String key, Object object) throws IOException {
        FileOutputStream fos = context.openFileOutput(key, Context.MODE_PRIVATE);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(object);
        oos.close();
        fos.close();
    }

    public boolean objectExists(String key) {
        try {
            FileInputStream fis = context.openFileInput(key);
        } catch (FileNotFoundException e) {
            return false;
        }
        return true;
    }

    public void deleteObject(String key) throws IOException {
        File dir =  context.getFilesDir();
        File fileToDelete = new File(dir, key);
        fileToDelete.delete();
    }

    public Object readObject(String key) throws IOException,
            ClassNotFoundException {
        FileInputStream fis =  context.openFileInput(key);
        ObjectInputStream ois = new ObjectInputStream(fis);
        return ois.readObject();
    }
}
