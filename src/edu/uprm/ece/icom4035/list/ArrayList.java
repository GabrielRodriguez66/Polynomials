package edu.uprm.ece.icom4035.list;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class ArrayList<E> implements List<E> {
	private E[] elements;
	private int size;
	private static final int INIT_CAP = 5;

	@SuppressWarnings("unchecked")
	public ArrayList() {
		elements = (E[]) new Object[INIT_CAP];
		size = 0;
	}

	@Override
	public int size() {return this.size;}

	@Override
	public boolean isEmpty() {return this.size == 0;}

	@Override
	public void add(E obj) {
		if(size == elements.length) changeCapacity(elements.length*2);
		elements[size] = obj;
		size++;
	}

	@Override
	public void add(int index, E obj) throws IndexOutOfBoundsException {
		if(index<0 || index>this.size) throw new IndexOutOfBoundsException("add: Invalid index = "+index);
		if(size == elements.length) changeCapacity(elements.length*2);
		rotateToR(index);
		elements[index] = obj;
		this.size++;
	}

	@Override
	public boolean remove(E obj) {
		int itr = this.firstIndex(obj);
		if(itr == -1) return false;
		else {
			return this.remove(itr);
		}
	}

	@Override
	public boolean remove(int index) throws IndexOutOfBoundsException {
		if(index<0 || index>=size) throw new IndexOutOfBoundsException("remove: Invalid index = "+index);
		rotateToL(index);
		size--;
		return true;
	}

	@Override
	public int removeAll(E obj) { 
		int count = 0, i = 0;
		while(i<size) {
			if(elements[i].equals(obj)) {
				this.remove(i);
				i = 0;
				count++;
			}
		}
		return count;
	}

	@Override
	public E get(int index) throws IndexOutOfBoundsException {
		if(index<0 || index>=size) throw new IndexOutOfBoundsException("get: Invalid index = "+index);
		return elements[index]; 
	}

	@Override
	public E set(int index, E obj) throws IndexOutOfBoundsException {
		if(index<0 || index>=size) throw new IndexOutOfBoundsException("set: Invalid index = "+index);
		E etr = elements[index];
		elements[index] = obj;
		return etr;
	}

	@Override
	public E first() {
		if(this.isEmpty()) return null;
		return this.elements[0];
	}

	@Override
	public E last() {
		if(this.isEmpty()) return null;
		return this.elements[size-1];	}

	@Override
	public int firstIndex(E obj) {
		for(int i=0; i<this.size;i++) {
			if(elements[i].equals(obj)) return i;
		}
		return -1;
	}

	@Override
	public int lastIndex(E obj) {
		int lastIdx = -1;
		for(int i=0; i<this.size;i++) {
			if(elements[i].equals(obj)) lastIdx = i;
		}
		return lastIdx;
	}

	@Override
	public boolean contains(E obj) {
		for(E e:elements) {
			if(e.equals(obj)) return true;
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void clear() {
		/**
		 * In order to help the GC we set used positions to null
		 */
		for(int i=0; i<size;i++) {
			elements[i] = null;
		}
		elements = (E[]) new Object[INIT_CAP]; // new empty array
		size = 0; // the list is empty
	}

	@Override
	public Iterator<E> iterator() {
		Iterator<E> itr = new ArrayListIterator();
		return itr;
	}

	// -------------- Private Methods and Classes ------------ \\

	/**
	 * This helper method is called any time the Array is full
	 * and the capacity needs to be changed
	 */
	@SuppressWarnings("unchecked")
	private void changeCapacity(int newCap) { 
		E[] newElement = (E[]) new Object[newCap]; 
		for (int i=0; i<size; i++) { 
			newElement[i] = elements[i]; 
			elements[i] = null; 
		} 
		elements = newElement; 
	}

	/**
	 * This helper method moves elements one position to the right
	 * starting from an index specified.
	 */
	private void rotateToR(int index) { 
		for (int pos = this.size -1; pos >= index; pos--)
			elements[pos+1] = elements[pos]; 
	}

	/**
	 * This helper method moves elements one position to the left
	 * starting from an index specified
	 */
	private void rotateToL(int index) { 
		for (int pos = index+1; pos <= this.size-1; pos++)
			elements[pos-1] = elements[pos]; 
		elements[size-1] = null;
	}

	/**
	 * Inner class for ArrayList Iterator implementation
	 */
	private class ArrayListIterator implements Iterator<E>{
		private int current;

		public ArrayListIterator() {
			current = 0;
		}

		@Override
		public boolean hasNext() {
			return current < size;
		}

		@Override
		public E next() throws NoSuchElementException{
			if(!this.hasNext()) throw new NoSuchElementException("No more elements to visit");
			return elements[current++];
		}
	}

}
