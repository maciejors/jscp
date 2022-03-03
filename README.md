# JSCP
Simple Command Processor for Java

## How to use it?

#### 1. Creating a simple command

```java
import com.maciejors.jscp.annotations.CommandDescription;
import com.maciejors.jscp.core.Command;

@CommandDescription("optional description")
public class MyCommand extends Command {
    @Override
    public String call(String[] args) {
        return String.join("_", args);
    }
}
```

#### 2. Registering commands and running the program

To register a command, create an instance of the `CommandManager` 
class and use the `registerCommand` method.

The simplest way to use your commands is to run JSCP in the command line:

```java
import com.maciejors.jscp.core.CommandManager;
import com.maciejors.jscp.core.CommandProcessor;

public class Main {
    public static void main(String[] args) {
        // Registering a command:
        CommandManager commandManager = new CommandManager();
        commandManager.registerCommand("commandName", new MyCommand());

        // Additionally, you can also register default commands:
        commandManager.registerDefaultHelpCommand();
        commandManager.registerDefaultExitCommand();
        
        // Running the program in the console:
        CommandProcessor commandProcessor = new CommandProcessor(commandManager);
        commandProcessor.startLoop(System.in, System.out);
    }
}
```

`System.in` and `System.out` can be replaced with any type of
`InputStream`/`PrintStream`, meaning the processor can read and execute
commands even from the file, printing the output to another file.

Additionaly, if you don't want to use the `startLoop` method, you can 
use the `CommandProcessor.executeLine` method to create 
a custom way of command processing.

#### 3. Command syntax

A syntax for commands looks like this:
```
!commandName arg1 arg2 ...
```
All arguments are treated as strings. If you wish to include space inside
an argument, wrap it with double quotes:
```
!commandName "argument with spaces"
```
To include a double quote inside an argument, precede it with "\":
```
!commandName "argument with \"double quotes\""
```
