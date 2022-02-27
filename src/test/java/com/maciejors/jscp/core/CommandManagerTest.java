package com.maciejors.jscp.core;

import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

public class CommandManagerTest {

    @Test
    public void commandsWithSpacesNotRegistered() {
        // arrange
        Command command = new Command() {
            @Override
            public String call(String[] args) {
                return null;
            }
        };
        CommandManager commandManager = new CommandManager();

        // act
        commandManager.registerCommand("a cool command", command);

        // assert
        assertNull(commandManager.findCommand("a cool command"));
    }

    @Test
    public void commandsWithIllegalCharactersNotRegistered() {
        // arrange
        Command command = new Command() {
            @Override
            public String call(String[] args) {
                return null;
            }
        };
        CommandManager commandManager = new CommandManager();

        // act
        commandManager.registerCommand("a@cool#\"command", command);

        // assert
        assertNull(commandManager.findCommand("a@cool#\"command"));
    }

    @Test
    public void duplicateCommandsGetOverridden() {
        // arrange
        CommandManager spt = new CommandManager();

        Command command = new Command() {
            @Override
            public String call(String[] args) {
                return null;
            }
        };

        Command commandOverride = new Command() {
            @Override
            public String call(String[] args) {
                return null;
            }
        };

        spt.registerCommand("myCommand", command);
        spt.registerCommand("myCommand", commandOverride);

        // act
        Command foundCommand = spt.findCommand("myCommand");

        // assert
        assertSame(commandOverride, foundCommand);
    }

}