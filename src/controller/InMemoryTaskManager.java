package controller;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import model.*;

public class InMemoryTaskManager implements TaskManager {
    protected int generatorId = 0;
    protected final Map<Integer, Task> taskRepo = new HashMap<>();
    protected final Map<Integer, Epic> epicRepo = new HashMap<>();
    protected final Map<Integer, SubTask> subtaskRepo = new HashMap<>();
    protected final Map<LocalDateTime, Task> mapOfPrioritizedTasks = new TreeMap<>();
    protected final HistoryManager historyManager = Managers.getDefaultHistory();
    protected Comparator<Task> compareTasksByStartTime = (task1, task2) -> {
        if (task1.getStartTime().isBefore(task2.getStartTime())) return -1;
        if (task1.equals(task2)) return 0;
        return 1;
    };
    protected final Set<Task> allTasks = new TreeSet<>(compareTasksByStartTime);

    @Override
    public int createTask(Task task) {
        task.setId(++generatorId);
        task.setStatus(Status.NEW);
        taskRepo.put(task.getId(), task);
        addTaskToPrioritizationList(task);
        allTasks.add(task);
        return task.getId();
    }

    @Override
    public int createEpic(Epic epic) {
        epic.setId(++generatorId);
        epic.setStatus(Status.NEW);
        setEpicStartTime(epic);
        setEpicEndTime(epic);
        setEpicDuration(epic);
        epicRepo.put(epic.getId(), epic);
        allTasks.add(epic);
        return epic.getId();
    }

