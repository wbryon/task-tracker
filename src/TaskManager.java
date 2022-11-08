import java.util.*;

/**
 * Класс, управляющий всеми задачами
 */
public class TaskManager {
    private int generatorId = 0;

    protected final Map<Integer, Task> taskRepo = new HashMap<>();
    protected final Map<Integer, Epic> epicRepo = new HashMap<>();
    protected final Map<Integer, Subtask> subtaskRepo = new HashMap<>();

    /**
     * Сеттер для id-генератора
     */
    public void setGeneratorId(int generatorId) {
            this.generatorId = generatorId;
    }

    /**
     * Геттер для id-генератора
     */
    public int getGeneratorId() {
        return generatorId;
    }

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
        epic.addSubtask(subtask);
        subtask.setId(++generatorId);
        subtask.setStatus(Status.NEW);
        updateEpicStatus(epic);
        subtaskRepo.put(subtask.getId(), subtask);
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
            for (Subtask subtask : epicRepo.get(epicId).subtaskList) {
                epicRepo.get(epicId).subtaskList.remove(subtask);
            }
        }
        epicRepo.clear();
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
    public List<Subtask> getEpicSubtasks(int epicId) {
        return epicRepo.get(epicId).subtaskList;
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
    static void updateEpicStatus(Epic epic) {
        Set<Status> getStatus = new HashSet();
        for (Subtask subtask : epic.subtaskList) {
            getStatus.add(subtask.status);
        }
        if ((getStatus.contains(Status.NEW) && getStatus.size() == 1) || getStatus.isEmpty()) {
            epic.setStatus(Status.NEW);
        } else if (getStatus.contains(Status.DONE) && getStatus.size() == 1) {
            epic.setStatus(Status.DONE);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }
}
