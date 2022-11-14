package controller;

import model.Task;

public interface HistoryManager<T extends Task> {
    void add(T task);
}
