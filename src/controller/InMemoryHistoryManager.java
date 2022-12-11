package controller;
import model.*;
import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    Map<Integer, Node> nodeStorage = new HashMap<>();
    private final InMemoryHistoryManager.CustomLinkedList list = new CustomLinkedList();

    @Override
    public void add(Task task) {
        if (task == null)
            return;
        Node newNode = new Node(task);
        if (nodeStorage.containsKey(newNode.task.getId())) {
            removeNode(newNode);
        }
        list.linkLast(task);
        nodeStorage.put(task.getId(), newNode);
    }

    @Override
    public void remove(int id) {
        Node node = new Node(nodeStorage.get(id).task);
        if (nodeStorage.get(id).task instanceof Epic) {
            Epic epic = (Epic)nodeStorage.get(id).task;
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
    public void removeNode(Node node) {
        if (node.task.getId() == list.head.task.getId()) {
            if (node.task.getId() == list.tail.task.getId()) {
                list.head = null;
                list.tail = null;
                return;
            }
            list.head = list.head.next;
            return;
        }
        if (node.task.getId() == list.tail.task.getId()) {
            list.tail = list.tail.prev;
            list.tail.next = null;
            return;
        }
        Node curNode = list.head;
        while (node.task.getId() != curNode.task.getId()) {
            curNode = curNode.next;
        }
        Node prevNode = curNode.prev;
        Node nextNode = curNode.next;
        prevNode.next = nextNode;
        nextNode.prev = prevNode;
    }

    @Override
    public List<Task> getHistory() {
        return new CustomLinkedList().getTasks(list);
    }

    public static class CustomLinkedList {
        private Node head;
        private Node tail;

        public CustomLinkedList() {
            head = null;
            tail = null;
        }

        /**
         * Метод связного списка CustomLinkedList, добавляющий задачу в конец этого списка
         */
        public void linkLast(Task task) {
            Node newNode = new Node(task);
            if (head == null)
                head = newNode;
            else
                tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        }

        /**
         * Метод связного списка CustomLinkedList, собирающий все задачи из него в обычный ArrayList
         */
        public List<Task> getTasks(CustomLinkedList list) {
            List<Task> taskList = new ArrayList<>();
            Node currentNode = list.head;
            if (currentNode == null)
                return null;
            while (currentNode != null) {
                taskList.add(currentNode.task);
                currentNode = currentNode.next;
            }
            for (Task task : taskList) {
                System.out.println(task);
            }
            return taskList;
        }
    }
}

/**
 * Класс Node для узла списка CustomLinkedList
 */
class Node {
    public Task task;
    public Node next;
    public Node prev;

    public Node(Task task) {
        this.task = task;
    }
}
