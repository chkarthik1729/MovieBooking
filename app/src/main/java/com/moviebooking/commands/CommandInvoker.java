package com.moviebooking.commands;

import com.moviebooking.exceptions.NoSuchCommandException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandInvoker {
    private static final Map<String, ICommand> commandMap = new HashMap<>();

    public void registerCommand(String commandName, ICommand command) {
        commandMap.put(commandName, command);
    }

    private ICommand get(String commandName) {
        return commandMap.get(commandName);
    }

    public void executeCommand(String commandName, List<String> tokens) throws NoSuchCommandException {
        ICommand command = get(commandName);
        if (command == null) {
            throw new NoSuchCommandException();
        }
        command.execute(tokens);
    }
}
