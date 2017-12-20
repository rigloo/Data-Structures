/*  Sergio Santana
    cssc0938
 */
package data_structures;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class BinarySearchTree<K extends Comparable<K>,V> implements DictionaryADT<K,V> {
	private class Node<K,V> {
		private K key;
		private V value;
		private Node<K,V> leftChild;
		private Node<K,V> rightChild;
		public Node(K k, V v) {
			key=k;
			value=v;
			leftChild=rightChild=null;
		}
	} //end class node
	long modCounter;
	private Node<K,V> root;   //first node of tree
	private int currentSize;
	public BinarySearchTree(){
		root=null;
		currentSize=0;
		modCounter=0;
	}

	@Override
	public boolean contains(K key) {
		return find(key,root)!=null;
	}

	@Override
	public boolean add(K key, V value) {
		if(contains(key)) return false;
		if(root==null)
			root=new Node<K,V>(key,value);
		else
			insert(key,value,root,null,false);
		currentSize++;
		modCounter++;
		return true;
	}
	private void insert(K key, V value, Node<K, V> n, Node<K,V> parent, boolean wasLeft) {
		if(n==null){
			if(wasLeft) parent.leftChild=new Node<K,V>(key,value);
			else parent.rightChild= new Node<K,V>(key,value);
		}
		else if (((Comparable<K>)key).compareTo((K)n.key)<0)
			insert(key,value,n.leftChild,n,true);
		else 
			insert(key,value,n.rightChild,n,false);
	}
	@Override
	public boolean delete(K key) {
		if(isEmpty()) return false;
		//		exit=false;
		wasFound=false;
		if(!delete(key,root,null,false)) return false;
		currentSize--; modCounter++;
		return true;
	}
	private boolean wasFound;
	private boolean delete(K key, Node<K, V> n, Node<K,V> parent, boolean wasLeft) {
		if(n==null) return false;
		if(n.key.compareTo(key)==0){
			wasFound=true;
			if(n.leftChild==null&&n.rightChild==null){
				if(parent==null) root=null;
				else if(wasLeft) parent.leftChild=null;
				else parent.rightChild=null;
			}
			else if(n.leftChild==null){
				if(parent==null) root=n.rightChild;
				else if(wasLeft) parent.leftChild=n.rightChild;
				else parent.rightChild=n.rightChild;
			}
			else if (n.rightChild==null){
				if(parent==null) root=n.leftChild;
				else if(wasLeft) parent.leftChild=n.leftChild;
				else parent.rightChild=n.leftChild;
			}
			else {
				Node<K,V> parent2=null;
				Node<K,V> current=n.rightChild;
				while(current.leftChild!=null){
					parent2=current;
					current=current.leftChild;
				}
				n.key=current.key;
				n.value=current.value;
				if(parent2==null) n.rightChild=current.rightChild;
				else parent2.leftChild=current.rightChild;
			}
			return wasFound;
		}		
		else {
			if(n.key.compareTo(key)>0) delete(key,n.leftChild,n,true);
			else if(n.key.compareTo(key)<0) delete(key,n.rightChild,n,false);
			return wasFound;
		}
	}
	@Override
	public V getValue(K key) {
		Node<K,V> temp;
		temp=find(key,root);
		if(temp==null) return null;
		return temp.value;
	}
	//reASON why I dont instead return value is bc I Use this method to find node 
	private Node<K,V> find(K key, Node<K, V> n) {
		if(n==null) return null;
		if(((Comparable<K>)key).compareTo(n.key)<0)
			return find(key,n.leftChild); //go left
		if(((Comparable<K>)key).compareTo(n.key)>0)
			return find(key,n.rightChild); //go right
		return (Node<K,V>) n; //found
	}
	private K temp;
	public K getKey(V value) {
		temp=null;
		getKey(value,root);
		return temp;

	}
	//better to walk tree
	// and dont use iterator because its a 
	//waste, would have to call getValue() every check
	//would make it O(nlog(n)) instead of 0(n)
	private void getKey(V value,Node<K, V> n) {
		if(n==null) return;
		getKey(value,n.leftChild);
		if(((Comparable<V>)n.value).compareTo(value)==0) temp=n.key;
		getKey(value,n.rightChild);
	}
	@Override 
	public int size() {
		return currentSize;
	}

	@Override
	public boolean isFull() {
		return false;
	}
	@Override
	public boolean isEmpty() {
		return currentSize==0;
	}

	@Override
	public void clear() {
		root=null;
		currentSize=0;
	}

	@Override
	public Iterator keys() {
		return new KeyIteratorHelper<K>();
	}

	@Override
	public Iterator values() {
		return new ValueIteratorHelper<V>();
	}

	abstract class IteratorHelper<E> implements Iterator<E>{
		protected int idx;
		protected long modCheck;
		Node<K,V>[] array;
		int index;
		public IteratorHelper(){
			index=0;
			idx=0;
			modCheck=modCounter;
			array=new Node[currentSize];
			inOrder(root);
		}
		private void inOrder(Node<K, V> n) {
			if(n!=null){
				inOrder(n.leftChild);
				array[index++]=n;
				inOrder(n.rightChild);
			}
		}
		@Override
		public boolean hasNext() {
			if(modCheck!=modCounter)
				throw new ConcurrentModificationException();
			return idx<currentSize;
		}
		@Override
		public abstract E next();
		public void remove(){
			throw new UnsupportedOperationException();
		}
	}
	class KeyIteratorHelper<K> extends IteratorHelper<K>{
		public KeyIteratorHelper(){
			super();
		}
		public K next(){
			if(array[idx]==null) throw new NoSuchElementException();
			return (K) array[idx++].key;

		}
	}
	class ValueIteratorHelper<V> extends IteratorHelper<V>{
		public ValueIteratorHelper(){
			super();
		}
		public V next(){
			if(array[idx]==null) throw new NoSuchElementException();
			return (V) array[idx++].value;
		}
	}
}