/*  Sergio Santana
    cssc0938
 */
package data_structures;

import java.util.Iterator;

public class Stack<E extends Comparable<E>> implements Iterable<E> {
	LinearListADT<E> list;
	// inserts the object obj into the stack
	public Stack(){
		list=new LinearList<E>();
	}
	public void push(E obj) {
		list.addFirst(obj);
	}

	// pops and returns the element on the top of the stack    
	public E pop() {
		return list.removeFirst();	
	}

	// returns the number of elements currently in the stack
	public int size() {
		return list.size();
	}

	// return true if the stack is empty, otherwise false
	public boolean isEmpty()  {
		return list.isEmpty();
	}

	// returns but does not remove the element on the top of the stack    
	public E peek() {
		return list.peekFirst();

	}

	// returns true if the object obj is in the stack,
	// otherwise false   
	public boolean contains(E obj) {
		return list.contains(obj);

	}

	// returns the stack to an empty state    
	public void makeEmpty() {
		list.clear();
	}

	// removes the Object obj if it is in the stack and
	// returns true, otherwise returns false.
	public boolean remove(E obj) {
		return list.remove(obj)!=null;
	}

	// returns a iterator of the elements in the stack.  The elements
	// must be in the same sequence as pop() would return them.    
	public Iterator<E> iterator(){
		return list.iterator();

	}
}
