import java.util.ArrayList;
import java.util.LinkedList;

/**
 * A class representing a weighted graph.
 * https://algs4.cs.princeton.edu/43mst/EdgeWeightedGraph.java.html
 *
 * @author Nicole Nkala 25022318
 */
public class Graph {
    int verts;
    public static LinkedList<Edge>[] adjacencylist;
    ArrayList<Edge> adjlist = new ArrayList<>();

    /**
     * Constructs a graph with the given number of vertices.
     *
     * @param vertices the number of vertices in the graph
     */
    Graph(int vertices) {
        this.verts = vertices;
        adjacencylist = new LinkedList[vertices];
        // initialize adjacency lists for all the vertices
        for (int i = 0; i < vertices; i++) {
            adjacencylist[i] = new LinkedList<>();
        }
    }

    /**
     * Adds a weighted edge to the graph between the given start state and destination vertices.
     *
     * @param startState  the starting vertex of the edge
     * @param destination the destination vertex of the edge
     * @param weight      the weight of the edge
     */
    public void addEdge(int startState, int destination, int weight) {
        Edge edge = new Edge(startState, destination, weight);
        adjacencylist[startState].add(edge);
        adjlist.add(edge);

    }

    /**
     * This method prints the graph by iterating over each vertex and its adjacency list
     * and printing the edges connecting each vertex to its neighbors along with their weights.
     */
    public void printGraph() {
        for (int i = 0; i < verts; i++) { //loop through the vertices
            LinkedList<Edge> list = adjacencylist[i]; // add those vertices to the linked list
            for (int j = 0; j < list.size(); j++) { // looping through the linked list
                //System.out.println(i + " " + list.get(j).destination + " " + list.get(j).weight);

            }
        }
    }
}

/**
 * Represents an edge in a graph with a starting vertex, a destination vertex, and a weight.
 *
 * @author Nicole Nkala 25022318
 */
class Edge {
    int startState;
    int destination;
    int weight;

    /**
     * Constructs a new edge with the given start state, destination, and weight.
     *
     * @param startState  the starting vertex of the edge
     * @param destination the destination vertex of the edge
     * @param weight      the weight of the edge
     */
    Edge(int startState, int destination, int weight) {
        this.startState = startState;
        this.destination = destination;
        this.weight = weight;
    }
}


