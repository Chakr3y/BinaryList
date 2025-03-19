package BinaryList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.List;
import java.util.AbstractList;
import java.util.ListIterator;

/**
 * An implementation of List<Boolean> by using binary integer.
 * Elements are stored at 2^n.
 * 
 * Some javadoc is derived from Java's List/ArrayList implementation.
 */
public class BinaryList extends AbstractList<Boolean> {
    // Integer value that stores the list of bits in binary.
    int list;
    // Number of elements in the list.
    int size;
    // Max that size should be, that is the bit-length of int
    public static final int MAX_SIZE = Integer.BYTES * 8;

    /**
     * Construct with {@code list} and {@code size} set to 0.
     */
    public BinaryList() {
        this(0);
    }

    /**
     * Construct with provided {@code list}, automatically calculates {@code size}.
     * @param list initial value of the list
     */
    public BinaryList(int list) {
        this(list, 0);

        for (int copy = list; copy > 0; copy >>= 1)
            size++;
    }

    /**
     * Construct with provided {@code list} and {@code size}. Allows list to exceed provided size.
     * @param list initial value of the list
     * @param size number of bits to consider in list
     */
    public BinaryList(int list, int size) {
        this.list = list;
        this.size = size;
    }

    /**
     * Construct by parsing a number from string. Only 0s and 1s are allowed.
     * @throws NumberFormatException if {@code String} contains anything other than 0 or 1.
     * @see Integer#parseInt(String, int)
     */
    public BinaryList(String list) {
        if (!list.matches("[01]+"))
            throw new NumberFormatException();

        this.list = Integer.parseInt(list, 2);
        this.size = list.length();
    }

    /**
     * Collection constructor, as per specification of AbstractList
     * @param c
     */
    public BinaryList(Collection<? extends Boolean> c) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public int value() {
        //return list;
        return list & ((1 << size) - 1);
    }

    public int size() {
        return size;
    }

    // Can check if losing data
    public boolean isFull() {
        return size >= MAX_SIZE;
    }

    /**
     * Appends the specified boolean to the end of this list.
     * @param e boolean to be appended to this list
     * @return {@code true} (as specified by {@link Collection#add})
     */
    public boolean add(Boolean e) {
        if (isFull()) throw new IndexOutOfBoundsException("BinaryList overflow");

        if (e)
            list |= (1 << size);
        else
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
    public void add(int index, Boolean element) {
        if (index < 0 || index > size) throw new IndexOutOfBoundsException();
        if (isFull()) throw new IndexOutOfBoundsException("BinaryList overflow");
        if (index == size) {
            add(element);
            return;
        }

        int maskR = (1 << index) - 1;
        int maskL = ~maskR;

        list = (list & maskL) << 1 | (list & maskR);

        if (element)
            list |= 1 << index;
        else
            list &= ~(1 << index);
        
        size++;
    }

    /**
     * Calls add method with int converted to boolean. Recursively calls add(Boolean).
     * @param e 0 or 1
     * @throws IllegalArgumentException if e is not 0 or 1
     * @see BinaryList#add(Boolean)
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
     * @see BinaryList#add(int, Boolean)
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

        // Removed bit
        boolean p = get(index);
        
        int maskR = (1 << index) - 1;
        int maskL = ~maskR - (1 << index);
        // int maskL = -1 << (1+index);

        list = (list & maskL) >>> 1 | (list & maskR);
        size--;

        return p;
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

    // Size manipulation
    // Increases size //(overwrites with 0)
    public void pad_back(int n) {
        //list &= ~((1 << size + n) - (1 << size));
        size += n;
    }
    // Adds 0s to front
    public void pad_front(int n) {
        list <<= n;
        size += n;
    }
    // Removes ends
    public void trim_front(int n) {
        list >>>= n;
        size -= n;
    }
    // Note: does not clear bits
    public void trim_back(int n) {
        size -= n;
    }

//    public void checkHighestBit() {
//        // can use Integer methods?
//    }

    /**
     * Note: Does not check for concurrent modification.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        // also checks for null
        if (!(obj instanceof List))
            return false;
        
        if (obj.getClass() == BinaryList.class) {
            BinaryList other = (BinaryList) obj;
            return size == other.size() && value() == other.value();
        } else {
            return super.equals(obj);
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + size;
        result = prime * result + value();
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
    	return String.format("%"+size()+"s", Integer.toBinaryString(value()))
                     .replace(' ', '0');
    }

    @Override
    public BinaryList clone() {
        return new BinaryList(list, size);
    }

    // @Override
    // public Iterator<Boolean> iterator() {
    //     return new BinaryIterator();
    // }

    // class BinaryIterator implements Iterator<Boolean> {
    //     int copy;
    //     int i;
    //     BinaryIterator() {
    //         copy = list;
    //         i = size;
    //     }

    //     @Override
    //     public boolean hasNext() {
    //         return i != 0;
    //     }

    //     @Override
    //     public Boolean next() {
    //         if (i < 1) throw new NoSuchElementException();;

    //         i--;
    //         int n = copy % 2;
    //         copy >>>= 1;
    //         return n != 0;
    //     }
    // }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        list = 0;
        size = 0;
    }
}
