import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

/**
 * 347-Top-K-Frequent-Elements
 *
 * @author Li Ersan
 */
class Solution {

    private class Array<E> {

        /**
         * 数据数组
         */
        private E[] data;

        /**
         * 数组元素的个数
         */
        private int size;

        /**
         * 构造函数，传入数组的容量capacity构造Array
         *
         * @param capacity
         */
        public Array(int capacity) {
            data = (E[]) new Object[capacity];
        }

        /**
         * 无参构造函数，默认数组容量capacity为10
         */
        public Array() {
            this(10);
        }

        public Array(E[] arr) {
        /*data = (E[]) new Object[arr.length];
        for (int i = 0; i < arr.length; i++) {
            data[i] = arr[i];
        }*/
            data = arr;
            size = arr.length;
        }

        /**
         * 获取数组中元素的个数
         *
         * @return
         */
        public int getSize() {
            return size;
        }

        /**
         * 获取数组的容量
         *
         * @return
         */
        public int getCapacity() {
            return data.length;
        }

        /**
         * 判断数组是否为空
         *
         * @return
         */
        public boolean isEmpty() {
            return size == 0;
        }

        /**
         * 在数组后面添加一个新元素
         *
         * @param e
         */
        public void addLast(E e) {
            add(size, e);
        }

        /**
         * 在数组最前面添加一个新元素
         *
         * @param e
         */
        public void addFirst(E e) {
            add(0, e);
        }

        /**
         * 在index个位置插入一个新元素e
         *
         * @param index
         * @param e
         */
        public void add(int index, E e) {

            if (index < 0 || index > size) {
                throw new IllegalArgumentException("index范围错误");
            }

            if (size == data.length) {
                resize(2 * data.length);
            }

            for (int i = size - 1; i >= index; i--) {
                data[i + 1] = data[i];
            }
            data[index] = e;
            size++;
        }

        /**
         * 获取int索引位置的元素
         *
         * @param index
         * @return
         */
        public E get(int index) {
            if (index < 0 || index >= size) {
                throw new IllegalArgumentException("index范围错误");
            }

            return data[index];
        }

        /**
         * 修改index索引位置的元素e
         *
         * @param index
         * @param e
         */
        public void set(int index, E e) {
            if (index < 0 || index >= size) {
                throw new IllegalArgumentException("index范围错误");
            }

            data[index] = e;
        }

        /**
         * 查找数组中是否包含元素e
         *
         * @param e
         * @return
         */
        public boolean contains(E e) {

            for (int i = 0; i < size; i++) {
                if (data[i].equals(e)) {
                    return true;
                }
            }

            return false;
        }

        /**
         * 查找数组中元素e所在的索引，如果不存在元素e，则返回-1
         *
         * @param e
         * @return
         */
        public int find(E e) {
            for (int i = 0; i < size; i++) {
                if (data[i] == e) {
                    return i;
                }
            }

            return -1;
        }

        /**
         * 从数组中删除index位置的元素，返回删除的元素
         *
         * @param index
         * @return
         */
        public E remove(int index) {
            if (index < 0 || index >= size) {
                throw new IllegalArgumentException("index范围错误");
            }

            E ret = data[index];
            for (int i = index + 1; i < size; i++) {
                data[i - 1] = data[i];
            }

            size--;
            data[size] = null; //loitering objects != memory leak

            if (size == data.length / 4 && data.length / 2 != 0) {
                resize(data.length / 2);
            }

            return ret;
        }

        /**
         * 从数组中删除第一个元素，返回删除的元素
         *
         * @return
         */
        public E removeFirst() {
            return remove(0);
        }

        /**
         * 从数组中删除最后一个元素，返回删除的元素
         *
         * @return
         */
        public E removeLast() {
            return remove(size - 1);
        }

        /**
         * 从数组中删除元素e
         *
         * @param e
         */
        public void removeElement(E e) {
            int index = find(e);

            if (index != -1) {
                remove(index);
            }
        }

        /**
         * 交换两个索引位置的值
         *
         * @param i
         * @param j
         */
        public void swap(int i, int j) {
            if (i < 0 || i >= size || j < 0 || j >= size) {
                throw new IllegalArgumentException("索引错误");
            }

            E t = data[i];
            data[i] = data[j];
            data[j] = t;
        }

        @Override
        public String toString() {

            StringBuilder res = new StringBuilder();
            res.append(String.format("Array: size = %d, capacity = %d\n", size, data.length));
            res.append("[");

            for (int i = 0; i < size; i++) {
                res.append(data[i]);
                if (i != size - 1) {
                    res.append(", ");
                }
            }
            res.append("]");

            return res.toString();
        }

        private void resize(int newCapacity) {
            E[] newData = (E[]) new Object[newCapacity];

            for (int i = 0; i < size; i++) {
                newData[i] = data[i];
            }

            data = newData;
        }
    }

    private interface Queue<E> {

        int getSize();

        boolean isEmpty();