    @Override
    public int createSubTask(SubTask subtask) {
        Epic epic = epicRepo.get(subtask.getEpicId());
        if (epic == null)
            return 0;
        subtask.setId(++generatorId);
        subtaskRepo.put(subtask.getId(), subtask);
        subtask.setStatus(Status.NEW);
        addTaskToPrioritizationList(subtask);
        epic.addSubtaskId(subtask);
        updateEpicStatus(epic);
        updateEpicDuration(epic);
        allTasks.add(subtask);
        return subtask.getId();
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public Task getTaskWithoutHistory(int id) {
        if (!taskRepo.containsKey(id))
            return null;
        return taskRepo.get(id);
    }

    @Override
    public Task getTask(int id) {
        if (!taskRepo.containsKey(id))
            return null;
        historyManager.add(taskRepo.get(id));
        return taskRepo.get(id);
    }

    @Override
    public Epic getEpicWithoutHistory(int id) {
        if (!epicRepo.containsKey(id))
            return null;
        return epicRepo.get(id);
    }

    @Override
    public Epic getEpic(int id) {
        if (!epicRepo.containsKey(id))
            return null;
        historyManager.add(epicRepo.get(id));
        return epicRepo.get(id);
    }

    @Override
    public SubTask getSubTaskWithoutHistory(int id) {
        if (!subtaskRepo.containsKey(id))
            return null;
        return subtaskRepo.get(id);
    }

    @Override
    public SubTask getSubTask(int id) {
        if (!subtaskRepo.containsKey(id))
            return null;
        historyManager.add(subtaskRepo.get(id));
        return subtaskRepo.get(id);
    }

    @Override
    public void deleteTask(int id) {
        if (!taskRepo.containsKey(id))
            return;
        taskRepo.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void deleteEpic(int id) {
        Epic epic = epicRepo.get(id);
        if (epic != null) {
            for (Integer subTasksId : epic.getSubtasksIds()) {
                if (historyManager.getHistory().contains(subtaskRepo.get(subTasksId)))
                    historyManager.remove(subTasksId);
                subtaskRepo.remove(subTasksId);
            }
            epicRepo.remove(id);
            historyManager.remove(id);
        }
    }

    @Override
    public void deleteSubTask(int id) {
        if (subtaskRepo.containsKey(id)) {
            subtaskRepo.remove(id);
            historyManager.remove(id);
        }
    }

    @Override
    public void deleteAllTasks() {
        if (taskRepo.isEmpty())
            return;
        for (Integer id : taskRepo.keySet())
            historyManager.remove(id);
        for (Task task : taskRepo.values())
            mapOfPrioritizedTasks.remove(task.getStartTime());
        taskRepo.clear();
    }

    @Override
    public void deleteAllEpics() {
        if (epicRepo.isEmpty())
            return;
        for (Integer id : epicRepo.keySet())
            historyManager.remove(id);
        for (Epic epic : epicRepo.values()) {
            mapOfPrioritizedTasks.remove(epic.getStartTime());
        }
        epicRepo.clear();
        subtaskRepo.clear();
    }

    @Override
    public void deleteAllSubTasks() {
        if (subtaskRepo.isEmpty())
            return;
        for (Integer id : subtaskRepo.keySet())
            historyManager.remove(id);
        subtaskRepo.clear();
        for (int epicId : epicRepo.keySet())
            epicRepo.get(epicId).getSubtasksIds().clear();
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
    public ArrayList<SubTask> getSubTaskList() {
        return new ArrayList<>(subtaskRepo.values());
    }

    @Override
    public List<Integer> getEpicSubTasks(int epicId) {
        return epicRepo.get(epicId).getSubtasksIds();
    }

    @Override
    public Task updateTask(Task task) {
        Task result = null;
        if (taskRepo.containsKey(task.getId())) {
            final Task updatableTask = taskRepo.get(task.getId());
            if (checkTasksForIntersectionsByTime(updatableTask)) {
                updatableTask.setTaskName(task.getTaskName());
                updatableTask.setTaskDescription(task.getTaskDescription());
                updatableTask.setStatus(task.getStatus());
                updatableTask.setStartTime(task.getStartTime());
                updatableTask.setDuration(task.getDuration());
                taskRepo.put(updatableTask.getId(), updatableTask);
            }
            result = updatableTask;
        }
        return result;
    }

    @Override
    public SubTask updateSubTask(SubTask subtask) {
        SubTask result = null;
        Epic epic = getEpicWithoutHistory(subtask.getEpicId());
        if (epic != null) {
            final SubTask updatableSubtask = subtaskRepo.get(subtask.getId());
            if (checkTasksForIntersectionsByTime(updatableSubtask)) {
                updatableSubtask.setTaskName(subtask.getTaskName());
                updatableSubtask.setTaskDescription(subtask.getTaskDescription());
                updatableSubtask.setStatus(subtask.getStatus());
                updatableSubtask.setStartTime(subtask.getStartTime());
                updatableSubtask.setDuration(subtask.getDuration());
                subtaskRepo.put(subtask.getId(), updatableSubtask);
            }
            updateEpicStatus(epic);
            result = updatableSubtask;
        }
        return result;
    }

    @Override
    public Epic updateEpic(Epic epic) {
        if (!epicRepo.containsKey(epic.getId()))
            return null;
        final Epic updatableEpic = epicRepo.get(epic.getId());
        updatableEpic.setTaskName(epic.getTaskName());
        updatableEpic.setTaskDescription(epic.getTaskDescription());
        updateEpicStatus(epic);
        epicRepo.put(updatableEpic.getId(), updatableEpic);
        return epic;
    }

    /**
     * Метод для обновления статуса эпика
     */
    public void updateEpicStatus(Epic epic) {
        Set<Status> statusChecker = new HashSet<>();
        for (Integer subtaskId : epic.getSubtasksIds())
            statusChecker.add(subtaskRepo.get(subtaskId).getStatus());
        if ((statusChecker.contains(Status.NEW) && statusChecker.size() == 1) || statusChecker.isEmpty())
            epic.setStatus(Status.NEW);
        else if (statusChecker.contains(Status.DONE) && statusChecker.size() == 1)
            epic.setStatus(Status.DONE);
        else
            epic.setStatus(Status.IN_PROGRESS);
    }

    /**
     * Обновление временных меток для эпиков по подзадачам
     */
    protected Duration updateEpicDuration(Epic epic) {
        LocalDateTime epicStartTime = epic.getStartTime();
        LocalDateTime epicEndTime = epic.getEndTime();
        Duration durationEpic = Duration.ZERO;

        for (Integer subTaskId : epic.getSubtasksIds()) {
            LocalDateTime subtaskStartTime = getSubTaskWithoutHistory(subTaskId).getStartTime();
            LocalDateTime subtaskEndTime = getSubTaskWithoutHistory(subTaskId).getEndTime();
            if (subtaskStartTime.isBefore(epicStartTime))
                epic.setStartTime(subtaskStartTime);
            if (subtaskEndTime.isAfter(epicEndTime))
                epic.setEndTime(subtaskEndTime);
            durationEpic = durationEpic.plus(getSubTaskWithoutHistory(subTaskId).getDuration());
            epic.setDuration(durationEpic);
        }
        return epic.getDuration();
    }

    /**
     * Метод, вычисляющий начало эпика
     */
    protected LocalDateTime setEpicStartTime(Epic epic) {
        epic.setStartTime(LocalDateTime.MAX);
        if (!epic.getSubtasksIds().isEmpty()) {
            for (Integer subTaskId : epic.getSubtasksIds()) {
                SubTask subtask = getSubTaskWithoutHistory(subTaskId);
                LocalDateTime startSubtask = subtask.getStartTime();
                epic.setStartTime(startSubtask);
                if (startSubtask.isBefore(epic.getStartTime()))
                    epic.setStartTime(startSubtask);
            }
        }
        return epic.getStartTime();
    }

    /**
     * Метод, вычисляющий окончание эпика
     */
    protected LocalDateTime setEpicEndTime(Epic epic) {
        epic.setEndTime(LocalDateTime.MIN);
        if (!epic.getSubtasksIds().isEmpty()) {
            for (Integer subTaskId : epic.getSubtasksIds()) {
                SubTask subtask = getSubTaskWithoutHistory(subTaskId);
                LocalDateTime endSubtask = subtask.getEndTime();
                epic.setEndTime(endSubtask);
                if (endSubtask.isAfter(epic.getEndTime()))
                    epic.setEndTime(endSubtask);
            }
        }
        return epic.getEndTime();
    }

    /**
     * Метод, вычисляющий продолжительность всех подзадач эпика
     */
    protected Duration setEpicDuration(Epic epic) {
        Duration epicDuration = Duration.ZERO;
        for (Integer subTaskId : epic.getSubtasksIds()) {
            SubTask subTask = getSubTaskWithoutHistory(subTaskId);
            epicDuration.plus(subTask.getDuration());
        }
        epic.setDuration(epicDuration);
        return epic.getDuration();
    }

    /**
     * Метод, добавляющий задачу в список приоритетности выполнения задачи
     */
    private void addTaskToPrioritizationList(Task task) {
        if (!(task instanceof Epic)) {
            if (checkTasksForIntersectionsByTime(task))
                mapOfPrioritizedTasks.put(task.getStartTime(), task);
            else
                System.out.println("Измените время выполнения задачи - " + task.getId());
        }
    }

    /**
     * Метод, возвращающий задачи по началу их выполнения
     */
    @Override
    public List<Task> getPrioritizedTasks() {
        allTasks.addAll(mapOfPrioritizedTasks.values());
        return new ArrayList<>(mapOfPrioritizedTasks.values());
    }

    /**
     * Метод, проверяющий задачи на пересечение
     */
    private Boolean checkTasksForIntersectionsByTime(Task newTask) {
        if (mapOfPrioritizedTasks.isEmpty())
            return true;
        for (Task task : mapOfPrioritizedTasks.values()) {
            if (task.getId() == newTask.getId())
                continue;
            if (!newTask.getEndTime().isAfter(task.getStartTime()))
                continue;
            if (!newTask.getStartTime().isBefore(task.getEndTime()))
                continue;
            return false;
        }
        return true;
    }

    public int getGeneratorId() {
        return generatorId;
    }
}
