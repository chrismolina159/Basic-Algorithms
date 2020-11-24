import java.io.*;
import java.util.*;

public class Solution {

    public static void main(String[] args) throws IOException{
    	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    	Graph<Integer> graph = new Graph<Integer>();
    	String firstLine = br.readLine();
    	int numOfVert = Integer.parseInt(firstLine.substring(0, firstLine.indexOf(" ")));
    	int numOfEdg = Integer.parseInt(firstLine.substring(firstLine.indexOf(" ")+1));
    	
    	for(int i = 0; i < numOfEdg; i++){
    		String line = br.readLine();
    		int from = Integer.parseInt(line.substring(0, line.indexOf(" ")));
    		int to = Integer.parseInt(line.substring(line.indexOf(" ")+1));
    		graph.add(from, to);
    	}
    	
    	try{
			if(graph.isDag())
				System.out.print("0");
			else{
				List<Integer> test = new ArrayList<Integer>();
				test = graph.topSort();
				System.out.println("1");
				for(int j = 0; j < test.size();j++){
						System.out.print(test.get(j)+" ");
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
			System.out.print("There was an exception");
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
    
    public Map<E,Integer> outDegree () {
        Map<E,Integer> result = new HashMap<E,Integer>();
        for (E v: neighbors.keySet()) result.put(v, neighbors.get(v).size());
        return result;
    }

    public List<E> topSort () {
        List<E> result = new LinkedList<E>();            // Used to store topological sort
        Map<E,String> marked = new HashMap<E,String>();  // Map: vertex -> mark
        ArrayList<Integer> hasCycle= new ArrayList<Integer>(); //pos 0 is hasCycle and pos 1 is do not continue adding to result
        hasCycle.add(0);
        hasCycle.add(0);
        hasCycle.add(0);

        try{
            for (E v: neighbors.keySet()) {
                if (marked.containsKey(v)) 
                	break;
                dfs(v, marked, result, hasCycle);
            }
        }
        catch (RuntimeException e) {
        	System.out.println("ERROR ERROR ERROR");
        	return null;
        	}
        return result;
    }
    
    /**
     * Private dfs for doing topSort.  Last step to solve problem is
     * to create another helper function to check if there is a path from the possible first to the possible last value in the cycle
     * If not do not set has a cycle to 1 and if so run code normally.
     */
    private void dfsHelper(E v, E p, Map<E, String> marked2,ArrayList hasCycle){
    	marked2.put(v, "active");

    	for (E w: neighbors.get(v)) {
    		if(w.equals(p)){
    			hasCycle.set(2, 1);
    			break;
    		}
    		else{
    			if(!hasCycle.get(2).equals(1)  && !marked2.containsKey(w))
    				dfsHelper(w, p,marked2,hasCycle);
    		}
    	}
        	
        marked2.put(v, "done");
    }

    private void dfs (E v, Map<E,String> marked, List<E> result,ArrayList hasCycle) {
        marked.put(v, "active");
        Map<E,Integer> map = outDegree();

        if((int)hasCycle.get(0) == 0){
        	for (E w: neighbors.get(v)) {
        		if (!marked.containsKey(w)){
        			if((int)hasCycle.get(0) == 0){
        				dfs(w, marked, result,hasCycle);
        				if((int)hasCycle.get(0) == 1){
        					break;
        				}
        			}
        			else{
        				break;
        			}
        		}
        		else if(marked.containsKey(w) && map.get(w) > 0){
        			Map<E,String> marked2 = new HashMap<E,String>();
        			dfsHelper(w,v,marked2,hasCycle);
        			if(hasCycle.get(2).equals(1)){
        				hasCycle.set(0, 1);
                		result.add(0,w);
                		break;
        			}
        			else
        				continue;
            	}
        	}
        }
        	
        marked.put(v, "done");
        if((int)hasCycle.get(0) == 1){
        	if(result.contains(v))
        		hasCycle.set(1,1);
        	else if((int)hasCycle.get(1) != 1){
        		result.add(1, v);
        	}
        }
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