/*  Sergio Santana
    cssc0938
 */
package data_structures;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class LinearList<E extends Comparable<E>> implements LinearListADT<E>, Iterable<E>  {
	private int currentSize=0;
	private long modCounter=0;;

	private class Node<E>{
		Node<E> next;
		Node<E> prev;
		E data;
		public Node(E data){
			this.data=data; 
			next=null;
			prev=null;
		}
	} 

	private Node<E> head,tail;

	public LinearList() {
		head=tail=null;
	}

	public boolean addFirst(E obj) { //return false if list full
		Node<E> newNode=new Node<E>(obj);
		if(head==null) {head=newNode; tail=newNode;}
		else { head.prev=newNode;
		newNode.next=head;
		head=newNode;
		}
		currentSize++;
		modCounter++;
		return true;
	}
	public boolean addLast(E obj) {
		Node<E> newNode=new Node<E>(obj);
		if(head==null){head=newNode; tail=newNode;}
		else{
			newNode.prev=tail;
			tail.next=newNode;
			tail=newNode;
		}
		currentSize++;
		modCounter++;  
		return true;
	}

	public E removeFirst() {
		if(head==null) return null; //is size 0  
		E temp=head.data;
		head=head.next;
		if(head==null) tail=null; //if size is one
		else head.prev=null;
		currentSize--;
		modCounter++;
		return temp;
	}

	public E removeLast() {
		if(head==null) return null;  //if size 0 get out
		E temp=tail.data;
		tail=tail.prev;
		if(tail==null) head=null; //this happens only if size is 1!!
		else tail.next=null; 
		currentSize--;
		modCounter++;
		return temp;
	}

	public E remove(E obj) {
		Node<E> locat=head;

		while(locat!=null){
			if(obj.compareTo(locat.data)==0) break;
			locat=locat.next;
		}
		if(locat==null) return null; //if size is 0 or not found
		if(head==locat) removeFirst();//if remove at head
		else if (tail==locat) removeLast();//if remove at tail
		else {locat.prev.next=locat.next; //base case
		locat.next.prev=locat.prev;
		currentSize--;	
		modCounter++;
		}
		return locat.data;
	}

	public E peekFirst() {
		if(head==null) return null;
		E temp=head.data;
		return temp;
	}   

	public E peekLast() {
		if(tail==null) return null;
		E temp=tail.data;
		return temp;
	}

	public boolean contains(E obj) {
		return find(obj)!=null;
	}

	public E find(E obj) {
		for(E tmp:this)  //uses the Iterator=this.iterator();
			if(obj.compareTo(tmp)==0)
				return tmp;
		return null;

	}
	public void clear() {
		head=null;
		tail=null;
		currentSize=0;
		modCounter++;
	}

	public boolean isEmpty() {
		return currentSize==0;
	}

	public boolean isFull() {
		return false;
	}

	public int size() {
		return currentSize;
	}

	public Iterator<E> iterator() {
		return new IteratorHelper();
	}
	class IteratorHelper implements Iterator<E>{
		private Node<E> tmp;
		private long modCheck;
		public IteratorHelper(){
			modCheck=modCounter;
			tmp=head;
		}
		public boolean hasNext() {
			if(modCheck!=modCounter) throw new ConcurrentModificationException();
			return tmp!=null;
		}
			
		public E next() {
			if(!hasNext()) throw new NoSuchElementException();
			E retData=tmp.data;
			tmp=tmp.next;
			return retData;
		}
		public void remove(){throw new UnsupportedOperationException();
		}
	}
}
