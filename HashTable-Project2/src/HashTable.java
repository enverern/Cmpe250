import java.util.LinkedList;
import java.util.ArrayList;

class HashTable<K, V> {
    private static int DEFAULT_CAPACITY = 50;
    private ArrayList<LinkedList<Entry<K, V>>> table;
    private int size;
    private int current_capacity;

    // constructor
    public HashTable() {
        this(DEFAULT_CAPACITY);
    }

    private HashTable(int capacity) {
        table = new ArrayList<LinkedList<Entry<K, V>>>(capacity);
        for (int i = 0; i < capacity; i++) {
            table.add(new LinkedList<Entry<K, V>>());
        }
        size = 0;
        current_capacity = capacity;
    }
    // hash function (I used seperate chaining)
    private int hash(K key) {
        int h = key.hashCode() % current_capacity;
        if (h < 0) {
            h += current_capacity;
        }
        return h;
    }
    // insert a new key-value pair
    public void put(K key, V value) {
        int index = hash(key);
        LinkedList<Entry<K, V>> bucket = table.get(index);

        // check if the key already exists in the bucket
        for (Entry<K, V> entry : bucket) {
            if (entry.getKey().equals(key)) {
                // key found, update the value
                entry.setValue(value);
                return;
            }
        }

        // key not found, add a new entry
        bucket.add(new Entry<>(key, value));
        size++;

        // check if the table needs to be resized,
        // resize if load-factor is greater than 2 which is the threshold for
        // slow operations in seperate chaining
        if (((double) size / current_capacity) > 2) {
            resize();
        }
    }

    // get the value of a key
    public V get(K key) {
        int index = hash(key);
        LinkedList<Entry<K, V>> bucket = table.get(index);

        // search for the key in the bucket
        for (Entry<K, V> entry : bucket) {
            if (entry.getKey().equals(key)) {
                return entry.getValue();
            }
        }

        // key not found
        return null;
    }

    // remove a key-value pair
    public void remove(K key) {
        int index = hash(key);
        LinkedList<Entry<K, V>> bucket = table.get(index);

        // search for the key in the bucket and remove it
        for (Entry<K, V> entry : bucket) {
            if (entry.getKey().equals(key)) {
                bucket.remove(entry);
                size--;
                return;
            }
        }
    }

    // resize the table if the load-factor is greater than 2
    private void resize() {
        int newCapacity = current_capacity * 2;
        ArrayList<LinkedList<Entry<K, V>>> newTable = new ArrayList<>(newCapacity);

        // initialize the new table
        for (int i = 0; i < newCapacity; i++) {
            newTable.add(new LinkedList<Entry<K, V>>());
        }

        // rehash and insert all entries into the new table
        for (LinkedList<Entry<K, V>> bucket : table) {
            for (Entry<K, V> entry : bucket) {
                int newIndex = entry.getKey().hashCode() % newCapacity;
                if (newIndex < 0) {
                    newIndex += newCapacity;
                }
                newTable.get(newIndex).add(entry);
            }
        }

        // update the table reference
        table = newTable;
        current_capacity = newCapacity;
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

    // entry class for key-value pairs
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