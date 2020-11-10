package pl.edu.agh.macwozni.dmeshparallel.myProductions;

import pl.edu.agh.macwozni.dmeshparallel.mesh.Vertex;
import pl.edu.agh.macwozni.dmeshparallel.production.AbstractProduction;
import pl.edu.agh.macwozni.dmeshparallel.production.PDrawer;

public class PW extends AbstractProduction<Vertex> {

    public PW(Vertex _obj, PDrawer<Vertex> _drawer) {
        super(_obj, _drawer);
    }

    @Override
    public Vertex apply(Vertex m) {
        System.out.print("->PW");
        Vertex m1 = new Vertex(null, m, null, null, "M");
        m.setWest(m1);
        return m1;
    }
}
