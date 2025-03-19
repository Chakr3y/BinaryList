package BinaryList;
import org.junit.Test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Arrays;

@SuppressWarnings("unused")
public class BinaryListTest {
    private void foo() {
        new ArrayList<>();
        new HashSet<>();
    }

    @Test
    public void testConstructor() {
        assertEquals(0, new BinaryList().value());

        
        BinaryList b = new BinaryList("011101");
        assertEquals(29, b.value());
    }

    @Test(expected=NumberFormatException.class)
    public void testConstructor2() {
        new BinaryList("-1");
    }

    @Test
    public void testAdd() {
        BinaryList b1 = new BinaryList(2);
        b1.add(true);
        assertEquals(6, b1.value());

        BinaryList b2 = new BinaryList(1, 0);
        assertEquals(0, b2.value());
        b2.add(0);
        assertEquals(0, b2.value());

        BinaryList b3 = new BinaryList(1, 0);
        b3.add(1);
        assertEquals(1, b3.value());
    }

    @Test
    public void testAdd2() {
        BinaryList b = new BinaryList(2); // 10
        b.add(0, true); // 101
        assertEquals(5, b.value());
        b.add(1, false); // 1001
        assertEquals(9, b.value());
    }
    
    @Test
    public void testEquals() {
        assertNotEquals(new BinaryList(), null);
        
        // equals other BinaryList
        BinaryList b1 = new BinaryList(5);
        BinaryList b2 = new BinaryList(1, 2); b2.add(1);
        assertEquals(b1, b2);
        
        // equals non-BinaryList
        List<Boolean> l1 = Arrays.asList(true, false, true);
        assertEquals(b1, l1);
        assertEquals(l1, b1);
    }

    @Test
    public void testClone() {
        BinaryList b1 = new BinaryList(12);
        BinaryList b2 = b1.clone();

        b1.add(true);
        assertNotEquals(b1, b2);

        b2.add(true);
        assertEquals(b1, b2);
    }

    @Test
    public void testGet() {
        BinaryList b = new BinaryList(51); // 0011 0011
        assertTrue(b.get(1));
        assertFalse(b.get(2));
    }

    @Test
    public void testIterator() {
        BinaryList b = new BinaryList(43);

        int[] expected = {1,1,0,1,0,1};
        int i = 0;
        for (boolean e : b)
            assertEquals(expected[i++] == 1, e);
    }

    @Test
    public void testReversed() {
        BinaryList b1 = new BinaryList(31); // 1 1111
        assertEquals(b1, b1.reversed());
        b1.add(false); // 01 1111
        assertNotEquals(b1, b1.reversed());
        assertEquals(62, b1.reversed().value());

        BinaryList b2 = new BinaryList(27); // 1 1011
        assertEquals(b2, b2.reversed());
    }

    @Test
    public void testSet() {
        BinaryList b1 = new BinaryList(15);
        b1.set(2, false);
        assertEquals(11, b1.value());
        b1.set(2, true);
        assertEquals(15, b1.value());
    }

    @Test
    public void testRemove() {
        BinaryList b1 = new BinaryList(13); // 1101
        assertEquals(4, b1.size());
        b1.remove(1);
        assertEquals(7, b1.value());
        assertEquals(3, b1.size());
        
        // TODO: test for unsigned shifts
    }
}
