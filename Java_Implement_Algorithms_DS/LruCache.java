package HashMaps;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class LruCache<K, V>  {

	
	Map<K,V> cache;
	public LruCache(final int capacity)
	{
		
		cache = new LinkedHashMap<K,V>(capacity, 0.9f, true){
			
			@Override 
			protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
	                // When to remove the eldest entry.
	                return size() > capacity; // Size exceeded the max allowed.
	            }
		};
		cache = Collections.synchronizedMap(cache);
		
	}
	
	
	
	public V put(K key , V value)
	{
		return cache.put(key, value);
	}
	
	public V get(K key)
	{
		return cache.get(key);
	}
	
	public void print()
	{
		System.out.println("contents");
		for(V k : cache.values())
		{
			System.out.print("--" + k);
		}	
		System.out.println("");
	}
	
	
	public static void main(String[] args) {
		
		final LruCache<String, Integer> lru = new LruCache<>(5);
		
		for(int i =0 ; i < 5 ;  i ++)
		{
			lru.put("i"+i, i);
		}
		
		lru.print();
		System.out.println("getting - - "+lru.get("i"+4));
		System.out.println("getting - - "+lru.get("i"+3));
		System.out.println("getting - - "+lru.get("i"+2));
		lru.put("i"+7, 7);
		lru.put("i"+8, 8);
		lru.print();
		
		/*Thread t1 = new Thread(new Runnable() {
			
			@Override
			public void run() {
				for(int i =0 ; i < 100 ;  i ++)
				{
					System.out.println("Adding --i:"+i+" --- "+lru.put("hello"+i, i));
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}
		});
		Thread t2 = new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				for(int i =0 ; i < 100 ;  i ++)
				{
					Integer k = lru.get("hello"+i);
					System.out.println("getting--"+i+" - - "+k);
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		t2.start();
		t1.start();*/
		
		
		
	}

}
