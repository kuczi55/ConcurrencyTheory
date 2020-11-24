import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.*;

public class Main {
    private static char currentOperation;
    private static int size;
    static double epsilon = 0.00001;

    public static Matrix loadMatrix(String filename) {
        try {
            Scanner sc = new Scanner(new BufferedReader(new FileReader(filename)));
            if(sc.hasNextLine()) {
                size = Integer.parseInt(sc.nextLine().trim().split(" ")[0]);
                Matrix m = new Matrix(size);
                double[][] tempMatrix = new double[size][size+1];
                try {
                    for (int i = 0; i < size; i++) {
                        if (sc.hasNextLine()) {
                            String[] line = sc.nextLine().trim().split(" ");
                            for (int j = 0; j < size; j++) {
                                tempMatrix[i][j] = Double.parseDouble(line[j]);
                            }
                        } else {
                            return null;
                        }
                    }
                    if (sc.hasNextLine()) {
                        String[] line = sc.nextLine().trim().split(" ");
                        for (int i = 0; i < size; i++) {
                            tempMatrix[i][size] = Double.parseDouble(line[i]);
                        }
                        m.setM(tempMatrix);
                        sc.close();
                        return m;
                    }
                } catch(IndexOutOfBoundsException e) {
                    System.out.println("Wrong data input");
                    System.exit(-1);
                }
            }
            return null;
        } catch(IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        return null;
    }

    public static void runThreads(int amount, Matrix matrix, int currentRow) throws ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(amount);
        List<Future<?>> runningThreads = new LinkedList<>();
        for(int i = 0; i < amount; i++) {
            runningThreads.add(executor.submit(new Task(matrix, currentOperation, currentRow)));
        }
        for(Future<?> f : runningThreads) {
            f.get();
        }
        executor.shutdown();
    }

    public static void scheduler(Matrix matrix) throws ExecutionException, InterruptedException {
        for(int currentRow = 1; currentRow < size; currentRow++) {
            int rowsNumber = size - currentRow;
            currentOperation = 'A';
            runThreads(rowsNumber, matrix, currentRow);
            int numberOfBAndCOperations = rowsNumber * (size + 2 - currentRow);
            currentOperation = 'B';
            runThreads(numberOfBAndCOperations, matrix, currentRow);
            currentOperation = 'C';
            runThreads(numberOfBAndCOperations, matrix, currentRow);
        }
    }

    public static void main(final String[] arguments) {
        String filename = "in.txt";
        if(arguments.length > 0) {
            filename = arguments[0];
        }
        Matrix matrix = loadMatrix(filename);
        if(matrix == null) {
            System.out.println("Couldn't read matrix from file");
            System.exit(-1);
        }
        double[][] M = matrix.getM();
        try {
            scheduler(matrix);
        } catch(InterruptedException | ExecutionException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        for (int i = size - 1; i >= 0; i--)
        {
            for (int k = i - 1; k >= 0; k--)
            {
                double factor = M[k][i] / M[i][i];
                M[k][i] -= factor * M[i][i];
                M[k][size] -= factor * M[i][size];
            }
            M[i][size] /= M[i][i];
            M[i][i] /= M[i][i];
        }
        for (int i = 0; i < size; i++)
        {
            for (int j = 0; j < size; j++)
            {
                if(M[i][j] < epsilon) {
                    M[i][j] = 0.0;
                }
            }
        }
        PrintStream stdout = System.out;
        try {
            File file = new File("out.txt");
            FileOutputStream fos = new FileOutputStream(file);
            PrintStream ps = new PrintStream(fos);
            System.setOut(ps);

            System.out.println(size);
            for (int i = 0; i < matrix.getSize(); i++) {
                for (int j = 0; j < matrix.getSize(); j++) {
                    System.out.print(M[i][j] + " ");
                }
                System.out.println();
            }
            for (int i = 0; i < size; i++) {
                System.out.print(M[i][size] + " ");
            }
            fos.close();
            ps.close();
        } catch(IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        System.setOut(stdout);
        System.out.println("Successfully completed Gaussian elimination");
    }
}