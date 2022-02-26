package com.maciejors.jscp.core.statements;

/**
 * Represents a single-line statement
 */
public abstract class Statement {

    /**
     * Executes the statement
     *
     * @return Value returned from the executed statement or {@code null}
     */
    public abstract String execute();
}
