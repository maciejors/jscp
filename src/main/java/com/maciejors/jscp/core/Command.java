package com.maciejors.jscp.core;

/**
 * Parent class for all the commands.
 * <br><br>
 * All commands operate on strings (i.e. all passed arguments are interpreted
 * as strings and so are the returned values)
 * <br><br>
 * To add metadata to the command (e.g. its name), annotate it with
 * {@link com.maciejors.jscp.annotations.CommandMetadata}.
 *
 * @see CommandManager
 * @see CommandProcessor
 * @see com.maciejors.jscp.annotations.CommandMetadata
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
