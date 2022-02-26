package com.maciejors.jscp.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Stores command description. That description is used by the default
 * help command
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface CommandDescription {

    /**
     * Command description
     */
    String defaultHelpDescription();
}
