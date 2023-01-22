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
        final Node node = nodeStorage.get(id);
        removeNode(node);
        nodeStorage.remove(id);
    }

    /**
     * Метод, принимающий объект Node — узел связного списка и вырезает его
     */
    private void removeNode(Node node) {
        if (node != null) {
            final Node nextNode = node.next;
            final Node prevNode = node.prev;

            if (nextNode != null && prevNode != null) {
                prevNode.next = node.next;
                nextNode.prev = node.prev;
            } else if (nextNode == null && prevNode != null) {
                tail = node.prev;
                node.prev.next = null;
            } else if (nextNode != null) {
                head = node.next;
                node.next.prev = null;
            } else {
                head = null;
                tail = null;
            }
            nodeStorage.remove(node.task.getId());
        }
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
