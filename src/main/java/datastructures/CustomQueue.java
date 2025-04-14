package datastructures;

public class CustomQueue<T> {
    private Node<T> front; // The front of the queue
    private Node<T> rear;  // The rear of the queue
    private int size;

    private static class Node<T> {
        T data;
        Node<T> next;

        Node(T data) {
            this.data = data;
            this.next = null;
        }
    }

    public CustomQueue() {
        this.front = null;
        this.rear = null;
        this.size = 0;
    }

    // Enqueue an element (add to the rear)
    public void enqueue(T data) {
        Node<T> newNode = new Node<>(data);
        if (isEmpty()) {
            front = rear = newNode; // If the queue is empty, both front and rear point to the new node
        } else {
            rear.next = newNode; // Link the new node to the current rear
            rear = newNode; // Update the rear to the new node
        }
        size++;
    }

    // Dequeue an element (remove from the front)
    public T dequeue() {
        if (isEmpty()) {
            throw new IllegalStateException("Queue is empty");
        }
        T data = front.data; // Get the data from the front node
        front = front.next; // Move the front pointer to the next node
        if (front == null) {
            rear = null; // If the queue becomes empty, set rear to null as well
        }
        size--;
        return data;
    }

    // Peek at the front element without removing it
    public T peek() {
        if (isEmpty()) {
            throw new IllegalStateException("Queue is empty");
        }
        return front.data;
    }

    // Check if the queue is empty
    public boolean isEmpty() {
        return front == null;
    }

    // Get the size of the queue
    public int size() {
        return size;
    }
}
