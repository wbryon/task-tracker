package controller;
import model.Task;
import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {

    Map<Integer, Integer> nodeStorage = new HashMap<>();
    private final InMemoryHistoryManager.CustomLinkedList list = new CustomLinkedList();

    @Override
    public void add(Task task) {
        int count = 0;
        if (task == null) {
            return;
        }
//        if (nodeStorage.containsKey(task.getId()))
//            list.removeNode(nodeStorage.get(task.getId()));
        list.linkLast(task);
        nodeStorage.put(task.getId(), ++count);
    }

    @Override
    public List<Task> getHistory() {
        return new CustomLinkedList().getTasks(list);
    }

    @Override
    public void remove(int id) {}

    public static class CustomLinkedList {
        private int size = 0;
        private Node head;
        private Node tail;

        public CustomLinkedList() {
            head = null;
            tail = null;
        }

        private boolean isEmpty() {
            return head == null;
        }
        public void linkFirst(Task task) {
            Node newNode = new Node(task);
            if (isEmpty())
                tail = newNode;
            else
                head.prev = newNode;
            newNode.next = head;
            head = newNode;
            size++;
        }
        public void linkLast(Task task) {
            Node newNode = new Node(task);
            if (isEmpty())
                head = newNode;
            else
                tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
            size++;
        }

        public void removeNode(Node node) {
            if (node.equals(head))
                head.next.prev = null;
            else if (node.equals(tail))
                tail.prev.next = null;
        }

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

        public void printList() {
            Node node = head;
            while (node != null) {
                System.out.println(node.task);
                node = node.next;
            }
        }

    }
}

class Node {
    public Task task;
    public Node next;
    public Node prev;

    public Node(Task task) {
        this.task = task;
    }
}
