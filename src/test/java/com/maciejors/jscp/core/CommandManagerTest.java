package com.maciejors.jscp.core;

import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CommandManagerTest {

    @Test
    public void commandsWithSpacesNotRegistered() {
        // arrange
        Command addTwoNumbers = new Command() {
            @Override
            public String call(String[] args) {
                int n1 = Integer.parseInt(args[0]);
                int n2 = Integer.parseInt(args[1]);
                return String.valueOf(n1 + n2);
            }
        };
        CommandManager commandManager = new CommandManager();

        // act
        commandManager.registerCommand("add two numbers", addTwoNumbers);

        // assert
        assertNull(commandManager.search("add two numbers"));
    }

    @Test
    public void commandsWithIllegalCharactersNotRegistered() {
        // arrange
        Command addTwoNumbers = new Command() {
            @Override
            public String call(String[] args) {
                int n1 = Integer.parseInt(args[0]);
                int n2 = Integer.parseInt(args[1]);
                return String.valueOf(n1 + n2);
            }
        };
        CommandManager commandManager = new CommandManager();

        // act
        commandManager.registerCommand("add@two#numbers", addTwoNumbers);

        // assert
        assertNull(commandManager.search("add@two#numbers"));
    }

}