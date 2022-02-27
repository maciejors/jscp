package com.maciejors.jscp.defaultcommands;

import com.maciejors.jscp.annotations.CommandDescription;
import com.maciejors.jscp.core.Command;
import com.maciejors.jscp.core.CommandManager;

@CommandDescription("Returns a description of the command of a given name.\n" +
        "Try out: !help <command-name>")
public class DefaultHelpCommand extends Command {

    /**
     * CommandManager is needed to access commands' descriptions
     */
    private CommandManager commandManager;

    public DefaultHelpCommand(CommandManager commandManager) {
        this.commandManager = commandManager;
    }

    @Override
    public String call(String[] args) {
        String commandName;
        if (args.length == 0) {
            // when invoked without any arguments, help command will return
            // its own description
            commandName = "help";
        } else {
            commandName = args[0];
        }

        Command command = commandManager.findCommand(commandName);

        if (command == null) {
            return "Can't find a command named \"" + commandName + "\"";
        }

        if (!command.getClass()
                .isAnnotationPresent(CommandDescription.class)) {
            return "No description provided for " + commandName;
        }

        String result = "Help for " + commandName + ":\n\n";
        String description = command.getClass()
                .getAnnotation(CommandDescription.class)
                .value();
        result += description;

        return result;
    }
}
