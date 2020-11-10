package pl.edu.agh.macwozni.dmeshparallel;

import pl.edu.agh.macwozni.dmeshparallel.parallelism.ConcurentBlockRunner;

class Application {

    public static void main(String args[]) {

        Executor e = new Executor(new ConcurentBlockRunner(),
                    args.length > 0 ? Integer.parseInt(args[0]) : 3);
        e.start();
    }
}
