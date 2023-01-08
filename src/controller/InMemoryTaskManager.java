package controller;
import java.util.*;
import model.*;

public class InMemoryTaskManager implements TaskManager {
    protected int generatorId = 0;

    protected final Map<Integer, Task> taskRepo = new HashMap<>();
    protected final Map<Integer, Epic> epicRepo = new HashMap<>();
    protected final Map<Integer, SubTask> subtaskRepo = new HashMap<>();

    protected final HistoryManager historyManager = Managers.getDefaultHistory();

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public Task getTask(int id) {
        if (!taskRepo.containsKey(id)) {
            System.out.println("Не найдена задача с id = " + id);
            return null;
        }
        historyManager.add(taskRepo.get(id));
        return taskRepo.get(id);
    }

    @Override
    public Epic getEpic(int id) {
        if (!epicRepo.containsKey(id)) {
            System.out.println("Не найден эпик с id = " + id);
            return null;
        }
        historyManager.add(epicRepo.get(id));
        return epicRepo.get(id);
    }

    @Override
    public SubTask getSubtask(int id) {
        if (!subtaskRepo.containsKey(id)) {
            System.out.println("Не найдена подзадача с id = " + id);
            return null;
        }
        historyManager.add(subtaskRepo.get(id));
        return subtaskRepo.get(id);
    }

    @Override
    public int addNewTask(Task task) {
        task.setId(++generatorId);
        task.setStatus(Status.NEW);
        taskRepo.put(task.getId(), task);
        return task.getId();
    }

    @Override
    public int addNewEpic(Epic epic) {
        epic.setId(++generatorId);
        epic.setStatus(Status.NEW);
        epicRepo.put(epic.getId(), epic);
        return epic.getId();
    }

    @Override
    public int addNewSubtask(SubTask subtask) {
        Epic epic = epicRepo.get(subtask.getEpicId());
        if (epic == null) {
            System.out.println("Эпик не найден");
            return 0;
        }
        subtask.setId(++generatorId);
        subtask.setStatus(Status.NEW);
        epic.addSubtaskId(subtask);
        subtaskRepo.put(subtask.getId(), subtask);
        updateEpicStatus(epic);
        return subtask.getId();
    }

    @Override
    public void deleteTask(int id) {
        if (!taskRepo.containsKey(id)) {
            System.out.println("Удаление задачи невозможно: id не найден");
            return;
        }
        taskRepo.remove(id);
        historyManager.remove(id);
    }

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
        historyManager.remove(id);
        epicRepo.remove(id);
    }

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
        historyManager.remove(id);
    }

    @Override
    public void deleteAllTasks() {
        if (taskRepo.isEmpty()) {
            System.out.println("Список задач пуст");
            return;
        }
        taskRepo.clear();
    }

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

    @Override
    public ArrayList<Task> getTaskList() {
        return new ArrayList<>(taskRepo.values());
    }

    @Override
    public ArrayList<Epic> getEpicList() {
        return new ArrayList<>(epicRepo.values());
    }

    @Override
    public ArrayList<SubTask> getSubtaskList() {
        return new ArrayList<>(subtaskRepo.values());
    }

    @Override
    public List<Integer> getEpicSubtasks(int epicId) {
        return epicRepo.get(epicId).getSubtasksIds();
    }

    @Override
    public void updateTask(Task task) {
        if (!taskRepo.containsKey(task.getId())) {
            System.out.println("Обновление задачи невозможно: id не найден");
            return;
        }
        taskRepo.put(task.getId(), task);
    }

    @Override
    public void updateEpic(Epic epic) {
        if (!epicRepo.containsKey(epic.getId())) {
            System.out.println("Обновление эпика невозможно: id не найден");
            return;
        }
        epicRepo.put(epic.getId(), epic);
    }

    @Override
    public void updateSubtask(SubTask subtask) {
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
        if ((statusChecker.contains(Status.NEW) && statusChecker.size() == 1) || statusChecker.isEmpty()) {
            epic.setStatus(Status.NEW);
        } else if (statusChecker.contains(Status.DONE) && statusChecker.size() == 1) {
            epic.setStatus(Status.DONE);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }
}
