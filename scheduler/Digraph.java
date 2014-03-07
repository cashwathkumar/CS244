package scheduler;


import java.util.LinkedList;

public class Digraph
{
	 private final int V;
	 private final LinkedList<Integer>[] adj;
	 
	 public Digraph(int V)
	 {
		 this.V = V;
		 adj = (LinkedList<Integer>[])new LinkedList[V+1];
		 
		 for (int v = 0; v < V+1; v++)
			 adj[v] = new LinkedList<Integer>();
	 }
	 
	 public int V()
	 {
		 return V;
	 }
	 
	 public void addEdge(int v, int w)
	 {
	 	adj[v].add(w); 
	 }
	 public Iterable<Integer> adj(int v)
	 { 
		 return adj[v]; 
	 }
}
