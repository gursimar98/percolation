import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;
// Models an N-by-N percolation system.
public class Percolation {
    private boolean[][] mark;
    private int count = 0;
    private int sink; 
    private int N; 
    private int source;
    private WeightedQuickUnionUF find;
    private WeightedQuickUnionUF backwashEliminator; 
    private int connect; 
    // Create an N-by-N grid, with all sites blocked.
    public Percolation(int N) {
         if ( N <= 0) {
             throw new IndexOutOfBoundsException("out of bounds");
         }
         this.mark = new boolean[N][N];
         this.N = N; 
         this.sink = (N * N) + 1; //sink is +1 because end tile 
         this.source = 0; //source is 0 because it all starts from here
         this.find = new WeightedQuickUnionUF(N * N + 2); 
         //create another Weighted union object to battle backwash
         this.backwashEliminator = new WeightedQuickUnionUF(N * N + 2); 
         this.count = count; 
         this.connect = connect; 
         for (int i = 0; i < N; i++) {
            //connection to the source
            find.union(encode(0, i), source);
            backwashEliminator.union(encode(0, i), source); 
         }
         for (int j = 0; j < N; j++) {
            //connection to the sink
            find.union(encode((N-1), j), sink); 
         }
    }
    // Open site (i, j) if it is not open already.
    public void open(int i, int j) {
       mark[i][j] = true; //site is open
       count++; //increment for every open site
       /* (j - 1) -> left
        * (j + 1) -> right
        * (i - 1) -> down
        * (i + 1) -> up 
        * find checks if the system percolates 
        * backwashEliminator works with visualizer
        */
       if (i < 0 || i > N || j < 0 || j > N){
           throw new IndexOutOfBoundsException("out of bounds"); 
       }
       if ((j - 1) >= 0) {
            if (isOpen(i, (j - 1))) { 
                //if the tile on the left is open do:
                find.union(encode(i, j), encode(i, (j-1)));
                backwashEliminator.union(encode(i, j), encode(i, (j-1))); 
            }
       }
       if ((j + 1) < N) { 
           if (isOpen(i, (j+1))) {
               //if the tile on the right is open do:
               find.union(encode(i, j), encode(i, (j+1))); 
               backwashEliminator.union(encode(i, j), encode(i, (j+1))); 
            }
       }
       if ((i - 1) >= 0) { 
            if (isOpen((i - 1), j)) {
              //if the tile on the bottom is open do:
              find.union(encode(i, j), encode((i - 1), j)); 
              backwashEliminator.union(encode(i, j), encode((i - 1), j)); 
            }
       }
       if ((i + 1) < N) {
            if (isOpen((i + 1), j)) {
              //if the tile on the top is open do:
              find.union(encode(i, j), encode((i + 1), j)); 
              backwashEliminator.union(encode(i, j), encode((i + 1), j)); 
            }
       }
    }
    // Is site (i, j) open?
    public boolean isOpen(int i, int j) {
        if (i < 0 || i > N || j < 0 || j > N){
           throw new IndexOutOfBoundsException("out of bounds"); 
       }
        return mark[i][j]; 
    }
    // Is site (i, j) full?
    public boolean isFull(int i, int j) {
        //nested if statements to see if the following conditions are met
        //if not throw an index out of bounds exception
        if (i < 0) {
            if (i > N) {
                if (j < 0) {
                    if (j > N) {
                       throw new IndexOutOfBoundsException("out of bounds"); 
                    }
                }
            }
        } 
        return backwashEliminator.connected(source, encode(i, j)); 
    }
    // Number of open sites.
    public int numberOfOpenSites() {
         return count; 
    }
    // Does the system percolate?
    public boolean percolates() {
        return find.connected(source, sink);
    }
    // An integer ID (1...N) for site (i, j).
    private int encode(int i, int j) {
        connect = i * N + j + 1; 
        return connect; 
    }
    // Test client. [DO NOT EDIT]
    public static void main(String[] args) {
        String filename = args[0];
        In in = new In(filename); 
        int N = in.readInt();
        Percolation perc = new Percolation(N);
        while (!in.isEmpty()) {
            int i = in.readInt(); 
            int j = in.readInt();
            perc.open(i, j);
        }
        StdOut.println(perc.numberOfOpenSites() + " open sites");
        if (perc.percolates()) {
            StdOut.println("percolates");
        }
        else {
            StdOut.println("does not percolate");
        }
        // Check if site (i, j) optionally specified on the command line
        // is full.
        if (args.length == 3) {
            int i = Integer.parseInt(args[1]);
            int j = Integer.parseInt(args[2]);
            StdOut.println(perc.isFull(i, j));
        }
    }
}