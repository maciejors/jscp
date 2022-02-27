package com.maciejors.jscp.core;

import com.maciejors.jscp.core.statements.CommandCall;
import com.maciejors.jscp.core.statements.Statement;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 * Contains methods to process lines and execute them
 * <br><br>
 * Command call syntax (given the prefix is "!"):<br>
 * !commandName arg1 arg2 "arg with spaces" ...
 */
public class CommandProcessor {

    private final CommandManager commandManager;

    /**
     * Prefix to be used when calling a command
     */
    private final String commandPrefix;

    /**
     * @param commandManager A command manager containing a set of
     *                       commands registered by the user
     */
    public CommandProcessor(CommandManager commandManager) {
        this.commandManager = commandManager;
        this.commandPrefix = "!";
    }

    /**
     * Parses a statement from the input
     *
     * @param line A line of input
     * @return A {@link Statement} object that can be later executed
     */
    private Statement parseStatement(String line) {
        // command name and arguments are separated by spaces
        StringTokenizer tokenizer = new StringTokenizer(" ");

        // First token determines the type of statement.
        // For now the only statement type is the command call, so the program
        // just makes sure that the call starts with the command prefix
        String firstToken = tokenizer.nextToken();

        if (firstToken.startsWith(commandPrefix)) {
            Command command = commandManager.findCommand(firstToken.substring(1));
            String[] args;

            // if command == null there is no reason to parse args anyway
            if (command == null) {
                args = null;
            } else {
                args = parseArguments(tokenizer);
            }

            return new CommandCall(command, args);
        }

        return null;
    }

    /**
     * Parses an array of arguments as Strings from user's input
     *
     * @param tokenizer A tokenizer that initiated statement parsing
     * @return An array of command parameters
     */
    private String[] parseArguments(StringTokenizer tokenizer) {
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
     * @param inputStream A stream providing lines to be executed
     * @param printStream A stream where the output will be printed
     */
    public void startLoop(InputStream inputStream, PrintStream printStream) {
        Scanner scanner = new Scanner(inputStream);
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            String output = executeLine(line);
            // if output == null, it doesn't get printed
            if (output != null) {
                printStream.println(output);
            }
        }
    }

    public String getCommandPrefix() {
        return commandPrefix;
    }
}
