package datastructures;

public class CustomHashMap<K, V> {
    private Bucket<K, V>[] buckets;
    private int capacity = 16;
    private int size;
    
    private static class Bucket<K, V> {
        Node<K, V> head;
    }
    
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
        Node<K, V> node = buckets[index].head;
        while(node != null) {
            if(node.key.equals(key)) {
                node.value = value;
                return;
            }
            node = node.next;
        }
        Node<K, V> newNode = new Node<>(key, value);
        newNode.next = buckets[index].head;
        buckets[index].head = newNode;
        size++;
    }
    
    public V get(K key) {
        int index = getBucketIndex(key);
        Node<K, V> node = buckets[index].head;
        while(node != null) {
            if(node.key.equals(key)) {
                return node.value;
            }
            node = node.next;
        }
        return null;
    }
    
    public boolean containsKey(K key) {
        return get(key) != null;
    }
    
    public int size() {
        return size;
    }
}
