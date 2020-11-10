package zd3;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.Semaphore;

public class MainArbiter {
    public static void main(String[] args) throws InterruptedException, IOException {
        int size = 5;
        int amountOfDinners = 10;
        Semaphore arbiter = new Semaphore(size-1);
        ForkArbiter[] forks = new ForkArbiter[size];
        PhilosopherArbiter[] philosophers = new PhilosopherArbiter[size];

        for(int i = 0; i < size; i++) {
            forks[i] = new ForkArbiter();
        }

        for(int i = 0; i < size; i++) {
            philosophers[i] = new PhilosopherArbiter(i, forks[i], forks[(i+1) % size], arbiter, amountOfDinners);
        }

        for(int i = 0; i < size; i++) {
            philosophers[i].start();
        }

        for(int i = 0; i < size; i++) {
            philosophers[i].join();
        }

        File f = new File(size + "arbiter" + amountOfDinners + ".txt");
        f.delete();
        FileWriter fr = new FileWriter(f, true);
        System.out.println("\nArbiter result:");
        for(int i = 0; i < size; i++) {
            System.out.println("Philosopher " + i + " average thinking time: " + philosophers[i].averageThinkingTime() + " ms");
            fr.write(i + " " + philosophers[i].averageThinkingTime() + "\n");
        }
        fr.close();
    }
}
