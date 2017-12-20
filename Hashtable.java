/*  Sergio Santana
    cssc0938
 */
package data_structures;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class Hashtable<K extends Comparable<K>,V> implements DictionaryADT<K,V> {
	long modCounter;
	int currentSize, maxSize, tableSize;  //tableSize must be 30% larger than maxSize
	//maxSize is the max number of elements (max number of phone numbers)
	private LinearListADT<DictionaryNode<K,V>> [] list;

	private class DictionaryNode<K,V> implements Comparable<DictionaryNode<K,V>>{
		K key;
		V value;

		public DictionaryNode(K key, V value){
			this.key=key;
			this.value=value;
		}
		public int compareTo(DictionaryNode<K, V> node) {

			return ((Comparable<K>) key).compareTo(node.key);
		}

	}
	public Hashtable(int size){
		modCounter=0;
		currentSize=0;
		maxSize=size;
		tableSize=(int) (maxSize*1.3f);

		list= new LinearList[tableSize];
		for(int i=0;i<tableSize;i++)
			list[i]=new LinearList<DictionaryNode<K,V>>();
	}

	@Override
	public boolean contains(K key) {
		//DictionaryNode<K,V> tmp=new DictionaryNode<K,V>(key1, null);
		return list[getIndex(key)].contains(new DictionaryNode<K,V>(key,null));
	}

	@Override
	public boolean add(K key, V value) {
		if(isFull()) return false;
		if(contains(key)) return false;
		DictionaryNode<K,V> newNode=new DictionaryNode<K,V>(key,value);
		list[getIndex(key)].addLast(newNode);
		modCounter++;
		currentSize++;
		return true;
	}
	private int getIndex(K key) {
		return ((key.hashCode() & 0x7fffffff)% tableSize);
	}

	@Override
	public boolean delete(K key) {
		if(!contains(key)) return false;
		list[getIndex(key)].remove(new DictionaryNode<K,V>(key,null));
		currentSize--;
		modCounter++;
		return true;
	}


	@Override
	public V getValue(K key) {
		DictionaryNode<K,V> tmp=list[getIndex(key)].find(new DictionaryNode<K,V>(key,null));
		if(tmp==null) return null;
		return tmp.value;
	}

	@Override
	public K getKey(V value1) {
		for(int i=0;i<tableSize;i++)
			for(DictionaryNode<K,V> n:list[i])
				if(((Comparable<V>)n.value).compareTo(value1) == 0)
					return n.key;
		return null;
	}
	@Override
	public int size() {
		return currentSize;
	}

	@Override
	public boolean isFull() {
		return currentSize==maxSize;
	}

	@Override
	public boolean isEmpty() {
		return currentSize==0;
	}

	@Override
	public void clear() {
		currentSize=0;
		modCounter=0;
		for(int i=0;i<tableSize;i++)
			list[i].clear();
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
		protected DictionaryNode[] nodes;
		protected int idx;
		protected long modCheck;
		public IteratorHelper(){
			nodes=new DictionaryNode[currentSize];
			idx=0;
			int j=0;
			modCheck=modCounter;
			for(int i=0;i<tableSize;i++)
				for(DictionaryNode<K,V> n:list[i])
					nodes[j++]=n;
			nodes= shellSort(nodes);
		}
		private DictionaryNode<K,V>[] shellSort(DictionaryNode<K,V>[] nodes1){
			DictionaryNode<K,V>[] n=nodes1;
			int in,out,h=1;
			DictionaryNode<K,V> temp1;
			int size=n.length;
			while(h<=size/3) 
				h=h*3+1;
			while(h>0){
				for(out=h;out<size;out++) {
					//temp1.key=n[out].key;
					temp1=n[out];
					in=out;
					while(in>h-1 && n[in-h].key.compareTo(temp1.key)>=0){
						n[in]=n[in-h];
						in-=h;
					}
					n[in]=temp1;
				}
				h=(h-1)/3;
			}
			return n;
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
			if(nodes[idx]==null) throw new NoSuchElementException();
			return (K) nodes[idx++].key;
		}
	}
	class ValueIteratorHelper<V> extends IteratorHelper<V>{
		public ValueIteratorHelper(){
			super();
		}
		public V next(){
			if(nodes[idx]==null) throw new NoSuchElementException();
			return (V) nodes[idx++].value;
		}
	}
}
