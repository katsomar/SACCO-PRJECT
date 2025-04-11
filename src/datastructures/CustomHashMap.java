package datastructures;

public class CustomHashMap<K, V> {
    private Bucket<K, V>[] buckets;
    private int capacity = 16;
    private int size = 0;

    private static class Bucket<K, V> {
        private Node<K, V> head;

        private static class Node<K, V> {
            K key;
            V value;
            Node<K, V> next;

            Node(K key, V value) {
                this.key = key;
                this.value = value;
                this.next = null;
            }
        }

        public void put(K key, V value) {
            Node<K, V> current = head;
            while (current != null) {
                if (current.key.equals(key)) {
                    current.value = value; // Update the value if the key exists
                    return;
                }
                current = current.next;
            }
            Node<K, V> newNode = new Node<>(key, value);
            newNode.next = head; // Insert the new node at the head of the bucket
            head = newNode;
        }

        public V get(K key) {
            Node<K, V> current = head;
            while (current != null) {
                if (current.key.equals(key)) {
                    return current.value;
                }
                current = current.next;
            }
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public CustomHashMap() {
        buckets = new Bucket[capacity];
        for (int i = 0; i < capacity; i++) {
            buckets[i] = new Bucket<>();
        }
    }

    private int getBucketIndex(K key) {
        return Math.abs(key.hashCode() % capacity);
    }

    public void put(K key, V value) {
        int index = getBucketIndex(key);
        buckets[index].put(key, value);
        size++;
    }

    public V get(K key) {
        int index = getBucketIndex(key);
        return buckets[index].get(key);
    }

    public int size() {
        return size;
    }
}
