
import java.util.LinkedList;

public class Digraph
{
	 private final int V;
	 private final LinkedList<Integer>[] adj;
	 private boolean[] marked;
	 private LinkedList<Integer> reversePost;
	 
	 public Digraph(int V)
	 {
		 this.V = V;
		 adj = (LinkedList<Integer>[])new LinkedList[V];
		 
		 for (int v = 0; v < V; v++)
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
	 
	 public void DepthFirstOrder()
	 {
		 reversePost = new LinkedList<Integer>();
		 marked = new boolean[V()];
		 for (int v = 0; v < V(); v++)
			 if (!marked[v]) dfs(v);
	 }
	  
	 
	 public Iterable<Integer> reversePost()
	 { 
		 return reversePost;
	 }
	 
	 private void dfs(int v)
	 {
		 marked[v] = true;
		 for (int w : adj(v))
			 if (!marked[w]) dfs(w);
		 		reversePost.addFirst(v);
	 }
}
