package com.maciejors.jscp.core.statements;

import com.maciejors.jscp.core.Command;

/**
 * Represents the command call
 */
public class CommandCall extends Statement {

    private final Command command;

    /**
     * Arguments passed with the command call
     */
    private final String[] args;

    public CommandCall(Command command, String[] args) {
        this.command = command;
        this.args = args;
    }

    /**
     * Executes the command with passed arguments
     *
     * @return A value returned by the command
     */
    @Override
    public String execute() {
        return null;
    }
}
