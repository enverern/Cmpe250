import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// I got the basic of the structure from PS session and I modified it to fit my project
public class MaxHeap<T extends Song> {
    private final List<T> heap;
    private final Comparator<T> comparator;
    private final Comparator<T> comparator1;
    private final Map<Integer, Integer> indexMap;

    // it works with the comparators which are used to compare the different scores of the songs depending on the category
    // also, to easily access the index of the song in the heap in removing, I used hashmap
    public MaxHeap(Comparator<T> comparator, Comparator<T> comparator1) {
        this.heap = new ArrayList<>();
        this.comparator = comparator;
        this.comparator1 = comparator1;
        this.indexMap = new HashMap<>();
    }

    public int size() {
        return heap.size();
    }

    public void insert(T element) {
        heap.add(element);
        int index = heap.size() - 1;
        indexMap.put(element.getId(), index);
        heapifyUp(index);
    }

    // the remove function is delete the song from the heap and update the heap (it is able to both heapifydown and heapifyup)
    public void remove(T element) {
        Integer id = element.getId();
        int index = indexMap.getOrDefault(id, -1);
        if (index != -1) {
            heap.set(index, heap.get(heap.size() - 1));
            indexMap.put(heap.get(index).getId(), index);
            indexMap.remove(id);
            heapifyUp(index);
            heap.remove(heap.size() - 1);
            heapifyDown(index);
        }
    }


    private void heapifyUp(int index) {
        while (index > 0) {
            int parentIndex = (index - 1) / 2;
            if (comparator.compare(heap.get(index), heap.get(parentIndex)) < 0 ||
                    (comparator.compare(heap.get(index), heap.get(parentIndex)) == 0 &&
                            comparator1.compare(heap.get(index), heap.get(parentIndex)) < 0)) {
                swap(index, parentIndex);
                index = parentIndex;
            } else {
                break;
            }
        }
    }

    private void swap(int i, int j) {
        T temp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, temp);

        // update the indices in the HashMap after the swap
        indexMap.put(heap.get(i).getId(), i);
        indexMap.put(heap.get(j).getId(), j);
    }

    public T getMax() {
        if (isEmpty()) {
            return null;
        }
        return heap.get(0);
    }

    public T extractMax() {
        if (isEmpty()) {
            return null;
        }

        T max = heap.get(0);
        int lastIdx = heap.size() - 1;
        heap.set(0, heap.get(lastIdx));
        heap.remove(lastIdx);

        heapifyDown(0);

        return max;
    }

    private void heapifyDown(int index) {
        int leftChildIdx = 2 * index + 1;
        int rightChildIdx = 2 * index + 2;
        int largest = index;

        largest = getLargest(leftChildIdx, largest);

        largest = getLargest(rightChildIdx, largest);

        if (largest != index) {
            swap(index, largest);
            heapifyDown(largest);
        }
    }

    private int getLargest(int rightChildIdx, int largest) {
        if (rightChildIdx < heap.size() && ((comparator.compare(heap.get(rightChildIdx), heap.get(largest)) < 0) || (comparator.compare(heap.get(rightChildIdx), heap.get(largest)) == 0 && comparator1.compare(heap.get(rightChildIdx), heap.get(largest)) < 0))) {
            largest = rightChildIdx;
        }
        return largest;
    }

    public T get(int i) {
        return heap.get(i);
    }

    public boolean isEmpty() {
        return heap.isEmpty();
    }

    public ArrayList<Integer> getIds() {
        ArrayList<Integer> ids = new ArrayList<>();
        for (T element : heap) {
            ids.add((element).getId());
        }
        return ids;
    }
}
