package optionsParser;

import java.util.Iterator;

/**
 * Created by erick on 10/07/16.
 *
 * This class implements the basic behavior of an array
 * iterator. It supports removing elements from the underlying
 * array, but it is an O(n) operation and must be used carefully.
 * The underlying array is accessed by reference, therefore the
 * remove() operation will take place in the original array (as
 * expected).
 */
public class ArrayIterator<T> implements Iterator<T> {

    private T[] array;
    private int currentPosition = -1;

    public ArrayIterator(T[] array) {
        this.array = array;
    }

    @Override
    public boolean hasNext() {
        return currentPosition < array.length - 1;
    }

    @Override
    public T next() {
        currentPosition++;
        return array[currentPosition];
    }

    @Override
    public void remove() {
        for(int i = currentPosition; i < array.length - 1; i++) {
            array[i] = array[i + 1];
        }
        currentPosition--;
    }
}
