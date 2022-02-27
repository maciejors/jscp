package com.maciejors.jscp.core;

import com.maciejors.jscp.core.statements.CommandCall;
import com.maciejors.jscp.core.statements.InvalidStatement;
import com.maciejors.jscp.core.statements.Statement;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.*;

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
        StringTokenizer tokenizer = new StringTokenizer(line);

        // First token determines the type of statement.
        // For now the only statement type is the command call, so the program
        // just makes sure that the call starts with the command prefix
        String firstToken = tokenizer.nextToken(" ");

        if (firstToken.startsWith(commandPrefix)) {
            Command command = commandManager.findCommand(firstToken.substring(1));

            // command not found
            if (command == null) {
                return new InvalidStatement("command not found");
            }

            String[] args = parseCommandArguments(tokenizer);

            // arguments failed to parse
            if (args == null) {
                return new InvalidStatement("error parsing arguments");
            }

            return new CommandCall(command, args);
        }

        return null;
    }

    /**
     * Parses an array of arguments as Strings from user's input
     *
     * @param tokenizer A tokenizer that initiated statement parsing
     * @return An array of command parameters or {@code null} if parsing failed
     */
    private String[] parseCommandArguments(StringTokenizer tokenizer) {
        Deque<String> argsDeque = new LinkedList<>();

        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken(" ");
            String currArg;

            // double quotes begin
            if (token.startsWith("\"")) {
                // removing " from the beginning
                token = token.substring(1);

                // the following condition will be true if the token
                // looks like this: "quotedArgument"
                if (token.length() != 1 && endsWithDoubleQuote(token)) {
                    // removing " from the end
                    currArg = token.substring(0, token.length() - 1);
                }
                // otherwise, program will look for the closing quote
                // somewhere further after the token
                else {
                    // tokens will be concatenated until an unescaped
                    // double quote is found
                    StringBuilder currArgBuilder = new StringBuilder();

                    // looking for the closing quote
                    while (true) {
                        currArgBuilder.append(token);

                        // looking for the next double quote
                        try {
                            token = tokenizer.nextToken("\"");
                        } catch (NoSuchElementException err) {
                            // unmatched double quote produces error
                            return null;
                        }

                        // when the quote is not escaped it is treated as a
                        // closing quote
                        if (!token.endsWith("\\")) {
                            currArgBuilder.append(token);
                            // "skipping" the closing double quote (it
                            // follows the token in the input string)
                            String temp = tokenizer.nextToken(" ");
                            break;
                        }
                    }

                    currArg = currArgBuilder.toString();
                }
            }
            // else = if the token doesn't start with double quote
            else {
                // Here the program looks for unescaped quotes in the middle
                // of the passed argument. Theoretically they could be left,
                // but for consistency all double quotes have to be escaped.

                // check if an unescaped quote appears at the end of the token
                if (endsWithDoubleQuote(token)) {
                    return null;
                }

                // look for unescaped quotes in the middle of the token
                StringTokenizer tokenizerDoubleQuotesDelim = new StringTokenizer(token, "\"");
                while (tokenizerDoubleQuotesDelim.countTokens() > 1) {
                    // piece of text just before the next double quote
                    String piece = tokenizerDoubleQuotesDelim.nextToken();
                    // Check if the following quote is escaped.
                    if (endsWithDoubleQuote(piece + "\"")) {
                        return null;
                    }
                }

                currArg = token;
            }

            // remove backslashes from escaped double quotes
            currArg = currArg.replace("\\\"", "\"");
            argsDeque.add(currArg);
        }

        int numberOfArguments = argsDeque.size();
        String[] args = new String[numberOfArguments];
        for (int i = 0; i < numberOfArguments; i++) {
            args[i] = argsDeque.removeFirst();
        }
        return args;
    }

    /**
     * Checks if a string ends with an unescaped double quote.
     * @param s A {@link String} object to be checked
     */
    private boolean endsWithDoubleQuote(String s) {
        return s.endsWith("\"") &&  // does it end with "?
                !s.endsWith("\\\"");  // is " not escaped?
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
