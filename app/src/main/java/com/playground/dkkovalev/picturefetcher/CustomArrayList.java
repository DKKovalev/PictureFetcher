package com.playground.dkkovalev.picturefetcher;

import java.util.AbstractList;
import java.util.Arrays;

public class CustomArrayList<E> extends AbstractList<E> {

    private Object[] innerArray;
    private Object[] emptyInnerArray = {};
    private int size = 1;

    private final static int DEFAULT_CAPACITY = 10;

    public CustomArrayList(int capacity) {
        if (capacity < 0) {
            throw new IllegalArgumentException("Illegal capacity" + capacity);
        }

        this.innerArray = new Object[capacity];
    }

    public CustomArrayList() {
        this.innerArray = emptyInnerArray;
    }

    public int size() {
        return innerArray.length;
    }

    E innerArray(int index) {
        return (E) innerArray[index];
    }

    public E get(int index) {
        rangeCheck(index);

        return innerArray(index);
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean add(E e) {

        if (innerArray.length - size <= 5) {
            innerArray = Arrays.copyOf(innerArray, innerArray.length * 2);
            System.out.println(innerArray.length);
        }
        innerArray[size++] = e;
        return true;
    }

    private void rangeCheck(int index) {
        if (index >= size) {
            throw new IndexOutOfBoundsException("Out of bounds, nigga");
        }
    }

    public void removeByIndex(int index) {
        rangeCheck(index);
        int numMoved = size - index - 1;
        if (numMoved > 0)
            System.arraycopy(innerArray, index + 1, innerArray, index,
                    numMoved);
        innerArray[--size] = null;
    }

    public void removeAll() {
        for (int i = 0; i < innerArray.length; i++) {
            innerArray[i] = 0;
            size = 0;
        }
    }

    private void increaseCapacity() {
        innerArray = Arrays.copyOf(innerArray, innerArray.length * 2);
    }
}