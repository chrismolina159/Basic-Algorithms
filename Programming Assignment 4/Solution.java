import java.io.*;
import java.util.*;

public class Solution {

	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String firstLine = br.readLine();
		int indexOfSpace = firstLine.indexOf(" ");
		int numOfLines = Integer.parseInt(firstLine.substring(indexOfSpace+1));
		int rangeOfNums = Integer.parseInt(firstLine.substring(0,indexOfSpace));
		Graph<Integer> graph = new Graph<Integer>();
		
		for(int i = 0; i < numOfLines; i++){
			String line = br.readLine();
			int space = line.indexOf(" ");
			int from = Integer.parseInt(line.substring(0,space));
			int to = Integer.parseInt(line.substring(space+1));
			graph.add(from, to);
		}

		List<Integer> result = new ArrayList<Integer>();
		try {
			result = graph.lexTopSort();
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		if(result == null)
			System.out.println(-1);
		else
			try {
				if (graph.isDag()){
					for(Object e: result){
						System.out.print(e +" ");
					}
				}
				else
					System.out.println(-1);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

}

class BinaryHeap<E extends Comparable>
{

	  int sz;
	  E [ ] arr;
	 
 public BinaryHeap( )
 {
     this(10);
 }

 public BinaryHeap( int capacity )
 {
     sz = 0;
     arr = (E[]) new Comparable[ capacity + 1 ];
 }
 
   public BinaryHeap( E [ ] items )
   {
     sz = items.length;
     arr = (E[]) new Comparable[ ( sz + 2 ) * 11 / 10 ];
     
     int i = 1;
     for( E item : items )
         arr[ i++ ] = item;
     buildHeap( );
	}

 public void insert( E x )
 {
     if( sz == arr.length - 1 )
         enlargearr(arr.length * 2 + 1 );

//     System.out.println("Trying to insert " + ((Data)x).name);
     int hole = ++sz;
     for( ; hole > 1 && x.compareTo( arr[ hole / 2 ] ) < 0; hole /= 2 ){
         arr[ hole ] = arr[ hole / 2 ];
     }
     arr[hole] = x;
 }


 private void enlargearr( int newSize )
 {
	E [] initialArr = arr;
		arr = (E []) new Comparable[newSize];
		      for( int i = 0; i < initialArr.length; i++ )
			       arr[ i ] = initialArr[ i ];	  
 }
			       
 public E findMin( ) throws Exception
 {
     if( isEmpty( ) )
         throw new Exception();
     return arr[ 1 ];
 }
 
 public int find(E n) throws Exception{
	 if( isEmpty() )
		 throw new Exception();
	 else{
		for(int i = 1; i <= sz; i++){
			if (arr[i].equals(n))
				return i;
		}
	return -1;
	 }
 }

 public E deleteMin( ) throws Exception
 {
     if( isEmpty( ) )
         throw new Exception();

     E minItem = findMin( );
     arr[ 1 ] = arr[ sz-- ];
     moveDown( 1 );

     return minItem;
 }

 private void buildHeap( )
 {
     for( int i = sz / 2; i > 0; i-- )
         moveDown( i );
 }

 /**
  * Test if the priority queue is logically empty.
  * @return true if empty, false otherwise.
  */
 public boolean isEmpty( )
 {
     return sz == 0;
 }
 
 public void printHeap( )
 {
 		for( int i = 1; i <= sz; i++ )
 			System.out.print( arr[ i ].toString() + " " );
 		
 		System.out.println( );
 }

 public void moveDown( int hole )
 {
     int child;
     E temp = arr[ hole ];

     for( ; hole * 2 <= sz; hole = child )
     {
         child = hole * 2;
         if( child != sz && arr[ child + 1 ].compareTo( arr[ child ] ) < 0 )
             child++;
         if( arr[ child ].compareTo( temp ) < 0 ){
             arr[ hole ] = arr[ child ];
         }
         else
             break;
     }
     arr[ hole ] = temp;
 }
 
}

class Graph<E> {
    
    private Map<E,List<E>> neighbors = new HashMap<E,List<E>>();

    public void add (E node) {
        if (neighbors.containsKey(node)) return;
        neighbors.put(node, new ArrayList<E>());
    }
    
    public boolean contains (E node) {
        return neighbors.containsKey(node);
    }
  
    public void add (E from, E to) {
        this.add(from);
        this.add(to);
        neighbors.get(from).add(to);
    }

    public Map<E,Integer> inDegree () {
        Map<E,Integer> result = new HashMap<E,Integer>();
        
        for (E v: neighbors.keySet()) 
        	result.put(v, 0);       
        for (E from: neighbors.keySet()) {
            for (E to: neighbors.get(from)) {
                result.put(to, result.get(to) + 1);           
            }
        }
        return result;
    }
    

    public List<Integer> lexTopSort () throws Exception {
        Map<E, Integer> degree = inDegree();
        BinaryHeap<Integer> zeroVertices = new BinaryHeap<Integer>();        
        
        for (E v: degree.keySet()) {
            if (degree.get(v) == 0) zeroVertices.insert((Integer)v);
        }

        List<Integer> result = new ArrayList<Integer>();

        while (!zeroVertices.isEmpty()) {
            int v = zeroVertices.deleteMin();                  
            result.add(v);                          
            for (E neighbor: neighbors.get(v)) {
                degree.put(neighbor, degree.get(neighbor) - 1);
                if (degree.get(neighbor) == 0) zeroVertices.insert((Integer)neighbor);
            }
        }

        if (result.size() != neighbors.size())
        	return null;
        
        return result;
    }

    public boolean isDag () throws Exception{
        return lexTopSort() != null;
    }
}