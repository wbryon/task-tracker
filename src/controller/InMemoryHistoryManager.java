package controller;
import model.*;
import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    Map<Integer, Node> nodeStorage = new HashMap<>();
    Node head = null;
    Node tail = null;

    @Override
    public void add(Task task) {
        if (task == null)
            return;
        Node newNode = nodeStorage.get(task.getId());
        if (newNode != null)
            removeNode(newNode);
        linkLast(task);
    }

    @Override
    public void remove(int id) {
        Node node = nodeStorage.get(id);
        if (node.task instanceof Epic) {
            Epic epic = (Epic)node.task;
            for (int i = 0; i < epic.getSubtasksIds().size(); i++) {
                Integer subtaskId = (((Epic) nodeStorage.get(id).task).getSubtasksIds().get(i));
                removeNode(new Node(nodeStorage.get(subtaskId).task));
            }
        }
        removeNode(node);
        nodeStorage.remove(id);
    }

    /**
     * Метод, принимающий объект Node — узел связного списка и вырезает его
     */
    private void removeNode(Node node) {
        if (node.task.getId() == head.task.getId()) {
            if (node.task.getId() == tail.task.getId()) {
                head = null;
                tail = null;
                return;
            }
            head = head.next;
            return;
        }
        if (node.task.getId() == tail.task.getId()) {
            tail = tail.prev;
            tail.next = null;
            return;
        }
        Node curNode = head;
        while (node.task.getId() != curNode.task.getId()) {
            curNode = curNode.next;
        }
        Node prevNode = curNode.prev;
        Node nextNode = curNode.next;
        prevNode.next = nextNode;
        nextNode.prev = prevNode;
    }

    /**
     * Метод связного списка, добавляющий задачу в конец этого списка
     */
    private void linkLast(Task task) {
        Node newNode = new Node(task);
        if (head == null)
            head = newNode;
        else
            tail.next = newNode;
        newNode.prev = tail;
        tail = newNode;
        nodeStorage.put(task.getId(), newNode);
    }

    /**
     * Метод связного списка CustomLinkedList, собирающий все задачи из него в обычный ArrayList
     */
    private List<Task> getTasks() {
        List<Task> taskList = new ArrayList<>();
        Node currentNode = head;
        if (currentNode == null)
            return null;
        while (currentNode != null) {
            taskList.add(currentNode.task);
            currentNode = currentNode.next;
        }
        return taskList;
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    /**
     * Класс Node для узла списка CustomLinkedList
     */
    private static class Node {
        public Task task;
        public  Node next;
        public  Node prev;

        public Node(Task task) {
            this.task = task;
        }
    }
}
