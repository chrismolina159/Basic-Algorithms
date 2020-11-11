import java.io.*;
import java.util.*;

public class Solution {

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		HashHeap hh = new HashHeap();
		
		String firstQuery = br.readLine();
    	int numOfQue = Integer.parseInt(firstQuery);
    	hh.heap = new BinaryHeap<Data>(numOfQue);
    	
    	for(int i = 0; i < numOfQue; i++){
    		String line = br.readLine();
    		int indexOfSpace = line.indexOf(" ");
    		String name = line.substring(0, indexOfSpace);
    		long score = Long.parseLong(line.substring(indexOfSpace+1));
    		int pos = hh.size();
    		Data temp = new Data(name, score, pos);
    		hh.insert(temp);
    	}

    	String secondQuery = br.readLine();
    	int numOfQue2 = Integer.parseInt(secondQuery);
    	
    	for(int i = 0; i < numOfQue2; i++){
    		String line = br.readLine();
    		if(line.charAt(0) == '1'){
    			int space = line.indexOf(" ",2);
        		String name = line.substring(2, space);
        		long score = Long.parseLong(line.substring(space+1));
        		hh.update(name, score);
    		}
    		else{
    			long standard = Long.parseLong(line.substring(2));
    			try {
					System.out.println(hh.evaluate(standard));
				} catch (Exception e) {
					System.err.println("Couldn't evaluate");
				}
    		}
    	}
	}
}

class Data implements Comparable{
    String name;
    long score;
    int pos;
    

    Data(String name, long score, int pos) {
        this.name = name;
        this.score = score;
        this.pos=pos;
    }
    
    public int compareTo(Object o){
    	Data d = (Data) o;
    	return Long.compare(this.score, d.score);
    }
    public String toString(){
    	return this.name+","+this.score+","+this.pos;
    }
}
	
class BinaryHeap<E extends Comparable>{
	int sz;
	E [ ] arr;
	 
    public BinaryHeap( ){
        this(10);
    }

    public BinaryHeap(int capacity){
        sz = 0;
        arr = (E[]) new Comparable[ capacity + 1 ];
    }
 
    public BinaryHeap(E[] items){
        sz = items.length;
        arr = (E[]) new Comparable[ ( sz + 2 ) * 11 / 10 ];
        int i = 1;
        
        for( E item : items )
            arr[ i++ ] = item;
    
        buildHeap( );
	}

    public void insert(E x){
        if( sz == arr.length - 1 )
            enlargearr(arr.length * 2 + 1 );

        int hole = ++sz;
        
        for( ; hole > 1 && x.compareTo( arr[ hole / 2 ] ) < 0; hole /= 2 ){
            arr[ hole ] = arr[ hole / 2 ];
            ((Data)arr[hole/2]).pos = hole;
        }
    
        arr[hole] = x;
        ((Data)arr[hole]).pos = hole;
    }


    private void enlargearr(int newSize){
    	E[] initialArr = arr;
		arr = (E[]) new Comparable[newSize];
		
        for(int i = 0; i < initialArr.length; i++)
			arr[ i ] = initialArr[ i ];	  
    }
			       
    public E findMin( ) throws Exception{
        if( isEmpty( ) )
            throw new Exception();
        return arr[ 1 ];
    }

    public E deleteMin( ) throws Exception{
        if( isEmpty( ) )
            throw new Exception();

        E minItem = findMin( );
        arr[ 1 ] = arr[ sz-- ];
        ((Data)arr[1]).pos = 1;
        moveDown( 1 );

        return minItem;
    }

    private void buildHeap( ){
        for( int i = sz / 2; i > 0; i-- )
            moveDown( i );
    }

    /**
    * Test if the priority queue is logically empty.
    * @return true if empty, false otherwise.
    */
    public boolean isEmpty( ){
        return sz == 0;
    }
 
    public void printHeap( ){
 		for( int i = 1; i <= sz; i++ )
 			System.out.print( arr[ i ].toString() + " " );
 		
 		System.out.println( );
    }

    public void moveDown( int hole ){
        int child;
        E temp = arr[ hole ];

        for( ; hole * 2 <= sz; hole = child ){
            child = hole * 2;
            if( child != sz && arr[ child + 1 ].compareTo( arr[ child ] ) < 0 )
                child++;
            if( arr[ child ].compareTo( temp ) < 0 ){
                arr[ hole ] = arr[ child ];
                ((Data)arr[hole]).pos = hole;
            }
            else
                break;
        }
        
        arr[ hole ] = temp;
        ((Data)arr[hole]).pos = hole;
    }
 
}

class HashHeap {
    BinaryHeap<Data> heap;
    Hashtable<String, Data> ht;
    int size;

    public HashHeap(){
        heap = new BinaryHeap<Data>();
        ht = new Hashtable<String, Data>();
        size = 1;
    }

    public void insert(Data d){
    	heap.insert(d);
    	ht.put(d.name, d);
    	size++;
    }
    
    public int size(){
    	return size;
    }
    
    public void update(String name, long improvement){
    	if(ht.containsKey(name)){
    		Data d = ht.get(name);
    		d.score += improvement;
    		try{
    			int p = d.pos;
    			heap.moveDown(p);
    		}
    		catch (Exception e){
    			System.err.println("Couldn't find min");
    		}
    	}
    }
    
    public int evaluate(long standard) throws Exception{
    	while(!heap.isEmpty()){
     		if(((Data)heap.findMin()).score < standard){
     			Data temp = (Data)heap.deleteMin();
     			ht.remove(temp.name);
     			size--;
     		}
     		else
     			break;
     	}
     	return heap.sz;
    }
}
