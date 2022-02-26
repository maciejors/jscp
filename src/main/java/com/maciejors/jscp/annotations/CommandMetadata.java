package com.maciejors.jscp.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Contains data about the command (e.g. its name)
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface CommandMetadata {

    /**
     * Command name used when calling the command. If not specified, it will
     * be set to the command class name when registering the command
     */
    String name() default "";

    /**
     * Command description printed when using default help command
     */
    String defaultHelpDescription() default "";

    /**
     * Minimal required number of arguments passed when calling the command
     */
    int requiredNumberOfArguments() default 0;
}
