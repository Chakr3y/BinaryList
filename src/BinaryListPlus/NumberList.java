package BinaryListPlus;

import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

import BinaryList.BinaryList;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

/**
 * Stores an array of numbers, restricted to a pre-determined number of bits.
 * Uses a {@code List<Integer>} to store bits consecutively
 */
public class NumberList implements List<Integer> {
    // main data structure
    List<Integer> list;
    // no. of 'numbers' stored
    int size;
    // pre-determined number of bits to store per number
    // not to be changed after construction
    int bits;
    
    // Max bits stored in each integer
    public static final int MAX_SIZE = Integer.BYTES * 8;


    /**
     * Construct with {@code list} and {@code size} set to 0.
     */
    public NumberList() {
        this(0);
    }

    /**
     * Construct with provided {@code list}, automatically calculates {@code size} and bits.
     * @param list initial value of the list
     */
    public NumberList(int list) {
        this(list, 0);

        for (int copy = list; copy > 0; copy >>= 1)
            size++;
    }

    /**
     * Construct with provided {@code list} and {@code size}. Allows list to exceed provided size.
     * @param list initial value of the list
     * @param size number of bits to store for each element
     */
    public NumberList(int list, int size) {
        this.list = new ArrayList<Integer>();
        this.list.add(list);
        this.size = size;
        this.bits = size;
    }

    // public NumberList(int list, int size, int bits) {
    //     this.list = new ArrayList<Integer>();
    //     this.list.add(list);
    //     this.size = size;
    //     this.bits = bits;
    // }

    /**
     * For cloning
     * @param l list to be adopted
     * @param size
     */
    NumberList(List<Integer> l, int size) {
        this.list = l;
        this.size = size;
    }

    /**
     * Construct by parsing a number from string. Only 0s and 1s are allowed.
     * @throws NumberFormatException if {@code String} contains anything other than 0 or 1.
     * @see Integer#parseInt(String, int)
     */
    // public NumberList(String list) {
    //     if (!list.matches("[01]+"))
    //         throw new NumberFormatException();

    //     this.list = new ArrayList<Integer>();
    //     this.list.add(Integer.parseInt(list, 2));
    //     this.size = list.length();
    // }

    public BigInteger value() {
        BigInteger val = new BigInteger("0");
        for (int e : list)
            val = val.add(new BigInteger(""+e));
        return val;
    }

    public int size() {
        return size;
    }

    public int bits() {
        return bits;
    }

    /**
     * Appends the specified int to the end of this list.
     * Will not throw if number's bits exceeds {@code bits}, but
     * @param e int to be appended to this list
     * @return {@code true} (as specified by {@link Collection#add})
     */
    public boolean add(Integer e) {
        // if more storage is needed
        boolean overflow = size % MAX_SIZE + bits > MAX_SIZE;
        if (overflow)
            list.add(0);

        int index = size/MAX_SIZE;
        
        list &= ~(1 << size);
        
        size++;
        return true;
    }

    /**
     * Inserts the specified boolean at the specified position in this
     * list. Shifts the bit currently at that position (if any) and
     * any subsequent bits to the left (adds one to their indices).
     *
     * @param index index at which the specified boolean is to be inserted
     * @param element boolean to be inserted
     * @throws IndexOutOfBoundsException if the index is out of range
     *         ({@code index < 0 || index > size()})
     */
    public void add(int index, Integer element) {
        if (index < 0 || index > size) throw new IndexOutOfBoundsException();
        if (index == size) {
            add(element);
            return;
        }

        // TODO
        
        size++;
    }

    /**
     * Unsupported operation.
     * @throws UnsupportedOperationException
     */
    public boolean remove(Object o) {
        throw new UnsupportedOperationException("Unimplemented method 'remove'");
    }
    
    /**
     * Removes the int at the specified position in this list.
     * Shifts any subsequent elements to the left (subtracts one from their
     * indices).
     *
     * @param index the index of the int to be removed
     * @return the int that was removed from the list
     * @throws IndexOutOfBoundsException if the index is out of range
     *         ({@code index < 0 || index >= size()})
     */
    public Integer remove(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();

        size--;
        return list.get(index/MAX_SIZE);
    }
    
    public Integer get(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();

        int startIdx = (index * size) % MAX_SIZE ;

        return (list & (1 << index)) != 0;
    }
    
    public Integer set(int index, Integer element) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();
        
        // Replaced bit
        boolean p = get(index);
        
        if (p != element)
            list ^= (1 << index);

