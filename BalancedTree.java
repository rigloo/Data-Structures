/*  Sergio Santana
    cssc0938
 */
package data_structures;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.TreeMap;

public class BalancedTree<K extends Comparable<K>,V> implements DictionaryADT<K,V>  {
	//private int currentSize;
	TreeMap<K,V> treeMap;
	long modCounter;
	public BalancedTree() {
		treeMap=new TreeMap<K,V>();
		modCounter=0;
	}

	@Override
	public boolean contains(K key) {
		return treeMap.containsKey(key);
	}

	@Override
	public boolean add(K key, V value) {
		if(contains(key)) return false;
		treeMap.put(key, value);
		modCounter++;
		return true;

	}

	@Override
	public boolean delete(K key) {
		if(treeMap.remove(key)==null) return false;
		modCounter++;
		return true;
	}

	@Override
	public V getValue(K key) {
		return treeMap.get(key);
	}

	@Override
	public K getKey(V value) { 
		//doesnt use iterator because if i were to get 
		//every key and call getValue(key)
		//would be 0(nlog(n))
		//its simpler to just use 
		//the Entry class getValue() method which is 0(n) instead
		for(Map.Entry<K,V> entry : treeMap.entrySet())
			if(((Comparable<V>)entry.getValue()).compareTo(value)==0)
				return entry.getKey();
		return null;

	}

	@Override
	public int size() {
		return treeMap.size();
	}
	@Override
	public boolean isFull() {
		return false;
	}

	@Override
	public boolean isEmpty() {
		return treeMap.size()==0;
	}

	@Override
	public void clear() {
		treeMap.clear();
		modCounter=0;
	}
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
		protected Entry<K,V>[] entryArray;
		public IteratorHelper(){
			int index=0;
			idx=0;
			modCheck=modCounter;
			entryArray=new Entry[treeMap.size()];
			for(Map.Entry<K,V> entry : treeMap.entrySet()) 
				entryArray[index++]= entry;
		}
		@Override
		public boolean hasNext() {
			if(modCheck!=modCounter)
				throw new ConcurrentModificationException();
			return idx<treeMap.size();
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
			if(entryArray[idx]==null) throw new NoSuchElementException();
			return (K) entryArray[idx++].getKey();
		}
	}
	class ValueIteratorHelper<V> extends IteratorHelper<V>{
		public ValueIteratorHelper(){
			super();
		}
		public V next(){
			if(entryArray[idx]==null) throw new NoSuchElementException();
			return (V) entryArray[idx++].getValue();
		}
	}
}
