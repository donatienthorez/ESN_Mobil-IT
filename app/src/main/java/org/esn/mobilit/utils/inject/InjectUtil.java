package org.esn.mobilit.utils.inject;


import org.esn.mobilit.MobilITApplication;
import org.esn.mobilit.activities.HomeActivity;

/**
 * Static utility which is responsible for holding the internal ObjectGraph of the application and injecting members.
 */
public class InjectUtil {

    private static AppComponent component;

    /**
     * Initializes the inject util and default object graph for the given application.
     * Should be called during application creation.
     * This method has to run on the main UI thread!
     * @param application application initializing the Inject util
     */
    public static void initialize(MobilITApplication application) {
        AndroidModules servicesModules = new AndroidModules(application);

        component = DaggerAppComponent.builder()
                .androidModules(servicesModules)
                .build();
        component.inject(application);
    }

    public static AppComponent component() {
        return component;
    }
}