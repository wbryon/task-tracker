package controller;

import java.util.*;
import model.*;

/**
 * Класс, управляющий всеми задачами
 */
public class TaskManager {
    private int generatorId = 0;

    protected final Map<Integer, Task> taskRepo = new HashMap<>();
    protected final Map<Integer, Epic> epicRepo = new HashMap<>();
    protected final Map<Integer, Subtask> subtaskRepo = new HashMap<>();

    /**
     * Метод для получения задачи по идентификатору
     */
    public Task getTask(int id) {
        if (!taskRepo.containsKey(id)) {
            System.out.println("Задача с таким id не найдена");
            return null;
        }
        return taskRepo.get(id);
    }

    /**
     * Метод для получения эпика по идентификатору
     */
    public Epic getEpic(int id) {
        if (!epicRepo.containsKey(id)) {
            System.out.println("Эпик с таким id не найден");
            return null;
        }
        return epicRepo.get(id);
    }

    /**
     * Метод для получения подзадачи по идентификатору
     */
    public Subtask getSubtask(int id) {
        if (!subtaskRepo.containsKey(id)) {
            System.out.println("Подзадача с таким id не найдена");
            return null;
        }
        return subtaskRepo.get(id);
    }

    /**
     * Метод для создания новой задачи
     */
    public int addNewTask(Task task) {
        task.setId(++generatorId);
        task.setStatus(Status.NEW);
        taskRepo.put(task.getId(), task);
        return task.getId();
    }

    /**
     * Метод для создания нового эпика
     */
    public int addNewEpic(Epic epic) {
        epic.setId(++generatorId);
        epic.setStatus(Status.NEW);
        epicRepo.put(epic.getId(), epic);
        return epic.getId();
    }

    /**
     * Метод для создания новой подзадачи
     */
    public void addNewSubtask(Subtask subtask) {
        Epic epic = getEpic(subtask.getEpicId());
        if (epic == null) {
            System.out.println("Эпик не найден");
            return;
        }
        subtask.setId(++generatorId);
        subtask.setStatus(Status.NEW);
        epic.addSubtaskId(subtask);
        subtaskRepo.put(subtask.getId(), subtask);
        updateEpicStatus(epic);
    }

    /**
     * Метод для удаления задачи по идентификатору
     */
    public void deleteTask(int id) {
        if (!taskRepo.containsKey(id)) {
            System.out.println("Удаление задачи невозможно: id не найден");
            return;
        }
        taskRepo.remove(id);
    }

    /**
     * Метод для удаления эпика по идентификатору
     */
    public void deleteEpic(int id) {
        if (!epicRepo.containsKey(id)) {
            System.out.println("Удаление эпика невозможно: id не найден");
            return;
        }
        for (Integer subtaskId : subtaskRepo.keySet()) {
            if (subtaskRepo.get(subtaskId).getEpicId() == id) {
                subtaskRepo.remove(getSubtask(subtaskId));
            }
        }
        epicRepo.remove(id);
    }

    /**
     * Метод для удаления подзадачи по идентификатору
     */
    public void deleteSubtask(int id) {
        if (!subtaskRepo.containsKey(id)) {
            System.out.println("Удаление подзадачи невозможно: id не найден");
            return;
        }
        for (int epicId : epicRepo.keySet()) {
            if (epicRepo.get(epicId).getSubtasksIds().contains(id)) {
                epicRepo.get(epicId).getSubtasksIds().remove(id);
                break;
            }
        }
        subtaskRepo.remove(id);
    }

    /**
     * Метод для удаления всех задач
     */
    public void deleteAllTasks() {
        if (taskRepo.isEmpty()) {
            System.out.println("Список задач пуст");
            return;
        }
        taskRepo.clear();
    }

    /**
     * Метод для удаления всех эпиков
     */
    public void deleteAllEpics() {
        if (epicRepo.isEmpty()) {
            System.out.println("Список эпиков пуст");
            return;
        }
        for (int epicId : epicRepo.keySet()) {
            for (Integer subtaskId : epicRepo.get(epicId).getSubtasksIds()) {
                epicRepo.get(epicId).getSubtasksIds().remove(subtaskId);
            }
        }
        epicRepo.clear();
        subtaskRepo.clear();
        System.out.println("Все эпики удалены");
    }

    /**
     * Метод для удаления всех подзадач
     */
    public void deleteAllSubtasks() {
        if (subtaskRepo.isEmpty()) {
            System.out.println("Список подзадач пуст");
            return;
        }
        subtaskRepo.clear();
        for (int epicId : epicRepo.keySet()) {
            epicRepo.get(epicId).getSubtasksIds().clear();
        }
        System.out.println("Все подзадачи удалены");
    }

    /**
     * Метод для получения списка всех задач
     */
    public ArrayList<Task> getTasksList() {
        return new ArrayList<>(taskRepo.values());
    }

    /**
     * Метод для получения списка всех эпиков
     */
    public ArrayList<Epic> getEpicList() {
        return new ArrayList<>(epicRepo.values());
    }

    /**
     * Метод для получения списка всех подзадач
     */
    public ArrayList<Subtask> getSubtaskList() {
        return new ArrayList<>(subtaskRepo.values());
    }

    /**
     * Метод для получения списка всех подзадач эпика
     */
    public List<Integer> getEpicSubtasks(int epicId) {
        return epicRepo.get(epicId).getSubtasksIds();
    }

    /**
     * Метод для обновления задачи. Новая версия задачи с верным идентификатором передаётся в виде параметра
     */
    public void updateTask(Task task) {
        if (!taskRepo.containsKey(task.getId())) {
            System.out.println("Обновление задачи невозможно: id не найден");
            return;
        }
        taskRepo.put(task.getId(), task);
    }

    /**
     * Метод для обновления эпика. Новая версия эпика с верным идентификатором передаётся в виде параметра
     */
    public void updateEpic(Epic epic) {
        if (!epicRepo.containsKey(epic.getId())) {
            System.out.println("Обновление эпика невозможно: id не найден");
            return;
        }
        epicRepo.put(epic.getId(), epic);
    }

    /**
     * Метод для обновления подзадачи. Новая версия подзадачи с верным идентификатором передаётся в виде параметра
     */
    public void updateSubtask(Subtask subtask) {
        Epic epic = getEpic(subtask.getEpicId());
        if (epic == null) {
            System.out.println("Эпик не найден");
            return;
        }
        subtaskRepo.put(subtask.getId(), subtask);
        updateEpicStatus(epic);
    }

    /**
     * Метод для обновления статуса эпика
     */
    public void updateEpicStatus(Epic epic) {
        Set<Status> statusChecker = new HashSet();
        for (Integer subtaskId : epic.getSubtasksIds()) {
            statusChecker.add(subtaskRepo.get(subtaskId).getStatus());
        }
        if ((Status.NEW.contains(statusChecker) && statusChecker.size() == 1) || statusChecker.isEmpty()) {
            epic.setStatus(Status.NEW);
        } else if (statusChecker.contains(Status.DONE) && statusChecker.size() == 1) {
            epic.setStatus(Status.DONE);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }
}
