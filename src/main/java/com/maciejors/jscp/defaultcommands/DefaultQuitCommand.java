package com.maciejors.jscp.defaultcommands;

import com.maciejors.jscp.annotations.CommandDescription;
import com.maciejors.jscp.core.Command;

/**
 * Default quit command that can be registered by {@link com.maciejors.jscp.core.CommandManager}
 */
@CommandDescription("Quits the program using System.exit()")
public class DefaultQuitCommand extends Command {
    @Override
    public String call(String[] args) {
        System.exit(0);
        return null;
    }
}
