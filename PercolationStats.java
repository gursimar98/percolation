import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
// Estimates percolation threshold for an N-by-N percolation system.
public class PercolationStats {
    private int indpEXP; 
    private double[] p; 
    private Percolation percolationSys; 
    private double fraction; 
    private double grid; 
    // Perform T independent experiments (Monte Carlo simulations) on an 
    // N-by-N grid.
    public PercolationStats(int N, int T)  {
        this.indpEXP = T; //number of independent experiments
        p = new double[indpEXP];
        for (int x = 0; x < indpEXP; x++) {
           this.percolationSys = new Percolation(N); 
           int count = 0; 
           // loop while the system does not percolate. 
           while (!percolationSys.percolates()) { 
               //pick a random coordinate (i, j)
               int i = StdRandom.uniform(N); 
               int j = StdRandom.uniform(N);
               if (!percolationSys.isOpen(i, j)) {
                   //open the sites that are not open 
                   percolationSys.open(i, j); 
                   count++; //increment the counter with 
                            //every open site.
               } 
           }
           this.grid = (N * N); 
           fraction = (double) count / grid;
           p[x] = fraction; 
        }
    } 
    // Sample mean of percolation threshold.
    public double mean() {
        return StdStats.mean(p); 
    }
    // Sample standard deviation of percolation threshold.
    public double stddev() {
        return StdStats.stddev(p); 
    }
    // Low endpoint of the 95% confidence interval.
    public double confidenceLow() {
        double cLOW; //variable for low confidence level
        double sqrtT; //takes care of the square root
        sqrtT = Math.sqrt(indpEXP); 
        cLOW = mean() - (1.96 * stddev()) / sqrtT;
        return cLOW; 
    }
    // High endpoint of the 95% confidence interval.
    public double confidenceHigh() {
        double cHIGH; //variable for high confidence level
        double sqrtT2;
        sqrtT2 = Math.sqrt(indpEXP); 
        cHIGH = mean() + ((1.96 * stddev()) / sqrtT2);
        return cHIGH; 
    }
    // Test client. [DO NOT EDIT]
    public static void main(String[] args) {
        int N = Integer.parseInt(args[0]);
        int T = Integer.parseInt(args[1]);
        PercolationStats stats = new PercolationStats(N, T);
        StdOut.printf("mean           = %f\n", stats.mean());
        StdOut.printf("stddev         = %f\n", stats.stddev());
        StdOut.printf("confidenceLow  = %f\n", stats.confidenceLow());
        StdOut.printf("confidenceHigh = %f\n", stats.confidenceHigh());
    }
}