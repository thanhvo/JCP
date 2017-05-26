package jcip;

import java.util.*;
import java.util.concurrent.*;

public class ReadWriteMapTest {
	public static void main(String[] args) {
		HashMap<Integer, Integer> base_map = new HashMap<Integer, Integer>();
		ReadWriteMap<Integer, Integer> map = new ReadWriteMap<Integer, Integer>(base_map);
		map.put(1, 0);
		System.out.println(map.get(1));				
		
		new Thread(new Runnable() {
			public void run() {
				map.put(1, 2);
				map.put(2, 7);
			}
		}).start();
		
		new Thread(new Runnable() {
			public void run() {
				map.put(2, 5);
				System.out.println(map.get(2));
				System.out.println(map.get(1));
				map.put(1, 3);
			}
		}).start();
		
		new Thread(new Runnable() {
			public void run() {				
				System.out.println(map.get(1));
				System.out.println(map.get(2));
			}
		}).start(); 
	}
}
