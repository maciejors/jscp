package com.maciejors.jscp.core;

import com.maciejors.jscp.core.statements.Statement;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Contains methods to process lines and execute them
 * <br><br>
 * Command call syntax (let the prefix be "!"):<br>
 * !commandName arg1 arg2 "arg with spaces" ...
 */
public class CommandProcessor {

    private final CommandManager commandManager;
    private final String commandPrefix;

    /**
     * @param commandManager A command manager containing a set of
     *                       commands registered by the user
     * @param commandPrefix Prefix to be used when calling a command
     */
    public CommandProcessor(CommandManager commandManager, String commandPrefix) {
        this.commandManager = commandManager;
        this.commandPrefix = commandPrefix;
    }

    private Statement parseStatement(String line) {
        return null;
    }

    /**
     * Executes a line. A line can for example contain a command invocation
     *
     * @param line Line to be executed
     * @return A value returned by the executed statement, or {@code null}
     * if no value has been returned
     */
    public String executeLine(String line) {
        Statement statement = parseStatement(line);
        if (statement == null) {
            return "Parsing error: invalid statement";
        }
        return statement.execute();
    }

    /**
     * Starts a loop, where in each iteration a single statement gets
     * executed.
     *
     * @param inputStream  A stream providing lines to be executed
     * @param outputStream A stream where the output will be printed
     */
    public void startLoop(InputStream inputStream, OutputStream outputStream) {
    }

    public String getCommandPrefix() {
        return commandPrefix;
    }
}
