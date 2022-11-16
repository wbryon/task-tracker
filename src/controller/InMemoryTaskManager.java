package controller;

import java.util.*;
import model.*;

/**
 * Класс, управляющий всеми задачами
 */
public class InMemoryTaskManager implements TaskManager {
    private int generatorId = 0;

    protected final Map<Integer, Task> taskRepo = new HashMap<>();
    protected final Map<Integer, Epic> epicRepo = new HashMap<>();
    protected final Map<Integer, Subtask> subtaskRepo = new HashMap<>();
    private final List<Task> taskViewHistory = new ArrayList<>();

    /**
     * Реализация метода, возвращающего последние 10 просмотренных задач
     */
    @Override
    public List<Task> getHistory() {
        for (int i = 0; i < taskViewHistory.size(); i++) {
            System.out.println(i + "-> id: " + taskViewHistory.get(i).getId() + "; " +  taskViewHistory.get(i));
        }
        return taskViewHistory;
    }
    /**
     * Метод, проверяющий количество просмотренных задач в списке (не > 10)
     */

    private boolean checkHistoryList() {
        int historyCount = 10;
        if (taskViewHistory.size() == historyCount) {
            for (int i = 0; i < (taskViewHistory.size() - 1); i++) {
                taskViewHistory.set(i, taskViewHistory.get(i + 1));
            }
            taskViewHistory.remove(taskViewHistory.get(taskViewHistory.size() - 1));
            return true;
        }
        return false;
    }

    /**
     * Реализация метода для получения задачи по идентификатору
     */
    @Override
    public Task getTask(int id) {
        if (!taskRepo.containsKey(id)) {
            System.out.println("Задача с таким id не найдена");
            return null;
        }
        if (checkHistoryList()) {
            taskViewHistory.add(taskRepo.get(id));
        } else {
            taskViewHistory.add(taskRepo.get(id));
        }
        return taskRepo.get(id);
    }

    /**
     * Реализация метода для получения эпика по идентификатору
     */
    @Override
    public Epic getEpic(int id) {
        if (!epicRepo.containsKey(id)) {
            System.out.println("Эпик с таким id не найден");
            return null;
        }
        if (checkHistoryList()) {
            taskViewHistory.add(epicRepo.get(id));
        } else {
            taskViewHistory.add(epicRepo.get(id));
        }
        return epicRepo.get(id);
    }

    /**
     * Реализация метода для получения подзадачи по идентификатору
     */
    @Override
    public Subtask getSubtask(int id) {
        if (!subtaskRepo.containsKey(id)) {
            System.out.println("Подзадача с таким id не найдена");
            return null;
        }
        if (checkHistoryList()) {
            taskViewHistory.add(subtaskRepo.get(id));
        } else {
            taskViewHistory.add(subtaskRepo.get(id));
        }
        return subtaskRepo.get(id);
    }

    /**
     * Реализация метода для создания новой задачи
     */
    @Override
    public int addNewTask(Task task) {
        task.setId(++generatorId);
        task.setStatus(Status.NEW);
        taskRepo.put(task.getId(), task);
        return task.getId();
    }

    /**
     * Реализация метода для создания нового эпика
     */
    @Override
    public int addNewEpic(Epic epic) {
        epic.setId(++generatorId);
        epic.setStatus(Status.NEW);
        epicRepo.put(epic.getId(), epic);
        return epic.getId();
    }

    /**
     * Реализация метода для создания новой подзадачи
     */
    @Override
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
     * Реализация метода для удаления задачи по идентификатору
     */
    @Override
    public void deleteTask(int id) {
        if (!taskRepo.containsKey(id)) {
            System.out.println("Удаление задачи невозможно: id не найден");
            return;
        }
        taskRepo.remove(id);
    }

    /**
     * Реализация метода для удаления эпика по идентификатору
     */
    @Override
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
     * Реализация метода для удаления подзадачи по идентификатору
     */
    @Override
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
     * Реализация метода для удаления всех задач
     */
    @Override
    public void deleteAllTasks() {
        if (taskRepo.isEmpty()) {
            System.out.println("Список задач пуст");
            return;
        }
        taskRepo.clear();
    }

    /**
     * Реализация метода для удаления всех эпиков
     */
    @Override
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
     * Реализация метода для удаления всех подзадач
     */
    @Override
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
     * Реализация метода для получения списка всех задач
     */
    @Override
    public ArrayList<Task> getTasksList() {
        return new ArrayList<>(taskRepo.values());
    }

    /**
     * Реализация метода для получения списка всех эпиков
     */
    @Override
    public ArrayList<Epic> getEpicList() {
        return new ArrayList<>(epicRepo.values());
    }

    /**
     * Реализация метода для получения списка всех подзадач
     */
    @Override
    public ArrayList<Subtask> getSubtaskList() {
        return new ArrayList<>(subtaskRepo.values());
    }

    /**
     * Реализация метода для получения списка всех подзадач эпика
     */
    @Override
    public List<Integer> getEpicSubtasks(int epicId) {
        return epicRepo.get(epicId).getSubtasksIds();
    }

    /**
     * Реализация метода для обновления задачи. Новая версия задачи с верным идентификатором передаётся в виде параметра
     */
    @Override
    public void updateTask(Task task) {
        if (!taskRepo.containsKey(task.getId())) {
            System.out.println("Обновление задачи невозможно: id не найден");
            return;
        }
        taskRepo.put(task.getId(), task);
    }

    /**
     * Реализация метода для обновления эпика. Новая версия эпика с верным идентификатором передаётся в виде параметра
     */
    @Override
    public void updateEpic(Epic epic) {
        if (!epicRepo.containsKey(epic.getId())) {
            System.out.println("Обновление эпика невозможно: id не найден");
            return;
        }
        epicRepo.put(epic.getId(), epic);
    }

    /**
     * Реализация метода для обновления подзадачи. Новая версия подзадачи с верным идентификатором передаётся в виде параметра
     */
    @Override
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
