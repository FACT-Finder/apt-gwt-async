package net.omikron.apt.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Set the location (path, URL) for this resource.
 *
 * <p>Inside a test class, clients can expected fields annotated with {@link Location} to be
 * populated at test runtime with the loaded resource. Currently fields of type {@link
 * javax.tools.JavaFileObject} are supported.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Location {

  String value();
}
