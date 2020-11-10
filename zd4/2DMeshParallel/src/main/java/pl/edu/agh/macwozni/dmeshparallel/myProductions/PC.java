package pl.edu.agh.macwozni.dmeshparallel.myProductions;

import pl.edu.agh.macwozni.dmeshparallel.mesh.Vertex;
import pl.edu.agh.macwozni.dmeshparallel.production.AbstractProduction;
import pl.edu.agh.macwozni.dmeshparallel.production.PDrawer;

public class PC extends AbstractProduction<Vertex> {

    public PC(Vertex _obj, PDrawer<Vertex> _drawer) {
        super(_obj, _drawer);
    }

    @Override
    public Vertex apply(Vertex m) {
        System.out.print("->PC");
        Vertex m1 = m.getNorth().getWest().getSouth();
        m.setWest(m1);
        m1.setEast(m);
        return m;
    }
}
