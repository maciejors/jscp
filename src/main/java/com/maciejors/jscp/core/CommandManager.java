package com.maciejors.jscp.core;

import com.maciejors.jscp.annotations.CommandDescription;
import com.maciejors.jscp.defaultcommands.DefaultExitCommand;
import com.maciejors.jscp.defaultcommands.DefaultHelpCommand;

import java.util.HashMap;
import java.util.Map;

/**
 * A class used to manage commands. Possible operations include registering
 * commands and searching for them
 */
public class CommandManager {

    /**
     * A set of registered commands. Keys are commands' names
     */
    private final Map<String, Command> registeredCommands = new HashMap<>();

    /**
     * Searches for a registered command with the specified name
     *
     * @param commandName Name of the command
     * @return A {@link Command} with the specified name or {@code null} if
     * such command has not been registered
     */
    public Command findCommand(String commandName) {
        return registeredCommands.getOrDefault(commandName, null);
    }

    /**
     * Adds the command to the command set. If a command with the specified
     * name has already been registered, it will be overridden.
     *
     * @param commandName Name of the command. It should consist only of
     *                    alphanumeric characters and underscores.
     * @param command Command to register
     * @return {@code true}, if a command with the same name has been
     * overridden when adding this command to the command set
     */
    public boolean registerCommand(String commandName, Command command) {
        if (!commandName.matches("[a-zA-z0-9_]+")) {
            System.err.println("Warning: command with an invalid name of \"" +
                    commandName + "\" not registered");
            return false;
        }
        boolean isOverriding = findCommand(commandName) != null;
        registeredCommands.put(commandName, command);
        return isOverriding;
    }

    /**
     * Registers the default help command.
     * <br><br>
     * This command accepts one parameter - a command name, and prints the
     * description of a command with a given name included in
     * {@link CommandDescription} annotation
     */
    public void registerDefaultHelpCommand() {
        registerCommand("help",
                new DefaultHelpCommand(this));
    }

    /**
     * Registers the default exit command. All it does is terminate the
     * program entirely using {@code System.exit()}
     */
    public void registerDefaultExitCommand() {
        registerCommand("exit",
                new DefaultExitCommand());
    }
}
