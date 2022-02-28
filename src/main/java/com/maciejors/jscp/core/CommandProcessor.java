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

    public static void main(String[] args) {
        CommandManager commandManager = new CommandManager();
        commandManager.registerDefaultExitCommand();
        commandManager.registerDefaultHelpCommand();

        CommandProcessor commandProcessor = new CommandProcessor(commandManager);
        commandProcessor.startLoop(System.in, System.out);
    }

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
        String firstToken;
        try {
            firstToken = tokenizer.nextToken(" ");
        } catch (NoSuchElementException err) {
            // when the input is blank
            return null;
        }

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

        return new InvalidStatement("Parsing error: invalid statement");
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
            String currArg = token;

            // double quotes begin
            if (currArg.startsWith("\"")) {
                // removing " from the beginning
                currArg = currArg.substring(1);

                // the following condition will be true if the token
                // looks like this: "quotedArgument"
                if (currArg.length() != 1 && endsWithDoubleQuote(currArg)) {
                    // removing " from the end
                    currArg = currArg.substring(0, currArg.length() - 1);
                }
                // otherwise, program will look for the closing quote
                // somewhere further after the token
                else {
                    // tokens will be concatenated until an unescaped
                    // double quote is found
                    StringBuilder currArgBuilder = new StringBuilder(currArg);

                    // looking for the closing quote
                    while (true) {
                        // looking for the next double quote
                        try {
                            token = tokenizer.nextToken("\"");
                        } catch (NoSuchElementException err) {
                            // unmatched double quote produces error
                            return null;
                        }
                        currArgBuilder.append(token);

                        // when the quote is not escaped it is treated as a
                        // closing quote
                        if (!token.endsWith("\\")) {
                            currArg = currArgBuilder.toString();
                            // "skipping" the closing double quote (it
                            // follows the token in the input string)
                            tokenizer.nextToken(" ");
                            break;
                        } else {
                            // adding an escaped double quote
                            currArgBuilder.append("\"");
                        }
                    }
                }
            }

            // Here the program looks for unescaped quotes in the middle
            // of the passed argument. Theoretically they could be left,
            // but for consistency all double quotes have to be escaped.

            // check if an unescaped quote appears at the end of the token
            if (endsWithDoubleQuote(currArg)) {
                return null;
            }

            // look for unescaped quotes in the middle of the token
            for (int i = 1; i < currArg.length(); i++) {
                if (currArg.charAt(i) == '"') {
                    if (currArg.charAt(i - 1) != '\\') {
                        return null;
                    }
                }
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
     *
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
            return "";
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
            printStream.println(output);
        }
    }

    public String getCommandPrefix() {
        return commandPrefix;
    }
}
