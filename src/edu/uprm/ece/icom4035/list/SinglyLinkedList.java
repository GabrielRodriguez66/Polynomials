package edu.uprm.ece.icom4035.list;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class SinglyLinkedList<E> implements List<E> {
	private Node header;
	private int size;

	public SinglyLinkedList() {
		this.header = new Node();
		this.size = 0;
	}

	@Override
	public int size() {return this.size;}

	@Override
	public boolean isEmpty() {return this.size == 0;}

	@Override
	public void add(E obj) {
		Node nuevo = new Node(obj);
		this.findNodeAt(size-1).setNext(nuevo);
		size++;
	}

	@Override
	public void add(int index, E obj) throws IndexOutOfBoundsException {
		if(index<0 || index>this.size) throw new IndexOutOfBoundsException("add: Invalid index = "+index);

		Node prev = this.findNodeAt(index-1);
		Node nuevo = new Node(obj, prev.getNext());
		prev.setNext(nuevo);
		size++;
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
	public boolean remove(int index)  throws IndexOutOfBoundsException {
		if(index<0 || index>=size) throw new IndexOutOfBoundsException("remove: Invalid index = "+index);
		Node prev = this.findNodeAt(index-1);
		Node ntr = prev.getNext();
		prev.setNext(ntr.getNext());
		ntr.clear();
		size--;
		return true;
	}

	@Override
	public int removeAll(E obj) {
		int count = 0; Node curr = header;
		while(curr.getNext() != null) {
			if(curr.getNext().getElement().equals(obj)) {
				Node ntr = curr.getNext();
				curr.setNext(ntr.getNext());
				ntr.clear();
				count++;
			}else {
				curr = curr.getNext();
			}
		}
		size -= count;
		return count;
	}

	@Override
	public E get(int index) throws IndexOutOfBoundsException {
		if(index<0 || index>=size) throw new IndexOutOfBoundsException("get: Invalid index = "+index);
		return this.findNodeAt(index).getElement(); 
	}

	@Override
	public E set(int index, E obj)throws IndexOutOfBoundsException {
		if(index<0 || index>=size) throw new IndexOutOfBoundsException("get: Invalid index = "+index);
		Node nts = this.findNodeAt(index);
		E etr = nts.getElement();
		nts.setElement(obj);
		return etr; 
	}

	@Override
	public E first() {
		if(this.isEmpty()) return null;
		return this.header.getNext().getElement();
	}

	@Override
	public E last() {
		if(this.isEmpty()) return null;
		return this.findNodeAt(size-1).getElement();
	}

	@Override
	public int firstIndex(E obj) {
		int idx = 0;
		for(E e:this) {
			if(e.equals(obj)) return idx;
			idx++;
		}
		return -1;
	}

	@Override
	public int lastIndex(E obj) {
		int lastIdx = -1, i = 0;
		for(E e:this) {
			if(e.equals(obj)) lastIdx = i;
			i++;
		}
		return lastIdx;
	}

	@Override
	public boolean contains(E obj) {
		for(E e:this) {
			if(e.equals(obj)) return true;
		}
		return false;
	}

	@Override
	public void clear() {
		Node c = header;
		while(c != null) {
			Node ntc = c;
			c = c.getNext();
			ntc.clear();
		}
		size = 0;
	}

	@Override
	public Iterator<E> iterator() {
		return new SinglyLinkedListIterator();
	}

	// -------------- Private Methods and Classes ------------ \\

	private class Node{
		private E element;
		private Node next;

		public Node(E e, Node n) {
			this.element = e;
			this.next = n;
		}

		public Node(E e) {
			this(e,null);
		}

		public Node() {
			this(null,null);
		}

		public E getElement() {
			return this.element;
		}

		public Node getNext() {
			return next;
		}

		public void setElement(E e) {
			this.element = e;
		}

		public void setNext(Node n) {
			this.next = n;
		}

		public void clear() {
			this.setElement(null);
			this.setNext(null);
		}
	}


	//Inner class for ArrayList Iterator implementation
	private class SinglyLinkedListIterator implements Iterator<E>{
		private Node current;

		public SinglyLinkedListIterator() {
			current = header.getNext();
		}

		@Override
		public boolean hasNext() {
			return current != null;
		}

		@Override
		public E next() throws NoSuchElementException{
			if(!this.hasNext()) throw new NoSuchElementException("No more elements to visit");
			E etr = current.getElement();
			current = current.getNext();
			return etr;
		}
	}

	private Node findNodeAt(int idx) {
		if(idx==-1) return header;
		Node c = header.getNext();
		for(int i=0;i<idx;i++) {
			c = c.getNext();
		}
		return c;
	}

}