        return p;
    }

    /**
     * Calls set method with int converted to boolean.
     * @param e 0 or 1
     * @throws IllegalArgumentException if e is not 0 or 1
     * @see BinaryList#set(int, Integer)
     */
    public Integer set(int index, int e) {
        if (e != 0 && e != 1)
            throw new IllegalArgumentException();

        return set(index, e == 1);
    }

    /**
     * Returns new instance with bits reversed.
     */
    public BinaryList reversed() {
        // TODO AAAHHhHHHH
        int copy = list;
        int r = 0;
        
        for (int i = 0; i < size; i++) {
            r <<= 1;
            if ((copy & 1) != 0)
                r |= 1;
            
            copy >>>= 1;
        }

        return new BinaryList(r, size);
    }

    // // Size manipulation
    // // Increases size //(overwrites with 0)
    // public void pad_back(int n) {
    //     //list &= ~((1 << size + n) - (1 << size));
    //     size += n;
    // }
    // // Adds 0s to front
    // public void pad_front(int n) {
    //     list <<= n;
    //     size += n;
    // }
    // // Removes ends
    // public void trim_front(int n) {
    //     list >>>= n;
    //     size -= n;
    // }
    // // Note: does not clear bits
    // public void trim_back(int n) {
    //     size -= n;
    // }

    /*public void checkHighestBit() {
        // can use Integer methods?
    }*/

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        NumberList other = (NumberList) obj;
        if (size != other.size)
            return false;
        if (value() != other.value())
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + size;
        result = prime * result + bits;
        for (int e : list)
            result = prime * result + e;
        
        return result;
    }

    public Integer[] toArray() {
        Integer[] array = new Integer[size];

        int i = 0;
        for (int e : this)
            array[i++] = e;

        return array;
    }

    /**
     * Copied from ArrayList.toArray(T[])
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        Integer[] elementData = toArray();

        if (a.length < size)
            // Make a new array of a's runtime type, but my contents:
            return (T[]) Arrays.copyOf(elementData, size, a.getClass());
        System.arraycopy(elementData, 0, a, 0, size);
        if (a.length > size)
            a[size] = null;
        return a;
    }
    
    @Override
    public String toString() {
        return String.format("%"+size()+"s", value().toString())
                     .replace(' ', '0');
    }

    @Override
    public NumberList clone() {
        List<Integer> l = new ArrayList<>();
        for (int e : list)
            l.add(e);
        
        return new NumberList(l, size);
    }

    @Override
    public Iterator<Integer> iterator() {
        return new NumberListIterator();
    }

    class NumberListIterator implements Iterator<Integer> {
        // a
        int n;
        // tracking current index in list
        int i;
        NumberListIterator() {
            copy = list;
            i = 0;
        }

        @Override
        public boolean hasNext() {
            return i != 0;
        }

        @Override
        public Integer next() {
            if (i < 1) throw new NoSuchElementException();;

            i--;
            int n = copy % 2;
            copy >>>= 1;
            return n != 0;
        }
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Unimplemented
     */
    @Override
    public boolean contains(Object o) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'contains'");
    }

    /**
     * Unsupported operation.
     */
    @Override
    public boolean containsAll(Collection<?> c) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'containsAll'");
    }

    /**
     * Unsupported operation.
     */
    @Override
    public boolean addAll(Collection<? extends Integer> c) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addAll'");
    }

    /**
     * Unsupported operation.
     */
    @Override
    public boolean addAll(int index, Collection<? extends Integer> c) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addAll'");
    }

    /**
     * Unsupported operation.
     */
    @Override
    public boolean removeAll(Collection<?> c) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'removeAll'");
    }

    /**
     * Unsupported operation.
     */
    @Override
    public boolean retainAll(Collection<?> c) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'retainAll'");
    }

    /**
     * Clears out list and size but retains bits.
     */
    @Override
    public void clear() {
        list.clear();
        list.add(0);
        size = 0;
    }

    /**
     * Unsupported operation.
     */
    @Override
    public int indexOf(Object o) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'indexOf'");
    }

    /**
     * Unsupported operation.
     */
    @Override
    public int lastIndexOf(Object o) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'lastIndexOf'");
    }

    /**
     * Unsupported operation.
     */
    @Override
    public ListIterator<Integer> listIterator() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'listIterator'");
    }

    /**
     * Unsupported operation.
     */
    @Override
    public ListIterator<Integer> listIterator(int index) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'listIterator'");
    }

    /**
     * Unsupported operation.
     */
    @Override
    public List<Integer> subList(int fromIndex, int toIndex) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'subList'");
    }
}
