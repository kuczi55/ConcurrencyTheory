public class Matrix {
    private double[][] M;
    private final double[][] m;
    private final double[][][] n;
    private final int size;

    public Matrix(int size) {
        this.size = size;
        this.M = new double[size][size+1];
        this.m = new double[size][size];
        this.n = new double[size][size+1][size];
    }

    public double[][] getM() {
        return M;
    }

    public double[][][] getN() {
        return n;
    }

    public double[][] getm() {
        return m;
    }

    public void setM(double[][] m) {
        M = m;
    }

    public int getSize() {
        return size;
    }
}
