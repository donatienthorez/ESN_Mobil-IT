package org.esn.mobilit.utils.inject;

import java.lang.annotation.Retention;

import java.lang.annotation.RetentionPolicy;

import javax.inject.Qualifier;

/**
 * Used to indicate that the object belongs to the application scope.
 */
@Retention(RetentionPolicy.RUNTIME)
@Qualifier
public @interface ForApplication {
}
