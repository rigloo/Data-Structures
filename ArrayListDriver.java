package data_structures;

public class ArrayListDriver {

	public static void main(String args[]){

		ArrayLinearList<Integer>  dicks=new ArrayLinearList<Integer>(40);

		//		for(int i=0; i < 0; i++) 
		//			dicks.addFirst(i);



		dicks.addFirst(2);
		//dicks.removeLast();
		dicks.addLast(29);
		//dicks.removeFirst();
		dicks.addLast(8);
		dicks.addLast(9);
		dicks.addLast(7);
//		
//		for(Integer r:dicks)
//		System.out.println(r);
//		
	//	System.out.println(dicks.remove(20));
	
		for(Integer r:dicks)
			System.out.println(r);
	

	}
}
