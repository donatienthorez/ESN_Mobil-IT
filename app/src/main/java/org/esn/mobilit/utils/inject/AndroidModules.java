package org.esn.mobilit.utils.inject;

import android.app.Application;
import android.content.Context;

import org.esn.mobilit.MobilITApplication;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AndroidModules {
    private MobilITApplication application;

    public AndroidModules(MobilITApplication application) {
        this.application = application;
    }
    /**
     * Allow the application context to be injected but require that it be annotated with
     * {@link ForApplication @Annotation} to explicitly differentiate it from an activity context.
     */
    @Provides @Singleton @ForApplication Context provideApplicationContext() {
        return application;
    }

    @Provides
    @Singleton
    Application providesApplication() {
        return application;
    }
}
