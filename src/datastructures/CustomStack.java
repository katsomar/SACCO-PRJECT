package datastructures;

public class CustomStack<T> {
    private Node<T> top; // The top of the stack
    private int size;

    private static class Node<T> {
        T data;
        Node<T> next;

        Node(T data) {
            this.data = data;
            this.next = null;
        }
    }

    public CustomStack() {
        this.top = null;
        this.size = 0;
    }

    // Push an element onto the stack
    public void push(T data) {
        Node<T> newNode = new Node<>(data);
        newNode.next = top; // Point the new node to the current top
        top = newNode; // Update the top to the new node
        size++;
    }

    // Pop an element from the stack
    public T pop() {
        if (isEmpty()) {
            throw new IllegalStateException("Stack is empty");
        }
        T data = top.data; // Get the data from the top node
        top = top.next; // Move the top pointer to the next node
        size--;
        return data;
    }

    // Peek at the top element without removing it
    public T peek() {
        if (isEmpty()) {
            throw new IllegalStateException("Stack is empty");
        }
        return top.data;
    }

    // Check if the stack is empty
    public boolean isEmpty() {
        return top == null;
    }

    // Get the size of the stack
    public int size() {
        return size;
    }
}
