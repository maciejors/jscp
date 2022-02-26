package com.maciejors.jscp.core;

import com.maciejors.jscp.annotations.CommandDescription;

/**
 * Parent class for all the commands.
 * <br><br>
 * All commands operate on strings (i.e. all passed arguments are interpreted
 * as strings and so are the returned values)
 * <br><br>
 * To add a description to the command, annotate it with
 * {@link CommandDescription}.
 *
 * @see CommandManager
 * @see CommandProcessor
 */
public abstract class Command {

    /**
     * Method invoked when calling the command
     *
     * @param args argument passed when calling the command
     * @return the value returned by command, can be {@code null}
     */
    public abstract String call(String[] args);
}
