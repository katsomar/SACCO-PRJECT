package datastructures;

public class CustomLinkedList<T> {
    private Node<T> head;
    private Node<T> tail;
    private int size;

    private static class Node<T> {
        T data;
        Node<T> next;

        Node(T data) {
            this.data = data;
            this.next = null;
        }
    }

    public CustomLinkedList() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    // Add an element to the end of the list
    public void add(T data) {
        Node<T> newNode = new Node<>(data);
        if (head == null) {
            head = tail = newNode; // If the list is empty, both head and tail point to the new node
        } else {
            tail.next = newNode; // Link the new node to the current tail
            tail = newNode; // Update the tail to the new node
        }
        size++;
    }

    // Get an element at a specific index
    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index out of bounds");
        }
        Node<T> current = head;
        for (int i = 0; i < index; i++) {
            current = current.next; // Traverse the list to the desired index
        }
        return current.data;
    }

    // Remove the last element from the list
    public T removeLast() {
        if (size == 0) {
            throw new IllegalStateException("List is empty");
        }
        if (size == 1) {
            T data = head.data;
            head = tail = null; // If there's only one element, clear the list
            size--;
            return data;
        }
        Node<T> current = head;
        while (current.next != tail) {
            current = current.next; // Traverse to the second-to-last node
        }
        T data = tail.data;
        tail = current; // Update the tail to the second-to-last node
        tail.next = null;
        size--;
        return data;
    }

    // Remove an element by value
    public boolean remove(T data) {
        if (head == null) return false;
        if (head.data.equals(data)) {
            head = head.next;
            size--;
            if (head == null) tail = null;
            return true;
        }
        Node<T> current = head;
        while (current.next != null) {
            if (current.next.data.equals(data)) {
                current.next = current.next.next;
                if (current.next == null) tail = current;
                size--;
                return true;
            }
            current = current.next;
        }
        return false;
    }

    // Get the size of the list
    public int size() {
        return size;
    }
}
