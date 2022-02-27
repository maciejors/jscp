package com.maciejors.jscp.core.statements;

public class InvalidStatement extends Statement {

    private final String errorMsg;

    public InvalidStatement(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    @Override
    public String execute() {
        return "Error: " + errorMsg;
    }
}
