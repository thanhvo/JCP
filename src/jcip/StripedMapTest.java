package jcip;

public class StripedMapTest {
	public static void main(String[] args) {
		StripedMap map = new StripedMap(1000);
		new Thread(new Runnable() {
			public void run() {
				map.put(1, 2);
				map.put(1001, 0);
				map.put(2, 3);
				map.put(3, 4);
				map.put(1, 4);
				map.put(1, 5);
				map.put(2,  500);
				map.put(1001, 999);	
				map.put(1001, 1000);
			}
		}).start();
		
		new Thread(new Runnable() {
			public void run() {
				map.put(3, 2000);
				map.put(2, 1000);
				map.put(5, 1);				
			}
		}).start();
		
		new Thread(new Runnable() {
			public void run() {
				System.out.println(map.get(1));
				System.out.println(map.get(1001));				
				System.out.println(map.get(2));
				System.out.println(map.get(3));
				System.out.println(map.get(5));
			}
		}).start();
	}
}