        void enqueue(E e);

        E dequeue();

        E getFront();
    }

    private class MaxHeap<E extends Comparable<E>> {

        private Array<E> data;

        public MaxHeap(int capacity) {
            data = new Array<>(capacity);
        }

        public MaxHeap() {
            data = new Array<>();
        }

        public MaxHeap(E[] arr) {
            data = new Array<>(arr);
            for (int i = parent(arr.length - 1); i >= 0; i--) {
                siftDown(i);
            }
        }

        /**
         * 返回堆中的元素个数
         *
         * @return
         */
        public int size() {
            return data.getSize();
        }

        /**
         * 返回一个布尔值，表示堆中是否为空
         *
         * @return
         */
        public boolean isEmpty() {
            return data.isEmpty();
        }

        /**
         * 返回完全二叉树的数组表示中，一个索引所表示的元素的父亲结点的索引
         *
         * @param index
         * @return
         */
        private int parent(int index) {

            if (index == 0) {
                throw new IllegalArgumentException("索引0是没有父亲结点的！");
            }

            return (index - 1) / 2;
        }

        /**
         * 返回完全二叉树的数组表示中，一个索引所表示的元素的左孩子结点的索引
         *
         * @param index
         * @return
         */
        private int leftChild(int index) {
            return index * 2 + 1;
        }

        /**
         * 返回完全二叉树的数组表示中，一个索引所表示的元素的右孩子结点的索引
         *
         * @param index
         * @return
         */
        private int rightChild(int index) {
            return index * 2 + 2;
        }

        /**
         * 向堆中添加元素
         *
         * @param e
         */
        public void add(E e) {
            data.addLast(e);
            siftUp(data.getSize() - 1);
        }

        private void siftUp(int k) {
            while (k > 0 && data.get(parent(k)).compareTo(data.get(k)) < 0) {
                data.swap(k, parent(k));
                k = parent(k);
            }
        }

        /**
         * 看堆中的最大元素
         *
         * @return
         */
        public E findMax() {

            if (data.isEmpty()) {
                throw new IllegalArgumentException("堆为空，无法查找最大值");
            }

            return data.get(0);
        }

        /**
         * 取出堆中最大元素
         *
         * @return
         */
        public E extractMax() {

            E ret = findMax();

            data.swap(0, data.getSize() - 1);
            data.removeLast();
            siftDown(0);

            return ret;
        }

        private void siftDown(int k) {
            while (leftChild(k) < data.getSize()) {
                int j = leftChild(k);
                if (rightChild(k) < data.getSize() && data.get(rightChild(k)).compareTo(data.get(j)) > 0) {
                    j = rightChild(k);
                }
                //data[j]是 leftChild 和 rightChild 中的最大值
                if (data.get(k).compareTo(data.get(j)) >= 0) {
                    break;
                }

                data.swap(k, j);
                k = j;
            }
        }

        /**
         * 取出堆中的最大元素，并且替换成元素e
         *
         * @param e
         * @return
         */
        public E replace(E e) {

            E ret = findMax();
            data.set(0, e);
            siftDown(0);
            return ret;
        }


    }

    private class PriorityQueue<E extends Comparable<E>> implements Queue<E> {

        private MaxHeap<E> maxHeap;

        public PriorityQueue() {
            maxHeap = new MaxHeap<>();
        }

        @Override
        public int getSize() {
            return maxHeap.size();
        }

        @Override
        public boolean isEmpty() {
            return maxHeap.isEmpty();
        }

        @Override
        public void enqueue(E e) {
            maxHeap.add(e);
        }

        @Override
        public E dequeue() {
            return maxHeap.extractMax();
        }

        @Override
        public E getFront() {
            return maxHeap.findMax();
        }
    }


    private class Freq implements Comparable<Freq>{
        public int e, freq;

        public Freq(int e, int freq) {
            this.e = e;
            this.freq = freq;
        }

        @Override
        public int compareTo(Freq o) {
            if (this.freq < o.freq) {
                return 1;
            } else if (this.freq > o.freq) {
                return -1;
            } else {
                return 0;
            }
        }
    }

    public List<Integer> topKFrequent(int[] nums, int k) {

        TreeMap<Integer, Integer> map = new TreeMap<>();
        for (int num : nums) {
            if (map.containsKey(num)) {
                map.put(num, map.get(num) + 1);
            } else {
                map.put(num, 1);
            }
        }

        PriorityQueue<Freq> pq = new PriorityQueue<>();
        for (int key : map.keySet()) {
            if (pq.getSize() < k) {
                pq.enqueue(new Freq(key, map.get(key)));
            } else if (map.get(key) > pq.getFront().freq) {
                pq.dequeue();
                pq.enqueue(new Freq(key, map.get(key)));
            }
        }

        LinkedList<Integer> res = new LinkedList<>();
        while (!pq.isEmpty()) {
            res.add(pq.dequeue().e);
        }

        return res;
    }
}
