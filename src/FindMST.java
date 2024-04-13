import java.util.Arrays;

public class FindMST {

    int numberOfVertices; //Number of vertices
    int numberOfEdges; //number of edges
    private int[] vertices; // Vertices array
    private Edge[] MST; // Minimum Spanning Tree Array
    private int mstCost; // Minimum Spanning Tree Cost

    /**
     * Constructor
     *
     * @param edges
     */
    public FindMST(Edge[] edges) {
        //Write your codes here

        this.vertices = CreateVertices ( edges );
        this.numberOfVertices = this.vertices.length;
        this.numberOfEdges = edges.length;
        this.MST = new Edge[this.numberOfVertices - 1];

    }

    /**
     * A utility function to find set of an element i (uses path compression technique)
     *
     * @param subsets
     * @param v
     * @return
     */
    private int find(Subset[] subsets, int v) {
        //Write your codes here

        if ( subsets[v].parent == v ) {
            return subsets[v].parent;
        }
        subsets[v].parent = find ( subsets, subsets[v].parent );

        return subsets[v].parent;

    }


    /**
     * A function that does union of two sets of x and y (uses union by rank)
     *
     * @param subsets
     * @param x
     * @param y
     */
    private void Union(Subset[] subsets, int x, int y) {
        //Write your codes here

        int set_x = find ( subsets, x );
        int set_y = find ( subsets, y );

        if ( subsets[set_x].rank < subsets[set_y].rank ) subsets[set_x].parent = set_y;
        else {
            subsets[set_y].parent = set_x;
            if ( subsets[set_x].rank <= subsets[set_y].rank ) subsets[set_x].rank++;
        }
    }

    /**
     * Define all vertices from Edge array and return Vertex array
     *
     * @param edges
     * @return
     */
    private int[] CreateVertices(Edge[] edges) {
        //Write your codes here

        int[] temp = new int[edges.length * 2];

        for( int i = 0; i < edges.length; i++ ) {
            temp[i] = edges[i].getVertex1 ();
            temp[i + edges.length] = edges[i].getVertex2 ();
        }
        Arrays.sort ( temp );

        int[] arr = new int[temp.length];
        arr[0] = temp[0];
        int index = 1;

        for( int i = 1; i < temp.length; i++ )
            if ( temp[i] != temp[i - 1] ) arr[index++] = temp[i];

        int[] range = Arrays.copyOfRange ( arr, 0, index );

        return range;

    }

    /**
     * Main calculate Minimum Spanning Tree method
     *
     * @param edges
     */
    public void calculateMST(Edge[] edges) {
        //Write your codes here

        Edge[] output;
        output = new Edge[this.numberOfVertices];
        int i;

        for( i = 0; i < this.numberOfVertices; ++i ) {
            output[i] = new Edge ( 0, 0, 0 );
        }
        Arrays.sort ( edges );

        Subset[] subsets = new Subset[this.numberOfVertices];

        i = 0;
        while (i < this.numberOfVertices) {
            subsets[i] = new Subset ();
            ++i;
        }

        int v = this.numberOfVertices - 1;
        while (v >= 0) {
            subsets[v].parent = v;
            subsets[v].rank = 0;
            --v;
        }

        i = 0;
        int edge = 0;

        while (edge < this.numberOfVertices - 1) {
            Edge next_edge;
            next_edge = edges[i++];

            int x = find ( subsets, next_edge.getVertex1 () );
            int y = find ( subsets, next_edge.getVertex2 () );

            if ( x != y ) {
                output[edge++] = next_edge;
                Union ( subsets, x, y );
            }
        }
        this.MST = output;

        for( i = 0; i < output.length; i++ ) this.mstCost += output[i].getWeight ();

    }

    /**
     * Get calculated Minimum Spanning Tree ArrayList
     *
     * @return
     */
    public Edge[] getMST() {
        return MST;
    }

    /**
     * Check vertex is included given arr[] or not
     *
     * @param arr
     * @param vertex
     * @return
     */
    private boolean isVertexIncluded(int[] arr, int vertex) {
        //Write your codes here

        boolean retVal = false;

        for( int j : arr )
            if ( j == vertex ) {
                retVal = true;
                break;
            }
        return retVal;

    }

    /**
     * Get cost of Minimum Spanning Tree
     *
     * @return
     */
    public int getMstCost() {
        return mstCost;
    }

    /**
     * Print Minimum Spanning Tree info
     */
    public void printMST() {
        //Write your codes here

        for( String s : Arrays.asList ( "\nMinimum Spanning Tree", "weight: " + this.getMstCost (), "Edge List:" ) ) {
            System.out.println ( s );
        }

        Arrays.stream ( this.getMST () ).forEach ( System.out::println );

    }
}
