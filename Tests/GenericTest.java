package oop.Tests;

/**
 * Tests for classes implementing generics
 */
public class GenericTest<K, V> {
	 
	  private final K key;
	  private final V value;
	 
	  public GenericTest(K k,V v) {  
	    key = k;
	    value = v;   
	  }
	 
	  public K getKey() {
	    return key;
	  }
	 
	  public V getValue() {
	    return value;
	  }

	}
