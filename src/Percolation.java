import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private final int FAUCET;
    private final int DRAIN;
    private int N;
    private WeightedQuickUnionUF UF;
    private boolean[][] grid;
    private int openedSitesCount;

    public Percolation(int N)                // create N-by-N grid, with all sites initially blocked
    {
        if (N <= 0) throw new IllegalArgumentException();
        this.N = N;
        grid = new boolean[N][N]; // Default bool value is false -- all sites blocked.
        UF = new WeightedQuickUnionUF(N * N + 2);
        openedSitesCount = 0;

        FAUCET = N * N;
        DRAIN = N * N + 1;
    }

    public void open(int row, int col)       // open the site (row, col) if it is not open already
    {
        final boolean OPEN = true;

        if (!isValidGridAddress(row, col)) throw new IndexOutOfBoundsException();
        if (isOpen(row, col)) return;

        grid[row][col] = OPEN;
        openedSitesCount++;

        handleUnion(row, col);
    }

    public boolean isOpen(int row, int col)  // is the site (row, col) open?
    {
        if (!isValidGridAddress(row, col)) throw new IndexOutOfBoundsException();
        return grid[row][col];
    }

    public boolean isFull(int row, int col)  // is the site (row, col) full?
    {
        if (!isValidGridAddress(row, col)) throw new IndexOutOfBoundsException();
        return UF.connected(FAUCET, gridToUF(row, col));

    }

    public int numberOfOpenSites()           // number of open sites
    {
        return openedSitesCount;
    }

    public boolean percolates()              // does the system percolate?
    {
        return UF.connected(FAUCET, DRAIN);
    }

    private int gridToUF(int row, int col) {
        return N * row + col;
    }

    private void handleUnion(int row, int col) {

        // Let water in
        if (row == 0) UF.union(FAUCET, gridToUF(row, col));

        // Let water out
        if (row == N - 1) UF.union(DRAIN, gridToUF(row, col));

        // Up-Neighbor
        int upRow = row - 1;
        if (isValidGridAddress(upRow, col) && isOpen(upRow, col))
            UF.union(gridToUF(row, col), gridToUF(upRow, col));

        // Down-Neighbor
        int downRow = row + 1;
        if (isValidGridAddress(downRow, col) && isOpen(downRow, col))
            UF.union(gridToUF(row, col), gridToUF(downRow, col));

        // Left-Neighbor
        int leftCol = col - 1;
        if (isValidGridAddress(row, leftCol) && isOpen(row, leftCol))
            UF.union(gridToUF(row, col), gridToUF(row, leftCol));

        // Right-Neighbor
        int rightCol = col + 1;
        if (isValidGridAddress(row, rightCol) && isOpen(row, rightCol))
            UF.union(gridToUF(row, col), gridToUF(row, rightCol));
    }

    private boolean isValidGridAddress(int row, int col) {
        return row >= 0 && row < N &&  col >= 0 && col < N ;
    }

    public static void main(String[] args)   // unit testing (suggested)
    {
        int successes = 0;
        int totalTests = 0;

        // GROUP: Constructor
        System.out.println("Constructor:");
        boolean zeroSideLengthFails = false;
        boolean negativeSideLengthFails = false;

        try {
            totalTests++;
            Percolation a = new Percolation(0);
        } catch (IllegalArgumentException e) {
            zeroSideLengthFails = true;
            successes++;
        } finally {
            if(zeroSideLengthFails) System.out.println("Zero side length should throw: Success;");
            else System.out.println("Zero side length should throw: Failure;");
        }

        try {
            totalTests++;
            Percolation b = new Percolation(-5);
        } catch (IllegalArgumentException e) {
            negativeSideLengthFails = true;
            successes++;
        } finally {
            if(negativeSideLengthFails) System.out.println("Negative side length should throw: Success;");
            else System.out.println("Negative side length should throw: Failure;");
        }
        System.out.println();

        // GROUP: Open/isOpen Methods
        System.out.println("Open & isOpen Methods:");
        Percolation a = new Percolation(2);
        a.open(0, 0);

        boolean shouldBeOpen = a.isOpen(0, 0);
        totalTests ++;
        boolean shouldBeClosed = !a.isOpen(1, 0);
        totalTests ++;

        if (shouldBeOpen){
            System.out.println("Site should be open: Success;");
            successes++;
        }
        else System.out.println("Site should be open: Failure;");

        if (shouldBeClosed) {
            System.out.println("Site should be closed: Success;");
            successes++;
        }
        else System.out.println("Site should be closed: Failure;");

        System.out.println();

        // GROUP: isFull Method
        System.out.println("isFull Method:");
        a = new Percolation(2);
        a.open(0, 0);
        a.open(1, 0);

        totalTests++;
        if (a.isFull(0, 0) && a.isFull(1, 0)) {
            System.out.println("Sites should be filled: Success;");
            successes++;
        } else System.out.println("Sites should be filled: Failure");

        totalTests++;
        if (!a.isFull(1, 1) && !a.isFull(0, 1)) {
            System.out.println("Sites should not be filled: " +
                    "Success;");
            successes++;
        } else System.out.println("Sites should not be filled: Failure;");

        System.out.println();

        // GROUP: numberOfOpenSites method
        System.out.println("numberOfOpenSites Method:");
        a = new Percolation(2);

        a.open(0, 0);
        a.open(1, 1);

        totalTests++;
        if (a.numberOfOpenSites() == 2) {
            System.out.println("Number of open sites is correct: Success;");
            successes++;
        } else System.out.println("Number of open sites is correct: Failure;");

        System.out.println();

        //GROUP: Percolates method
        System.out.println("percolates Method:");

        a = new Percolation(2);
        a.open(0, 0);
        a.open(1, 0);

        totalTests++;
        if (a.percolates()) {
            System.out.println("System should percolate: Success;");
            successes++;
        } else System.out.println("System should percolate: Failure;");

        a = new Percolation(2);
        a.open(0, 0);
        a.open(1, 1);

        totalTests++;
        if (!a.percolates()) {
            System.out.println("System should not percolate: Success;");
            successes++;
        } else System.out.println("System should not percolate: Failure;");

        System.out.println();
        System.out.println("Tests successful: " + successes);
        System.out.println("Tests run: " + totalTests );
        System.out.println("Tests failed: " + (totalTests - successes));

    }

}