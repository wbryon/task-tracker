package model;

import java.util.Set;

public enum Status {
    NEW, IN_PROGRESS, DONE;

    /**
     * метод для сравнения состояния статуса
     */
    public boolean contains(Set<Status> statusChecker) {
        return true;
    }
}
