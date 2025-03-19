package BinaryListPlus;
import BinaryList.BinaryList;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.math.BigInteger;

/**
 * This data structure expands the storage capacity of a BinaryList beyond 32/max bits
 * by storing ints inside a List.
 * This should theoretically allow it to hold unlimited bits.
 */
public class BLP implements List<Boolean> {
    // Integer value that stores the list of bits in binary.
    List<Integer> list;
    // Number of elements in the list.
    int size;
    // Max size for BinaryList
    public static final int MAX_SIZE = Integer.BYTES * 8;

    /**
     * Construct with {@code list} and {@code size} set to 0.
     */
    public BLP() {
        this(0);
    }

    /**
     * Construct with provided {@code list}, automatically calculates {@code size}.
     * @param list initial value of the list
     */
    public BLP(int list) {
        this(list, 0);

        for (int copy = list; copy > 0; copy >>= 1)
            size++;
    }

    /**
     * Construct with provided {@code list} and {@code size}. Allows list to exceed provided size.
     * @param list initial value of the list
     * @param size number of bits to consider in list
     */
    public BLP(int list, int size) {
        this.list = new ArrayList<Integer>();
        this.list.add(list);
        this.size = size;
    }

    /**
     * For cloning
     * @param l list to be adopted
     * @param size
     */
    BLP(List<Integer> l, int size) {
        this.list = l;
        this.size = size;
    }

    /**
     * Construct by parsing a number from string. Only 0s and 1s are allowed.
     * @throws NumberFormatException if {@code String} contains anything other than 0 or 1.
     * @see Integer#parseInt(String, int)
     */
    public BLP(String list) {
        if (!list.matches("[01]+"))
            throw new NumberFormatException();

        this.list = new ArrayList<Integer>();
        this.list.add(Integer.parseInt(list, 2));
        this.size = list.length();
    }

    public BigInteger value() {
        BigInteger val = new BigInteger("0");
        for (int e : list)
            val = val.add(new BigInteger(""+e));
        return val;
    }

    public int size() {
        return size;
    }

    // // Can check if losing data
    // public boolean isFull() {
    //     return size >= MAX_SIZE;
    // }

    /**
        * Appends the specified boolean to the end of this list.
        * @param e boolean to be appended to this list
        * @return {@code true} (as specified by {@link Collection#add})
        */
    public boolean add(Boolean e) {
        if (size % MAX_SIZE == 0)
            list.add(0);
        
        list.getLast().add(e);
        
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
        *         ({@code index < 0 || index >= size()})
        */
    public void add(int index, Boolean element) {
        if (index < 0 || index > size) throw new IndexOutOfBoundsException();
        if (index == size) {
            add(element);
            return;
        }

        // TODO
        
        size++;
    }

    /**
        * Calls add method with int converted to boolean. Recursively calls add(Boolean).
        * @param e 0 or 1
        * @throws IllegalArgumentException if e is not 0 or 1
        * @see BLP#add(Boolean)
        */
    public boolean add(int e) {
        if (e != 0 && e != 1)
            throw new IllegalArgumentException();

        add(e == 1);
        return true;
    }

    /**
        * Calls add method with int converted to boolean. Recursively calls add(int, Boolean).
        * @param e 0 or 1
        * @throws IllegalArgumentException if e is not 0 or 1
        * @see BLP#add(int, Boolean)
        */
    public void add(int index, int e) {
        if (e != 0 && e != 1)
            throw new IllegalArgumentException();

        add(index, e == 1);
        return;
    }
    
    /**
        * Unsupported operation.
        * @throws UnsupportedOperationException
        */
    public boolean remove(Object o) {
        throw new UnsupportedOperationException("Unimplemented method 'remove'");
    }
    
    /**
        * Removes the bit at the specified position in this list.
        * Shifts any subsequent bits to the left (subtracts one from their
        * indices).
        *
        * @param index the index of the bit to be removed
        * @return the boolean that was removed from the list
        * @throws IndexOutOfBoundsException if the index is out of range
        *         ({@code index < 0 || index >= size()})
        */
    public Boolean remove(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();

        size--;
        return list.get(index/MAX_SIZE).get(index%MAX_SIZE);
    }
    
    public Boolean get(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();

        return (list & (1 << index)) != 0;
    }
    
    public Boolean set(int index, Boolean element) {
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
        * @see BinaryList#set(int, Boolean)
        */
    public Boolean set(int index, int e) {
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
        BLP other = (BLP) obj;
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
        for (BinaryList b : list)
            result = prime * result + b.value();
        
        return result;
    }

    public Boolean[] toArray() {
        Boolean[] array = new Boolean[size];

        int i = 0;
        for (boolean e : this)
            array[i++] = e;

        return array;
    }

    /**
        * Copied from ArrayList.toArray(T[])
        */
    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        Boolean[] elementData = toArray();

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
    public BLP clone() {
        List<BinaryList> l = new ArrayList<>();
        for (BinaryList b : list)
            l.add(b.clone());
        
        return new BLP(l, size);
    }

    @Override
    public Iterator<Boolean> iterator() {
        return new BLPIterator();
    }

    class BLPIterator implements Iterator<Boolean> {
        // a
        int n;
        // tracking current index in list
        int i;
        BLPIterator() {
            copy = list;
            i = 0;
        }

        @Override
        public boolean hasNext() {
            return i != 0;
        }

        @Override
        public Boolean next() {
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
    public boolean addAll(Collection<? extends Boolean> c) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addAll'");
    }

    /**
        * Unsupported operation.
        */
    @Override
    public boolean addAll(int index, Collection<? extends Boolean> c) {
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

    @Override
    public void clear() {
        list.clear();
        list.add(new BinaryList());
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
    public ListIterator<Boolean> listIterator() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'listIterator'");
    }

    /**
        * Unsupported operation.
        */
    @Override
    public ListIterator<Boolean> listIterator(int index) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'listIterator'");
    }

    /**
        * Unsupported operation.
        */
    @Override
    public List<Boolean> subList(int fromIndex, int toIndex) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'subList'");
    }
}