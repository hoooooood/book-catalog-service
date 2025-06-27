package com.example.bookcatalog.command;

public interface Command {
    void execute();
    void undo();
    String getDescription();
}

