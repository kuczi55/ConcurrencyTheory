package zd3;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws InterruptedException, IOException {
        int size = 5;
        int amountOfDinners = 10;
        Fork[] forks = new Fork[size];
        Philosopher[] philosophers = new Philosopher[size];

        for(int i = 0; i < size; i++) {
            forks[i] = new Fork();
        }

        for(int i = 0; i < size; i++) {
            philosophers[i] = new Philosopher(i, forks[i], forks[(i+1) % size], amountOfDinners);
        }

        for(int i = 0; i < size; i++) {
            philosophers[i].start();
        }

        for(int i = 0; i < size; i++) {
            philosophers[i].join();
        }

        File f = new File(size + "starv" + amountOfDinners + ".txt");
        f.delete();
        FileWriter fr = new FileWriter(f, true);
        System.out.println("\nStarving result:");
        for(int i = 0; i < size; i++) {
            System.out.println("Philosopher " + i + " average thinking time: " + philosophers[i].averageThinkingTime() + " ms");
            fr.write(i + " " + philosophers[i].averageThinkingTime() + "\n");
        }
        fr.close();
    }
}
