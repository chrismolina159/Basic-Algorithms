import java.io.*;
import java.util.*;

public class Solution {

	public static void main(String [] args) throws IOException{

		int a;
		int b;
		int c;

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String line = br.readLine();
		StringTokenizer tk = new StringTokenizer(line);
		int n = Integer.parseInt(tk.nextToken());
		Graph graph = new Graph(n*2+1);

		while((line = br.readLine()) != null){
			tk = new StringTokenizer(line);
			a = Integer.parseInt(tk.nextToken());
			b = Integer.parseInt(tk.nextToken());
			c = Integer.parseInt(tk.nextToken());

			if(a != b){
				graph.addEdge(a, b, c);
				graph.addEdge(a+n, b+n, c);
			}
		}
		int result = redBlue(graph);
		System.out.print(result);
	}
	
	public static int redBlue(Graph graph){
		int n = ((graph.size - 1)/2);

		for(int i = 1; i <= n; i++){
			Vertex vert = graph.vertexList[i];
			int length = vert.successors.size();

			for(int j = 0; j < length; j++){
				Vertex v = vert.successors.get(j);
				int edgeWeight = vert.edge.get(j);
				
				if(edgeWeight == 0){
					graph.addEdge(vert.value, (v.value+n), edgeWeight);
				}
			}
		}

		int djikstraResult = dijkstra(graph, graph.vertexList[1]);
		if(djikstraResult == 0 || graph.vertexList[n*2].distance == Integer.MAX_VALUE){
			return -1;
		}

		return graph.vertexList[n*2].distance+1;
	}
	
	public static int dijkstra(Graph graph, Vertex vert){

		int reservoirPaths = 0;
		vert.distance = 0;
		Heap heap = new Heap(graph.size+1);
		heap.insert(vert);

		while(heap.size > 0){
			Vertex v = heap.deleteMin();

			for(int i = 0; i < v.successors.size(); i++){
				Vertex vertexLoop = v.successors.get(i);
				int edgeWeight = v.edge.get(i);

				if((v.distance + edgeWeight) < vertexLoop.distance){
					if(vertexLoop.distance == Integer.MAX_VALUE){
						heap.insert(vertexLoop);
					}
					vertexLoop.distance = v.distance + edgeWeight;
				}
				if(edgeWeight == 0){
					reservoirPaths++;
				}
			}
		}
		return reservoirPaths;
	}
}

class Heap{

	int capacity;
	Vertex [] arr;
	int size = 0;

	public Heap(int capacity){
		this.capacity = capacity;
		this.arr = new Vertex[capacity];
	}

	public void insert(Vertex v){
		arr[size] = v;
		v.pos = size;
		size++;
		percolateUp(v);
	}

	public Vertex deleteMin(){
		Vertex min = arr[0];
		arr[0].pos = -1;
		size--;
		arr[size].pos = 0;
		arr[0] = arr[size];
		percolateDown(arr[size]);
		return min;
	}

	public void percolateUp(Vertex v){
		Vertex current = v;
		int parentPosition = ((v.pos+1)/2-1);

		while(parentPosition >= 0){
			Vertex parent = arr[parentPosition];

			if(parent.distance > current.distance){
				swap(current,parent);
				parentPosition = (((parentPosition + 1)/2)-1);
			}
			else{
				break;
			}
		}
	}

	public void percolateDown(Vertex v){
		Vertex current = v;
		int leftChildPos = (2 * (v.pos+1)) - 1;
		int rightChildPos = (2 * (v.pos+1));

		while(leftChildPos < size){
			rightChildPos = leftChildPos + 1;
			Vertex left = arr[leftChildPos];
			Vertex right = null;

			if(rightChildPos < size){
				right = arr[rightChildPos];
			}

			Vertex newVertexChild = null;

			if(right != null && current.distance > right.distance){
				newVertexChild = right;
			}

			if( (current.distance > left.distance) && (newVertexChild == null || left.distance < right.distance)){
				newVertexChild = left;
			}

			if(newVertexChild == null){
				break;
			}

			swap(current,newVertexChild);
			leftChildPos = (2 *(current.pos + 1)) - 1;
		}
	}

	public void swap(Vertex x, Vertex y){
		arr[x.pos] = y;
		arr[y.pos] = x;
		int temp = x.pos;
		x.pos = y.pos;
		y.pos = temp;
	}
}

class Graph{
	
	int size;
	boolean [] vertices;
	Vertex [] vertexList;	

	public Graph(int graphsize){
		this.size = graphsize;
		this.vertices = new boolean[graphsize+1];
		this.vertexList = new Vertex[graphsize + 1];
	}	

	public void addEdge(int a, int b, int c){
		Vertex vertA;
		Vertex vertB;

		if(vertices[a] == false){
			vertA = new Vertex(a);
			vertexList[a] = vertA;
			vertices[a] = true;
		}
		else{
			vertA = vertexList[a];
		}

		if(vertices[b] == false){
			vertB = new Vertex(b);
			vertexList[b] = vertB;
			vertices[b] = true;
		}
		else{
			vertB = vertexList[b];
		}
		vertA.successors.add(vertB);
		vertA.edge.add(c-1);
	}
}

class Vertex{

	ArrayList<Vertex> successors = new ArrayList<Vertex>();
	ArrayList<Integer> edge = new ArrayList <Integer>();
	int value;
	int distance;
	int pos;

	public Vertex(int value){
		this.value = value;
		this.distance = Integer.MAX_VALUE;
	}
}