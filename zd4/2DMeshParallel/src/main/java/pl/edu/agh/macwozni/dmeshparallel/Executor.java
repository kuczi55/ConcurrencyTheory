package pl.edu.agh.macwozni.dmeshparallel;

import pl.edu.agh.macwozni.dmeshparallel.myProductions.PW;
import pl.edu.agh.macwozni.dmeshparallel.myProductions.PI;
import pl.edu.agh.macwozni.dmeshparallel.myProductions.PS;
import pl.edu.agh.macwozni.dmeshparallel.myProductions.PC;
import pl.edu.agh.macwozni.dmeshparallel.mesh.Vertex;
import pl.edu.agh.macwozni.dmeshparallel.mesh.GraphDrawer;
import pl.edu.agh.macwozni.dmeshparallel.parallelism.BlockRunner;
import pl.edu.agh.macwozni.dmeshparallel.production.IProduction;
import pl.edu.agh.macwozni.dmeshparallel.production.PDrawer;

import java.util.LinkedList;
import java.util.List;

public class Executor extends Thread {
    
    private final BlockRunner runner;
    private final int N;

    public Executor(BlockRunner _runner, int _N){
        this.runner = _runner;
        this.N = _N;
    }

    @Override
    public void run() {
        List<IProduction<Vertex>> oldProductions = new LinkedList();
        List<IProduction<Vertex>> newProductions = new LinkedList();

        PDrawer drawer = new GraphDrawer();
        //axiom
        Vertex s = new Vertex(null, null, null, null, "S");
        drawer.draw(s);

        PI pi = new PI(s, drawer);
        this.runner.addThread(pi);

        //start threads
        this.runner.startAll();
        drawer.draw(s);

        oldProductions.add(pi);

        for(int i = 0; i < N-1; i++) {
            List<IProduction<Vertex>> pcList = new LinkedList<>();

            IProduction<Vertex> pw = new PW(oldProductions.get(0).getObj(), drawer);

            for(IProduction<Vertex> p : oldProductions) {
                Vertex current = p.getObj();
                newProductions.add(new PS(current, drawer));
                if(current.getNorth() != null && current.getNorth().getEast() != null
                    && current.getNorth().getEast().getSouth() != null) {
                    pcList.add(new PC(current.getNorth().getEast().getSouth(), drawer));
                }
            }

            this.runner.addThread(pw);
            runThreads(oldProductions, newProductions, drawer, s, pcList);
            oldProductions.add(pw);
            oldProductions.addAll(newProductions);
            newProductions.clear();
        }

        for(int i = 0; i < N; i++) {
            List<IProduction<Vertex>> pcList = new LinkedList<>();

            for(int j = 0; j < oldProductions.size(); j++) {
                Vertex current = oldProductions.get(j).getObj();
                if(current.getNorth() != null && current.getNorth().getEast() != null
                        && current.getNorth().getEast().getSouth() != null) {
                    pcList.add(new PC(current.getNorth().getEast().getSouth(), drawer));
                }
                if(j != oldProductions.size() - 1) {
                    newProductions.add(new PS(current,drawer));
                }
            }

            runThreads(oldProductions, newProductions, drawer, s, pcList);
            oldProductions.addAll(newProductions);
            newProductions.clear();
        }

        this.runner.startAll();

        //done
        System.out.println("done");
    }

    private void runThreads(List<IProduction<Vertex>> oldProductions, List<IProduction<Vertex>> newProductions, PDrawer drawer, Vertex s, List<IProduction<Vertex>> pcList) {
        for (IProduction<Vertex> ps : newProductions)
            this.runner.addThread(ps);
        for (IProduction<Vertex> pc : pcList)
            this.runner.addThread(pc);

        this.runner.startAll();
        drawer.draw(s);

        oldProductions.clear();
    }
}
