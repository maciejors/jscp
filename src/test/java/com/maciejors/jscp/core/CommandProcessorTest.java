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
    public void argumentsInDoubleQuotesCorrectlySplit() {
        // arrange
        CommandProcessor spt = getSampleCommandProcessor();

        // act
        String commandOutput = spt.executeLine("!concat Ala \" ma kota\" \".\"");

        // assert
        assertEquals("Ala ma kota.", commandOutput);
    }

    @Test
    public void unescapedQuotesInsideArgumentsProduceInvalidStatement() {
        // arrange
        CommandProcessor spt = getSampleCommandProcessor();

        // act
        String commandOutput = spt.executeLine("!concat t\"ic \"ta\"c\"");

        // assert
        assertTrue(commandOutput.startsWith("Error"));
    }

    @Test
    public void spacesInsideQuotesDoNotChangeParsingBehaviour() {
        // arrange
        CommandProcessor spt = getSampleCommandProcessor();

        // act
        String commandOutput = spt.executeLine("!concat \"Ala \" \" ma\" \" kota \"");

        // assert
        assertEquals("Ala  ma kota ", commandOutput);
    }

    @Test
    public void quotesCorrectlyEscaped() {
        // arrange
        CommandProcessor spt = getSampleCommandProcessor();

        // act
        String commandOutput = spt.executeLine("!concat \\\"QuotedText\\\" \"NoQuotes\"");

        // assert
        assertEquals("\"QuotedText\"NoQuotes", commandOutput);
    }

    @Test
    public void unmatchedDoubleQuoteProducesInvalidStatement() {
        // arrange
        CommandProcessor spt = getSampleCommandProcessor();

        // act
        String commandOutput = spt.executeLine("!concat \"tic\" \"tac");

        // assert
        assertTrue(commandOutput.startsWith("Error"));
    }

    @Test
    public void unmatchedDoubleQuoteAtTheEndProducesInvalidStatement() {
        // arrange
        CommandProcessor spt = getSampleCommandProcessor();

        // act
        String commandOutput = spt.executeLine("!concat \"tic\" tac\"");

        // assert
        assertTrue(commandOutput.startsWith("Error"));
    }


    private CommandProcessor getSampleCommandProcessor() {

        Command concatStringsCommand = new Command() {
            @Override
            public String call(String[] args) {
                return String.join("", args);
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
        commandManager.registerCommand("null", returnNull);
        // name contains illegal characters (spaces)
        commandManager.registerCommand("add two numbers", returnNull);

        return new CommandProcessor(commandManager);
    }
}
