package com.example.bookcatalog.command;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CommandInvoker {
    
    private final List<Command> commandHistory;
    
    public CommandInvoker() {
        this.commandHistory = new ArrayList<>();
    }
    
    public void executeCommand(Command command) {
        command.execute();
        commandHistory.add(command);
    }
    
    public void undoLastCommand() {
        if (!commandHistory.isEmpty()) {
            Command lastCommand = commandHistory.get(commandHistory.size() - 1);
            lastCommand.undo();
            commandHistory.remove(commandHistory.size() - 1);
        }
    }
    
    public List<String> getCommandHistory() {
        List<String> history = new ArrayList<>();
        for (Command command : commandHistory) {
            history.add(command.getDescription());
        }
        return history;
    }
    
    public void clearHistory() {
        commandHistory.clear();
    }
    
    public int getHistorySize() {
        return commandHistory.size();
    }
    
    public boolean hasCommands() {
        return !commandHistory.isEmpty();
    }
}

