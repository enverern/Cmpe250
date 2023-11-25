import java.util.LinkedList;
import java.util.ArrayList;

class HashTable<K, V> {
    private static final int DEFAULT_CAPACITY = 10;
    private ArrayList<LinkedList<Entry<K, V>>> table;
    private int size;

    public HashTable() {
        this(DEFAULT_CAPACITY);
    }

    public HashTable(int capacity) {
        table = new ArrayList<LinkedList<Entry<K, V>>>(capacity);
        for (int i = 1; i < capacity; i++) {
            table.add(new LinkedList<Entry<K, V>>());
        }
        size = 0;
    }

    private int hash(K key) {
        int h = key.hashCode() % table.size();
        if (h < 0) {
            h += table.size();
        }
        return h;
    }

    public void put(K key, V value) {
        int index = hash(key);
        LinkedList<Entry<K, V>> bucket = table.get(index);

        // Check if the key already exists in the bucket
        for (Entry<K, V> entry : bucket) {
            if (entry.getKey().equals(key)) {
                // Key found, update the value
                entry.setValue(value);
                return;
            }
        }

        // Key not found, add a new entry
        bucket.add(new Entry<>(key, value));
        size++;

        // Check if the table needs to be resized
        if ((double) size / table.size() > 2) {
            resize();
        }
    }

    public V get(K key) {
        int index = hash(key);
        LinkedList<Entry<K, V>> bucket = table.get(index);

        // Search for the key in the bucket
        for (Entry<K, V> entry : bucket) {
            if (entry.getKey().equals(key)) {
                return entry.getValue();
            }
        }

        // Key not found
        return null;
    }

    public void remove(K key) {
        int index = hash(key);
        LinkedList<Entry<K, V>> bucket = table.get(index);

        // Search for the key in the bucket and remove it
        for (Entry<K, V> entry : bucket) {
            if (entry.getKey().equals(key)) {
                bucket.remove(entry);
                size--;
                return;
            }
        }
    }

    private void resize() {
        int newCapacity = table.size() * 2;
        ArrayList<LinkedList<Entry<K, V>>> newTable = new ArrayList<>(newCapacity);

        // Initialize the new table
        for (int i = 0; i < newCapacity; i++) {
            newTable.add(new LinkedList<Entry<K, V>>());
        }

        // Rehash and insert all entries into the new table
        for (LinkedList<Entry<K, V>> bucket : table) {
            for (Entry<K, V> entry : bucket) {
                int newIndex = entry.getKey().hashCode() % newCapacity;
                if (newIndex < 0) {
                    newIndex += newCapacity;
                }
                newTable.get(newIndex).add(entry);
            }
        }

        // Update the table reference
        table = newTable;
    }

    public int size() {
        return size;
    }

    public ArrayList<V> values() {
        ArrayList<V> values = new ArrayList<>();
        for (LinkedList<Entry<K, V>> bucket : table) {
            for (Entry<K, V> entry : bucket) {
                values.add(entry.getValue());
            }
        }
        return values;
    }

    private static class Entry<K, V> {
        private K key;
        private V value;

        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public void setValue(V value) {
            this.value = value;
        }
    }
}