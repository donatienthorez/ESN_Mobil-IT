package org.esn.mobilit.services;


public class LauncherService {

    private static LauncherService instance;

    private int count;
    private final int countLimit = 5;

    private LauncherService(){
        this.count = 0;
    }

    public static LauncherService getInstance(){
        if (instance == null){
            instance = new LauncherService();
        }
        return instance;
    }

    public void incrementCount() {
        this.count++;
    }

    public boolean launchHomeActivity(){
        return count == this.countLimit;
    };
}
