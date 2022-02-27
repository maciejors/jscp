package com.maciejors.jscp.core;

import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CommandProcessorTest {

    @Test
    public void lineExecutionProducesOutput() {
        // arrange
        CommandProcessor spt = getSampleCommandProcessor();

        // act
        String commandOutput = spt.executeLine("!concat tic tac");

        // assert
        assertEquals("tictac", commandOutput);
    }

    @Test
    public void argumentsInDoubleQuotesCorrectlyProcessed() {
        // arrange
        CommandProcessor spt = getSampleCommandProcessor();

        // act
        String commandOutput = spt.executeLine("!concat Ala \" ma kota\" \".\"");

        // assert
        assertEquals("Ala ma kota.", commandOutput);
    }

    @Test
    public void duplicateCommandsGetOverridden() {
        // arrange
        CommandProcessor spt = getSampleCommandProcessor();

        // act
        String commandOutput = spt.executeLine("!increaseInt 1");

        // assert
        assertEquals("11", commandOutput);
    }

    private CommandProcessor getSampleCommandProcessor() {

        Command concatStringsCommand = new Command() {
            @Override
            public String call(String[] args) {
                return String.join("", args);
            }
        };

        // Increases int from input by 5. Will be overridden later
        Command increaseInt = new Command() {
            @Override
            public String call(String[] args) {
                return String.valueOf(
                        Integer.parseInt(args[0]) + 5
                );
            }
        };

        // Increases int from input by 10.
        Command increaseIntOverride = new Command() {
            @Override
            public String call(String[] args) {
                return String.valueOf(
                        Integer.parseInt(args[0]) + 10
                );
            }
        };

        Command returnNull = new Command() {
            @Override
            public String call(String[] args) {
                return null;
            }
        };

        CommandManager commandManager = new CommandManager();

        commandManager.registerCommand("concat", concatStringsCommand);
        commandManager.registerCommand("increaseInt", increaseInt);
        commandManager.registerCommand("increaseInt", increaseIntOverride);
        commandManager.registerCommand("null", returnNull);
        // name contains illegal characters (spaces)
        commandManager.registerCommand("add two numbers", returnNull);

        return new CommandProcessor(commandManager, "!");
    }
}
