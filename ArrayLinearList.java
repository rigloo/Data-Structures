/*
Program #1
Sergio Santana
cssc0938
 */

package data_structures;

import java.util.Iterator;
import java.util.NoSuchElementException;
public class ArrayLinearList<E extends Comparable<E>> implements LinearListADT<E>, Iterable<E> {
	private	int frontIndex=0;
	private	int rearIndex=0;
	private	E[] array;
	private int maxCpcity;
	private	int currentSize=0; 
	public ArrayLinearList(int maxCapacity){
		array = (E[])new Object[maxCapacity];
		maxCpcity=maxCapacity;
	}

	public ArrayLinearList(){
		this(DEFAULT_MAX_CAPACITY);
	}

	@Override
	public boolean addFirst(E obj) {
		//if this is the case frontIndex and rearIndex is same value
		if(currentSize==maxCpcity)
			return false;
		if(currentSize++==0){
			array[frontIndex]=obj;
			return true;
		}
		if(--frontIndex==-1) frontIndex=maxCpcity-1;
		array[frontIndex]=obj;
		return true;
	}

	@Override
	public boolean addLast(E obj) {
		//if this is the case frontIndex and rearIndex is same value
		if(currentSize==maxCpcity)
			return false;
		if(currentSize++==0){
			array[rearIndex]=obj;
			return true;
		}
		if (++rearIndex==maxCpcity) rearIndex=0;
		array[rearIndex]=obj;
		return true;
	}

	@Override
	public E removeFirst() {
		if(currentSize==0)
			return null;
		currentSize--;
		if(frontIndex==rearIndex)
			return array[frontIndex];
		if(++frontIndex==maxCpcity) {frontIndex=0;
		return array[maxCpcity-1];
		}

		else return array[frontIndex-1];
	}

	@Override
	public E removeLast() {
		if(currentSize==0)
			return null;
		currentSize--;
		if(frontIndex==rearIndex) return array[frontIndex];
		if(--rearIndex==-1) {rearIndex=maxCpcity-1;
		return array[0];
		}
		else return array[rearIndex+1];
	}

	@Override
	public E remove(E obj) {

		if(currentSize==0) return null;
		int where=-1;
		int counter=0;
		int i=frontIndex;
		//this loop searches entire array for index of obj
		while(counter<currentSize){
			if(i==maxCpcity) i=0;
			E temp=array[i];
			if(((Comparable<E>)obj).compareTo(temp)==0){
				where=i;
				break;
			}
			i++;
			counter++;
		}
		//exits if can't find obj in list
		if(where==-1)
			return null;
		E tmp=array[where];

		if(rearIndex==frontIndex){
			currentSize--;
			return tmp;
		}
		//this loop shifts all objs after index=where
		for(int m=where;m!=rearIndex;m++){
			if(m+1==maxCpcity) {
				array[m]=array[0];
				m=-1;
			}
			else array[m]=array[m+1];
		}
		currentSize--;
		if(--rearIndex==-1) rearIndex=maxCpcity-1;
		return tmp;
	}

	@Override
	public E peekFirst() {

		if(currentSize==0) return null;

		return array[frontIndex];
	}

	@Override
	public E peekLast() {

		if(currentSize==0) return null;
		return array[rearIndex];
	}

	@Override
	public boolean contains(E obj) {

		return find(obj)!=null;
	}

	
	@Override
	public E find(E obj) {
		for(E tmp:this)  //uses the Iterator=this.iterator();
			if(((Comparable<E>)obj).compareTo(tmp)==0)
				return tmp;
		return null;
	}

	@Override
	public void clear() {
		frontIndex=0;
		rearIndex=0;
		currentSize=0;
	}

	@Override
	public boolean isEmpty() {

		return currentSize==0;
	}

	@Override
	public boolean isFull() {

		return currentSize==maxCpcity;
	}

	@Override
	public int size() {
		return currentSize;
	}

	@Override
	public Iterator<E> iterator() {
		return new IteratorHelper();
	}

	class IteratorHelper implements Iterator<E>{
		private int count, index;

		public IteratorHelper(){
			index=frontIndex;
			count=0;
		}

		@Override
		public boolean hasNext() {
			return count!=currentSize;
		}

		@Override
		public E next() {
			if(!hasNext()) throw new NoSuchElementException();
			E tmp= array[index++];
			if( index==maxCpcity) index=0;
			count++;
			return tmp;
		}
		public void remove(){throw new UnsupportedOperationException();};
	}
}