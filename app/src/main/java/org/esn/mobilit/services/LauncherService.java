package org.esn.mobilit.services;


public class LauncherService {

    private static LauncherService instance;

    private int count;
    private final int countLimit = 4;

    private LauncherService(){
        this.count = 0;
    }

    public static LauncherService getInstance(){
        if (instance == null){
            instance = new LauncherService();
        }
        return instance;
    }

    public void resetCount() {this.count = 0;}

    public void incrementCount() {
        this.count++;
    }

    public boolean launchHomeActivity(){
        return count == this.countLimit;
    }
}
