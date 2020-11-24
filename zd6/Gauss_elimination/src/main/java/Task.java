public class Task implements Runnable {
    private final char currentOperation;
    private final int currentRow;
    private final double[][] m;
    private final double[][] M;
    private final double[][][] n;
    private final int size;

    public Task(Matrix matrix, char currentOperation, int currentRow) {
        this.currentOperation = currentOperation;
        this.currentRow = currentRow;
        this.m = matrix.getm();
        this.M = matrix.getM();
        this.n = matrix.getN();
        this.size = matrix.getSize();
    }

    public void operationA(int i, int k) {
        i--;
        k--;

        m[k][i] = M[k][i] / M[i][i];
    }

    public void operationB(int i, int j, int k) {
        i--;
        j--;
        k--;

        n[k][j][i] = M[i][j] * m[k][i];
    }

    public void operationC(int i, int j, int k) {
        i--;
        j--;
        k--;

        M[k][j] -= n[k][j][i];
    }

    @Override
    public void run() {
        int i = currentRow;
        int j, k;
        int threadNumber = Integer.parseInt(Thread.currentThread().getName().trim().split("-")[3]);

        j = i + (threadNumber - 1) % (size + 2 - i);

        int tempK = i + 1 + (threadNumber - 1) / (size + 2 - i);
        switch (currentOperation) {
            case 'A':
                k = i + threadNumber;
                operationA(i, k);
                break;
            case 'B':
                k = tempK;
                operationB(i, j, k);
                break;
            case 'C':
                k = tempK;
                operationC(i, j, k);
                break;
            default:
                System.out.println("Wrong operation");
                System.exit(-1);
        }
    }
}
